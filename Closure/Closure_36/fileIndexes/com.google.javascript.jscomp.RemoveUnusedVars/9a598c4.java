/*
 * Copyright 2008 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Garbage collection for variable and function definitions. Basically performs
 * a mark-and-sweep type algorithm over the javascript parse tree.
 *
 * For each scope:
 * (1) Scan the variable/function declarations at that scope.
 * (2) Traverse the scope for references, marking all referenced variables.
 *     Unlike other compiler passes, this is a pre-order traversal, not a
 *     post-order traversal.
 * (3) If the traversal encounters an assign without other side-effects,
 *     create a continuation. Continue the continuation iff the assigned
 *     variable is referenced.
 * (4) When the traversal completes, remove all unreferenced variables.
 *
 * If it makes it easier, you can think of the continuations of the traversal
 * as a reference graph. Each continuation represents a set of edges, where the
 * source node is a known variable, and the destination nodes are lazily
 * evaluated when the continuation is executed.
 *
 * This algorithm is similar to the algorithm used by {@code SmartNameRemoval}.
 * {@code SmartNameRemoval} maintains an explicit graph of dependencies
 * between global symbols. However, {@code SmartNameRemoval} cannot handle
 * non-trivial edges in the reference graph ("A is referenced iff both B and C
 * are referenced"), or local variables. {@code SmartNameRemoval} is also
 * substantially more complicated because it tries to handle namespaces
 * (which is largely unnecessary in the presence of {@code CollapseProperties}.
 *
 * This pass also uses a more complex analysis of assignments, where
 * an assignment to a variable or a property of that variable does not
 * necessarily count as a reference to that variable, unless we can prove
 * that it modifies external state. This is similar to
 * {@code FlowSensitiveInlineVariables}, except that it works for variables
 * used across scopes.
 *
 */
class RemoveUnusedVars implements CompilerPass {
  private static final Logger logger =
    Logger.getLogger(RemoveUnusedVars.class.getName());

  private final AbstractCompiler compiler;

  private final boolean removeGlobals;

  private boolean preserveFunctionExpressionNames;

  /**
   * Keep track of variables that we've referenced.
   */
  private final Set<Var> referenced = Sets.newHashSet();

  /**
   * Keep track of variables that might be unreferenced.
   */
  private final List<Var> maybeUnreferenced = Lists.newArrayList();

  /**
   * Keep track of scopes that we've traversed.
   */
  private final List<Scope> allFunctionScopes = Lists.newArrayList();

  /**
   * Keep track of assigns to variables that we haven't referenced.
   */
  private final Multimap<Var, Assign> assignsByVar =
      ArrayListMultimap.create();

  /**
   * The assigns, indexed by the NAME node that they assign to.
   */
  private final Map<Node, Assign> assignsByNode = Maps.newHashMap();

  /**
   * Keep track of continuations that are finished iff the variable they're
   * indexed by is referenced.
   */
  private final Multimap<Var, Continuation> continuations =
      ArrayListMultimap.create();

  RemoveUnusedVars(
      AbstractCompiler compiler,
      boolean removeGlobals,
      boolean preserveFunctionExpressionNames) {
    this.compiler = compiler;
    this.removeGlobals = removeGlobals;
    this.preserveFunctionExpressionNames = preserveFunctionExpressionNames;
  }

  /**
   * Traverses the root, removing all unused variables. Multiple traversals
   * may occur to ensure all unused variables are removed.
   */
  public void process(Node externs, Node root) {
    traverseAndRemoveUnusedReferences(root);
  }

  /**
   * Traverses a node recursively. Call this once per pass.
   */
  private void traverseAndRemoveUnusedReferences(Node root) {
    Scope scope = new SyntacticScopeCreator(compiler).createScope(root, null);
    traverseNode(root, null, scope);

    if (removeGlobals) {
      collectMaybeUnreferencedVars(scope);
    }

    interpretAssigns();
    removeUnreferencedVars();
    for (Scope fnScope : allFunctionScopes) {
      removeUnreferencedFunctionArgs(fnScope);
    }
  }

