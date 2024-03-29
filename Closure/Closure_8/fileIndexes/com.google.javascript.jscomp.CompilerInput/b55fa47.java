/*
 * Copyright 2009 The Closure Compiler Authors.
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.deps.DependencyInfo;
import com.google.javascript.jscomp.deps.JsFileParser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A class for the internal representation of an input to the compiler.
 * Wraps a {@link SourceAst} and maintain state such as module for the input and
 * whether the input is an extern. Also calculates provided and required types.
 *
 */
public class CompilerInput
    implements SourceAst, DependencyInfo, StaticSourceFile {
  private static final long serialVersionUID = 1L;

  // Info about where the file lives.
  private JSModule module;
  final private String name;

  // The AST.
  private final SourceAst ast;

  // Source Line Information
  private int[] lineOffsets = null;

  // Provided and required symbols.
  private final Set<String> provides = Sets.newHashSet();
  private final Set<String> requires = Sets.newHashSet();
  private boolean generatedDependencyInfoFromSource = false;

  // An error manager for handling problems when dealing with
  // provides/requires.
  private ErrorManager errorManager;

  // An AbstractCompiler for doing parsing.
  private AbstractCompiler compiler;

  public CompilerInput(SourceAst ast) {
    this(ast, ast.getSourceFile().getName(), false);
  }

  public CompilerInput(SourceAst ast, boolean isExtern) {
    this(ast, ast.getSourceFile().getName(), isExtern);
  }

  public CompilerInput(SourceAst ast, String inputName, boolean isExtern) {
    this.ast = ast;
    this.name = inputName;

    // TODO(nicksantos): Add a precondition check here. People are passing
    // in null, but they should not be.
    if (ast != null && ast.getSourceFile() != null) {
      ast.getSourceFile().setIsExtern(isExtern);
    }
  }

  public CompilerInput(JSSourceFile file) {
    this(file, false);
  }

  public CompilerInput(JSSourceFile file, boolean isExtern) {
    this(new JsAst(file), file.getName(), isExtern);
  }

  /** Returns a name for this input. Must be unique across all inputs. */
  @Override
  public String getName() {
    return name;
  }

  /** Gets the path relative to closure-base, if one is available. */
  @Override
  public String getPathRelativeToClosureBase() {
    // TODO(nicksantos): Implement me.
    throw new UnsupportedOperationException();
  }

  @Override
  public Node getAstRoot(AbstractCompiler compiler) {
    return ast.getAstRoot(compiler);
  }

  @Override
  public void clearAst() {
    ast.clearAst();
  }

  @Override
  public SourceFile getSourceFile() {
    return ast.getSourceFile();
  }

  @Override
  public void setSourceFile(SourceFile file) {
    ast.setSourceFile(file);
    lineOffsets = null;
  }

  /** Returns the SourceAst object on which this input is based. */
  public SourceAst getSourceAst() {
    return ast;
  }

  /** Sets an error manager for routing error messages. */
  public void setErrorManager(ErrorManager errorManager) {
    this.errorManager = errorManager;
  }

  /** Sets an abstract compiler for doing parsing. */
  public void setCompiler(AbstractCompiler compiler) {
    this.compiler = compiler;
    setErrorManager(compiler.getErrorManager());
  }

  /** Gets a list of types depended on by this input. */
  @Override
  public Collection<String> getRequires() {
    Preconditions.checkNotNull(errorManager,
        "Expected setErrorManager to be called first");
    try {
      regenerateDependencyInfoIfNecessary();
      return Collections.<String>unmodifiableSet(requires);
    } catch (IOException e) {
      errorManager.report(CheckLevel.ERROR,
          JSError.make(AbstractCompiler.READ_ERROR, getName()));
      return ImmutableList.<String>of();
    }
  }

  /** Gets a list of types provided by this input. */
  @Override
  public Collection<String> getProvides() {
    Preconditions.checkNotNull(errorManager,
        "Expected setErrorManager to be called first");
    try {
      regenerateDependencyInfoIfNecessary();
      return Collections.<String>unmodifiableSet(provides);
    } catch (IOException e) {
      errorManager.report(CheckLevel.ERROR,
          JSError.make(AbstractCompiler.READ_ERROR, getName()));
      return ImmutableList.<String>of();
    }
  }

  /**
   * Regenerates the provides/requires if we need to do so.
   */
  private void regenerateDependencyInfoIfNecessary() throws IOException {
    // If the code is NOT a JsAst, then it was not originally JS code.
    // Look at the Ast for dependency info.
    if (!(ast instanceof JsAst)) {
      Preconditions.checkNotNull(compiler,
          "Expected setCompiler to be called first");
      DepsFinder finder = new DepsFinder();
      Node root = getAstRoot(compiler);
      if (root == null) {
        return;
      }

      finder.visitTree(getAstRoot(compiler));

      // TODO(nicksantos|user): This caching behavior is a bit
      // odd, and only works if you assume the exact call flow that
      // clients are currently using.  In that flow, they call
      // getProvides(), then remove the goog.provide calls from the
      // AST, and then call getProvides() again.
      //
      // This won't work for any other call flow, or any sort of incremental
      // compilation scheme. The API needs to be fixed so callers aren't
      // doing weird things like this, and then we should get rid of the
      // multiple-scan strategy.

      provides.addAll(finder.provides);
      requires.addAll(finder.requires);
    } else {
      // Otherwise, look at the source code.
      if (!generatedDependencyInfoFromSource) {
        // Note: it's ok to use getName() instead of
        // getPathRelativeToClosureBase() here because we're not using
        // this to generate deps files. (We're only using it for
        // symbol dependencies.)
        DependencyInfo info = (new JsFileParser(errorManager)).parseFile(
            getName(), getName(), getCode());

        provides.addAll(info.getProvides());
        requires.addAll(info.getRequires());

        generatedDependencyInfoFromSource = true;
      }
    }
  }

  private static class DepsFinder {
    private final List<String> provides = Lists.newArrayList();
    private final List<String> requires = Lists.newArrayList();
    private final CodingConvention codingConvention =
        new ClosureCodingConvention();

    void visitTree(Node n) {
      visitSubtree(n, null);
    }

    void visitSubtree(Node n, Node parent) {
      if (n.getType() == Token.CALL) {
        String require =
            codingConvention.extractClassNameIfRequire(n, parent);
        if (require != null) {
          requires.add(require);
        }

        String provide =
            codingConvention.extractClassNameIfProvide(n, parent);
        if (provide != null) {
          provides.add(provide);
        }
        return;
      } else if (parent != null &&
          parent.getType() != Token.EXPR_RESULT &&
          parent.getType() != Token.SCRIPT) {
        return;
      }

      for (Node child = n.getFirstChild();
           child != null; child = child.getNext()) {
        visitSubtree(child, n);
      }
    }
  }

  /**
   * Gets the source line for the indicated line number.
   *
   * @param lineNumber the line number, 1 being the first line of the file.
   * @return The line indicated. Does not include the newline at the end
   *     of the file. Returns {@code null} if it does not exist,
   *     or if there was an IO exception.
   */
  public String getLine(int lineNumber) {
    return getSourceFile().getLine(lineNumber);
  }

  /**
   * Get a region around the indicated line number. The exact definition of a
   * region is implementation specific, but it must contain the line indicated
   * by the line number. A region must not start or end by a carriage return.
   *
   * @param lineNumber the line number, 1 being the first line of the file.
   * @return The line indicated. Returns {@code null} if it does not exist,
   *     or if there was an IO exception.
   */
  public Region getRegion(int lineNumber) {
    return getSourceFile().getRegion(lineNumber);
  }

  public String getCode() throws IOException {
    return getSourceFile().getCode();
  }

  /** Returns the module to which the input belongs. */
  public JSModule getModule() {
    return module;
  }

  /** Sets the module to which the input belongs. */
  public void setModule(JSModule module) {
    // An input may only belong to one module.
    Preconditions.checkArgument(
        module == null || this.module == null || this.module == module);
    this.module = module;
  }

  @Override
  public boolean isExtern() {
    if (ast == null || ast.getSourceFile() == null) {
      return false;
    }
    return ast.getSourceFile().isExtern();
  }

  void setIsExtern(boolean isExtern) {
    if (ast == null || ast.getSourceFile() == null) {
      return;
    }
    ast.getSourceFile().setIsExtern(isExtern);
  }

  /**
   * @param lineno the line of the input to get the absolute offset of.
   * @return the absolute offset of the start of the provided line.
   * @throws IllegalArgumentException if lineno is less than 1 or greater than
   *         the number of lines in the source.
   */
  public int getLineOffset(int lineno) {
    if (lineOffsets == null) {
      findLineOffsets();
    }
    if (lineno < 1 || lineno > lineOffsets.length) {
      throw new IllegalArgumentException(
          "Expected line number between 1 and " + lineOffsets.length);
    }
    return lineOffsets[lineno - 1];
  }

  /** @return The number of lines in this input. */
  public int getNumLines() {
    if (lineOffsets == null) {
      findLineOffsets();
    }
    return lineOffsets.length;
  }

  private void findLineOffsets() {
    try {
      String[] sourceLines = ast.getSourceFile().getCode().split("\n");
      lineOffsets = new int[sourceLines.length];
      for (int ii = 1; ii < sourceLines.length; ++ii) {
        lineOffsets[ii] =
            lineOffsets[ii - 1] + sourceLines[ii - 1].length() + 1;
      }
    } catch (IOException e) {
      lineOffsets = new int[1];
      lineOffsets[0] = 0;
    }
  }

}
