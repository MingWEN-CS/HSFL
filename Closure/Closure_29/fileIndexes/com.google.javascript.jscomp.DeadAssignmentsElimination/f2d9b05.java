/*
 * Copyright 2008 Google Inc.
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
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Removes local variable assignments that are useless based on information from
 * {@link LiveVariablesAnalysis}. If there is an assignment to variable
 * {@code x} and {@code x} is dead after this assignment, we know that the
 * current content of {@code x} will not be read and this assignment is useless.
 *
*
 */
class DeadAssignmentsElimination extends AbstractPostOrderCallback implements
    CompilerPass, ScopedCallback {

  private final AbstractCompiler compiler;
  private LiveVariablesAnalysis liveness;

  public DeadAssignmentsElimination(AbstractCompiler compiler) {
    this.compiler = compiler;
  }

  @Override
  public void process(Node externs, Node root) {
    Preconditions.checkNotNull(externs);
    Preconditions.checkNotNull(root);
    NodeTraversal.traverse(compiler, root, this);
  }

  @Override
  public void enterScope(NodeTraversal t) {
    Scope scope = t.getScope();
    // Global scope _SHOULD_ work, however, liveness won't finish without
    // -Xmx1024 in closure. We might have to look at coding conventions for
    // exported variables as well.
    if (scope.isGlobal()) {
      return;
    }

    // We are not going to do any dead assignment elimination in when there is
    // at least one inner function because in most browsers, when there is a
    // closure, ALL the variables are saved (escaped).
    if (!NodeUtil.containsFunctionDeclaration(
        t.getScopeRoot().getLastChild())) {
      // Computes liveness information first.
      ControlFlowGraph<Node> cfg = t.getControlFlowGraph();
      liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
      liveness.analyze();
      tryRemoveDeadAssignments(t, cfg);
    }
  }

  @Override
  public void exitScope(NodeTraversal t) {
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
  }

  /**
   * Try to remove useless assignments from a control flow graph that has been
   * annotated with liveness information.
   *
   * @param t The node traversal.
   * @param cfg The control flow graph of the program annotated with liveness
   *        information.
   */
  private void tryRemoveDeadAssignments(NodeTraversal t,
      ControlFlowGraph<Node> cfg) {
    List<DiGraphNode<Node, Branch>> nodes = cfg.getDirectedGraphNodes();

    for (DiGraphNode<Node, Branch> cfgNode : nodes) {
      FlowState<LiveVariableLattice> state =
          cfgNode.getAnnotation();
      Node n = cfgNode.getValue();
      if (n == null) {
        continue;
      }
      switch (n.getType()) {
        case Token.IF:
        case Token.WHILE:
        case Token.DO:
          tryRemoveAssignment(t, NodeUtil.getConditionExpression(n), state);
          continue;
        case Token.FOR:
          if (!NodeUtil.isForIn(n)) {
            tryRemoveAssignment(
                t, NodeUtil.getConditionExpression(n), state);
          }
          continue;
        case Token.SWITCH:
        case Token.CASE:
        case Token.RETURN:
          if (n.hasChildren()) {
            tryRemoveAssignment(t, n.getFirstChild(), state);
          }
          continue;
        // TODO(user): case Token.VAR: Remove var a=1;a=2;.....
      }

      tryRemoveAssignment(t, n, state);
    }
  }

  private void tryRemoveAssignment(NodeTraversal t, Node n,
      FlowState<LiveVariableLattice> state) {
    tryRemoveAssignment(t, n, n, state);
  }

  /**
   * Determines if any local variables are dead after the instruction {@code n}
   * and are assigned within the subtree of {@code n}. Removes those assignments
   * if there are any.
   *
   * @param n Target instruction.
   * @param exprRoot The CFG node where the liveness information in state is
   *     still correct.
   * @param state The liveness information at {@code n}.
   */
  private void tryRemoveAssignment(NodeTraversal t, Node n, Node exprRoot,
      FlowState<LiveVariableLattice> state) {

    Node parent = n.getParent();

    if (NodeUtil.isAssignmentOp(n) ||
        n.getType() == Token.INC || n.getType() == Token.DEC) {

      Node lhs = n.getFirstChild();
      Node rhs = lhs.getNext();

      // Recurse first. Example: dead_x = dead_y = 1; We try to clean up dead_y
      // first.
      if (rhs != null) {
        tryRemoveAssignment(t, rhs, exprRoot, state);
        rhs = lhs.getNext();
      }

      Scope scope = t.getScope();
      if (!NodeUtil.isName(lhs)) {
        return; // Not a local variable assignment.
      }
      String name = lhs.getString();
      if (!scope.isDeclared(name, false)) {
        return;
      }
      Var var = scope.getVar(name);
      if (liveness.getEscapedLocals().contains(var)) {
        return; // Local variable that might be escaped due to closures.
      }
      if (state.getOut().isLive(var)) {
        return; // Variable not dead.
      }
      if (state.getIn().isLive(var) &&
          isVariableStillLiveWithinExpression(n, exprRoot, var.name)) {
        // The variable is killed here but it is also live before it.
        // This is possible if we have say:
        //    if (X = a && a = C) {..} ; .......; a = S;
        // In this case we are safe to remove "a = C" because it is dead.
        // However if we have:
        //    if (a = C && X = a) {..} ; .......; a = S;
        // removing "a = C" is NOT correct, although the live set at the node
        // is exactly the same.
        // TODO(user): We need more fine grain CFA or we need to keep track
        // of GEN sets when we recurse here.
        return;
      }

      if (NodeUtil.isAssign(n)) {
        n.removeChild(rhs);
        n.getParent().replaceChild(n, rhs);
      } else if (NodeUtil.isAssignmentOp(n)) {
        n.removeChild(rhs);
        n.removeChild(lhs);
        Node op = new Node(NodeUtil.getOpFromAssignmentOp(n), lhs, rhs);
        parent.replaceChild(n, op);
      } else if (n.getType() == Token.INC || n.getType() == Token.DEC) {
        if (NodeUtil.isExpressionNode(parent)) {
          parent.replaceChild(n, new Node(Token.VOID, Node.newNumber(0)));
        } else if(n.getType() == Token.COMMA && n != parent.getLastChild()) {
          parent.removeChild(n);
        } else if (parent.getType() == Token.FOR && !NodeUtil.isForIn(parent) &&
            NodeUtil.getConditionExpression(parent) != n) {
          parent.replaceChild(n, new Node(Token.EMPTY));
        } else {
          // Cannot replace x = a++ with x = a because that's not valid
          // when a is not a number.
          return;
        }
      } else {
        // Not reachable.
        Preconditions.checkState(false, "Unknown statement");
      }

      compiler.reportCodeChange();
      return;

    } else {
      for (Node c = n.getFirstChild(); c != null;) {
        Node next = c.getNext();
        if (!ControlFlowGraph.isEnteringNewCfgNode(c)) {
          tryRemoveAssignment(t, c, exprRoot, state);
        }
        c = next;
      }
      return;
    }
  }

  /**
   * Given a variable, node n in the tree and a sub-tree denoted by exprRoot as
   * the root, this function returns true if there exists a read of that
   * variable before a write to that variable that is on the right side of n.
   *
   * For example, suppose the node is x = 1:
   *
   * y = 1, x = 1; // false, there is no reads at all.
   * y = 1, x = 1, print(x) // true, there is a read right of n.
   * y = 1, x = 1, x = 2, print(x) // false, there is a read right of n but
   *                               // it is after a write.
   *
   * @param n The current node we should look at.
   * @param exprRoot The node
   */
  private boolean isVariableStillLiveWithinExpression(
      Node n, Node exprRoot, String variable) {
    while (n != exprRoot) {
      for(Node sibling = n.getNext(); sibling != null;
          sibling = sibling.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(sibling)) {
          VariableLiveness state = readVariableBeforeKilling(sibling, variable);

          // If we see a READ or KILL there is no need to continue.
          if (state == VariableLiveness.READ) {
            return true;
          } else if (state == VariableLiveness.KILL) {
            return false;
          }
        }
      }
      n = n.getParent();
    }
    return false;
  }

  // The current liveness of the variable
  private enum VariableLiveness {
    MAYBE_LIVE, // May be still live in the current expression tree.
    READ, // Known there is a read left of it.
    KILL, // Known there is a write before any read.
  }

  /**
   * Give an expression and a variable. It returns READ, if the right-most
   * reference of that variable is a read. It returns KILL, if the right-most
   * reference of that variable is an assignment. It returns MAY_LIVE otherwise.
   *
   * This need to be a pre-order traversal so we cannot use the normal node
   * traversals.
   */
  private VariableLiveness readVariableBeforeKilling(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
      if (NodeUtil.isLhs(n, n.getParent())) {
        return VariableLiveness.KILL;
      } else {
        return VariableLiveness.READ;
      }
    }
    for (Node child = n.getFirstChild();
        child != null; child = child.getNext()) {
      if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
        VariableLiveness state = readVariableBeforeKilling(child, variable);
        if (state != VariableLiveness.MAYBE_LIVE) {
          return state;
        }
      }
    }
    return VariableLiveness.MAYBE_LIVE;
  }
}