  /**
   * Traverses everything in the current scope and marks variables that
   * are referenced.
   *
   * During traversal, we identify subtrees that will only be
   * referenced if their enclosing variables are referenced. Instead of
   * traversing those subtrees, we create a continuation for them,
   * and traverse them lazily.
   */
  private void traverseNode(Node n, Node parent, Scope scope) {
    int type = n.getType();
    Var var = null;
    switch (type) {
      case Token.FUNCTION:
        // If this function is a removable var, then create a continuation
        // for it instead of traversing immediately.
        if (NodeUtil.isFunctionDeclaration(n)) {
          var = scope.getVar(n.getFirstChild().getString());
        }

        if (var != null && isRemovableVar(var)) {
          continuations.put(var, new Continuation(n, scope));
        } else {
          traverseFunction(n, scope);
        }
        return;

      case Token.ASSIGN:
        Assign maybeAssign = Assign.maybeCreateAssign(n);
        if (maybeAssign != null) {
          // Put this in the assign map. It might count as a reference,
          // but we won't know that until we have an index of all assigns.
          var = scope.getVar(maybeAssign.nameNode.getString());
          if (var != null) {
            assignsByVar.put(var, maybeAssign);
            assignsByNode.put(maybeAssign.nameNode, maybeAssign);

            if (isRemovableVar(var) &&
                !maybeAssign.mayHaveSecondarySideEffects) {
              // If the var is unreferenced and performing this assign has
              // no secondary side effects, then we can create a continuation
              // for it instead of traversing immediately.
              continuations.put(var, new Continuation(n, scope));
              return;
            }
          }
        }
        break;

      case Token.NAME:
        var = scope.getVar(n.getString());
        if (parent.getType() == Token.VAR) {
          Node value = n.getFirstChild();
          if (value != null && var != null && isRemovableVar(var) &&
              !NodeUtil.mayHaveSideEffects(value)) {
            // If the var is unreferenced and creating its value has no side
            // effects, then we can create a continuation for it instead
            // of traversing immediately.
            continuations.put(var, new Continuation(n, scope));
            return;
          }
        } else {
          // All name references that aren't declarations or assigns
          // are references to other vars.
          if (var != null) {
            // If that var hasn't already been marked referenced, then
            // start tracking it.  If this is an assign, do nothing
            // for now.
            if (isRemovableVar(var)) {
              if (!assignsByNode.containsKey(n)) {
                markReferencedVar(var);
              }
            } else {
              markReferencedVar(var);
            }
          }
        }
        break;
    }

    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
      traverseNode(c, n, scope);
    }
  }

  private boolean isRemovableVar(Var var) {
    // Global variables are off-limits if the user might be using them.
    if (!removeGlobals && var.isGlobal()) {
      return false;
    }

    // Referenced variables are off-limits.
    if (referenced.contains(var)) {
      return false;
    }

    // Exported variables are off-limits.
    if (compiler.getCodingConvention().isExported(var.getName())) {
      return false;
    }

    return true;
  }

  /**
   * Traverses a function, which creates a new scope in javascript.
   *
   * Note that CATCH blocks also create a new scope, but only for the
   * catch variable. Declarations within the block actually belong to the
   * enclosing scope. Because we don't remove catch variables, there's
   * no need to treat CATCH blocks differently like we do functions.
   */
  private void traverseFunction(Node n, Scope parentScope) {
    Preconditions.checkState(n.getChildCount() == 3);
    Preconditions.checkState(n.getType() == Token.FUNCTION);

    final Node body = n.getLastChild();
    Preconditions.checkState(body.getNext() == null &&
            body.getType() == Token.BLOCK);

    Scope fnScope =
        new SyntacticScopeCreator(compiler).createScope(n, parentScope);
    traverseNode(body, n, fnScope);

    collectMaybeUnreferencedVars(fnScope);
    allFunctionScopes.add(fnScope);
  }

  /**
   * For each variable in this scope that we haven't found a reference
   * for yet, add it to the list of variables to check later.
   */
  private void collectMaybeUnreferencedVars(Scope scope) {
    for (Iterator<Var> it = scope.getVars(); it.hasNext(); ) {
      Var var = it.next();
      if (isRemovableVar(var)) {
        maybeUnreferenced.add(var);
      }
    }
  }

  /**
   * Removes unreferenced arguments from a function declaration.
   *
   * @param fnScope The scope inside the function
   */
  private void removeUnreferencedFunctionArgs(Scope fnScope) {
    // Strip unreferenced args off the end of the function declaration.
    Node function = fnScope.getRootNode();
    Preconditions.checkState(function.getType() == Token.FUNCTION);

    Node argList = function.getFirstChild().getNext();
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      Var var = fnScope.getVar(lastArg.getString());
      if (!referenced.contains(var)) {
        if (var == null) {
          throw new IllegalStateException(
              "Function parameter not declared in scope: "
              + lastArg.getString());
        }
        argList.removeChild(lastArg);
        compiler.reportCodeChange();
      } else {
        break;
      }
    }
  }

  /**
   * Look at all the property assigns to all variables.
   * These may or may not count as references. For example,
   *
   * <code>
   * var x = {};
   * x.foo = 3; // not a reference.
   * var y = foo();
   * y.foo = 3; // is a reference.
   * </code>
   *
   * Interpreting assigments could mark a variable as referenced that
   * wasn't referenced before, in order to keep it alive. Because we find
   * references by lazily traversing subtrees, marking a variable as
   * referenced could trigger new traversals of new subtrees, which could
   * find new references.
   *
   * Therefore, this interpretation needs to be run to a fixed point.
   */
  private void interpretAssigns() {
    boolean changes = false;
    do {
      changes = false;

      // We can't use traditional iterators and iterables for this list,
      // because our lazily-evaluated continuations will modify it while
      // we traverse it.
      for (int current = 0; current < maybeUnreferenced.size(); current++) {
        Var var = maybeUnreferenced.get(current);
        if (referenced.contains(var)) {
          maybeUnreferenced.remove(current);
          current--;
        } else {
          boolean assignedToUnknownValue = false;
          boolean hasPropertyAssign = false;

          if (var.getParentNode().getType() == Token.VAR &&
              !NodeUtil.isForIn(var.getParentNode().getParent())) {
            Node value = var.getInitialValue();
            assignedToUnknownValue = value != null &&
                !NodeUtil.isLiteralValue(value, true);
          } else {
            // This was initialized to a function arg or a catch param
            // or a for...in variable.
            assignedToUnknownValue = true;
          }

          for (Assign assign : assignsByVar.get(var)) {
            if (assign.isPropertyAssign) {
              hasPropertyAssign = true;
            } else if (!NodeUtil.isLiteralValue(
                assign.assignNode.getLastChild(), true)) {
              assignedToUnknownValue = true;
            }
          }

          if (assignedToUnknownValue && hasPropertyAssign) {
            changes = markReferencedVar(var) || changes;
            maybeUnreferenced.remove(current);
            current--;
          }
        }
      }
    } while (changes);
  }

  /**
   * Remove all assigns to a var.
   */
  private void removeAllAssigns(Var var) {
    for (Assign assign : assignsByVar.get(var)) {
      assign.remove();
      compiler.reportCodeChange();
    }
  }

  /**
   * Marks a var as referenced, recursing into any values of this var
   * that we skipped.
   * @return True if this variable had not been referenced before.
   */
  private boolean markReferencedVar(Var var) {
    if (referenced.add(var)) {
      for (Continuation c : continuations.get(var)) {
        c.apply();
      }
      return true;
    }
    return false;
  }

  /**
   * Removes any vars in the scope that were not referenced. Removes any
   * assigments to those variables as well.
   */
  private void removeUnreferencedVars() {
    CodingConvention convention = compiler.getCodingConvention();

    for (Iterator<Var> it = maybeUnreferenced.iterator(); it.hasNext(); ) {
      Var var = it.next();

      // Regardless of what happens to the original declaration,
      // we need to remove all assigns, because they may contain references
      // to other unreferenced variables.
      removeAllAssigns(var);

      compiler.addToDebugLog("Unreferenced var: " + var.name);
      Node nameNode = var.nameNode;
      Node toRemove = nameNode.getParent();
      Node parent = toRemove.getParent();

      Preconditions.checkState(
          toRemove.getType() == Token.VAR ||
          toRemove.getType() == Token.FUNCTION ||
          toRemove.getType() == Token.LP &&
          parent.getType() == Token.FUNCTION,
          "We should only declare vars and functions and function args");

      if (toRemove.getType() == Token.LP &&
          parent.getType() == Token.FUNCTION) {
        // Don't remove function arguments here. That's a special case
        // that's taken care of in removeUnreferencedFunctionArgs.
      } else if (NodeUtil.isFunctionExpression(toRemove)) {
        if (!preserveFunctionExpressionNames) {
          toRemove.getFirstChild().setString("");
          compiler.reportCodeChange();
        }
        // Don't remove bleeding functions.
      } else if (parent != null &&
          parent.getType() == Token.FOR &&
          parent.getChildCount() < 4) {
        // foreach iterations have 3 children. Leave them alone.
      } else if (toRemove.getType() == Token.VAR &&
          nameNode.hasChildren() &&
          NodeUtil.mayHaveSideEffects(nameNode.getFirstChild())) {
        // If this is a single var declaration, we can at least remove the
        // declaration itself and just leave the value, e.g.,
        // var a = foo(); => foo();
        if (toRemove.getChildCount() == 1) {
          parent.replaceChild(toRemove,
              new Node(Token.EXPR_RESULT, nameNode.removeFirstChild()));
          compiler.reportCodeChange();
        }
      } else if (toRemove.getType() == Token.VAR &&
          toRemove.getChildCount() > 1) {
        // For var declarations with multiple names (i.e. var a, b, c),
        // only remove the unreferenced name
        toRemove.removeChild(nameNode);
        compiler.reportCodeChange();
      } else if (parent != null) {
        NodeUtil.removeChild(parent, toRemove);
        compiler.reportCodeChange();
      }
    }
  }

  /**
   * Our progress in a traversal can be expressed completely as the
   * current node and scope. The continuation lets us save that
   * information so that we can continue the traversal later.
   */
  private class Continuation {
    private final Node node;
    private final Scope scope;

    Continuation(Node node, Scope scope) {
      this.node = node;
      this.scope = scope;
    }

    void apply() {
      if (NodeUtil.isFunctionDeclaration(node)) {
        traverseFunction(node, scope);
      } else {
        for (Node child = node.getFirstChild();
             child != null; child = child.getNext()) {
          traverseNode(child, node, scope);
        }
      }
    }
  }

  private static class Assign {

    final Node assignNode;

    final Node nameNode;

    // If false, then this is an assign to the normal variable. Otherwise,
    // this is an assign to a property of that variable.
    final boolean isPropertyAssign;

    // Secondary side effects are any side effects in this assign statement
    // that aren't caused by the assignment operation itself. For example,
    // a().b = 3;
    // a = b();
    // var foo = (a = b);
    // In the first two cases, the sides of the assignment have side-effects.
    // In the last one, the result of the assignment is used, so we
    // are conservative and assume that it may be used in a side-effecting
    // way.
    final boolean mayHaveSecondarySideEffects;

    Assign(Node assignNode, Node nameNode, boolean isPropertyAssign) {
      Preconditions.checkState(NodeUtil.isAssignmentOp(assignNode));
      this.assignNode = assignNode;
      this.nameNode = nameNode;
      this.isPropertyAssign = isPropertyAssign;

      this.mayHaveSecondarySideEffects =
          assignNode.getParent().getType() != Token.EXPR_RESULT ||
          NodeUtil.mayHaveSideEffects(assignNode.getFirstChild()) ||
          NodeUtil.mayHaveSideEffects(assignNode.getLastChild());
    }

    /**
     * If this is an assign to a variable or its property, return it.
     * Otherwise, return null.
     */
    static Assign maybeCreateAssign(Node assignNode) {
      Preconditions.checkState(NodeUtil.isAssignmentOp(assignNode));

      // Skip one level of GETPROPs or GETELEMs.
      //
      // Don't skip more than one level, because then we get into
      // situations where assigns to properties of properties will always
      // trigger side-effects, and the variable they're on cannot be removed.
      boolean isPropAssign = false;
      Node current = assignNode.getFirstChild();
      if (NodeUtil.isGet(current)) {
        current = current.getFirstChild();
        isPropAssign = true;

        if (current.getType() == Token.GETPROP &&
            current.getLastChild().getString().equals("prototype")) {
          // Prototype properties sets should be considered like normal
          // property sets.
          current = current.getFirstChild();
        }
      }

      if (current.getType() == Token.NAME) {
        return new Assign(assignNode, current, isPropAssign);
      }
      return null;
    }

    /**
     * Replace the current assign with its right hand side.
     */
    void remove() {
      Node parent = assignNode.getParent();
      if (mayHaveSecondarySideEffects) {
        Node replacement = assignNode.getLastChild().detachFromParent();

        // Aggregate any expressions in GETELEMs.
        for (Node current = assignNode.getFirstChild();
             current.getType() != Token.NAME;
             current = current.getFirstChild()) {
          if (current.getType() == Token.GETELEM) {
            replacement = new Node(Token.COMMA,
                current.getLastChild().detachFromParent(), replacement);
            replacement.copyInformationFrom(current);
          }
        }

        parent.replaceChild(assignNode, replacement);
      } else {
        Node gramps = parent.getParent();
        if (parent.getType() == Token.EXPR_RESULT) {
          gramps.removeChild(parent);
        } else {
          parent.replaceChild(assignNode,
              assignNode.getLastChild().detachFromParent());
        }
      }
    }
  }
}
