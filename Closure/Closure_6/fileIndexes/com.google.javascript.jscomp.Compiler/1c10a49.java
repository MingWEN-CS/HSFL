/*
 * Copyright 2004 Google Inc.
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.DevMode;
import com.google.javascript.jscomp.CompilerOptions.TracerMode;
import com.google.javascript.jscomp.deps.SortedDependencies;
import com.google.javascript.jscomp.mozilla.rhino.ErrorReporter;
import com.google.javascript.jscomp.parsing.Config;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Compiler (and the other classes in this package) does the following:
 * <ul>
 * <li>parses JS code
 * <li>checks for undefined variables
 * <li>performs optimizations such as constant folding and constants inlining
 * <li>renames variables (to short names)
 * <li>outputs compact javascript code
 * </ul>
 *
 * External variables are declared in 'externs' files. For instance, the file
 * may include definitions for global javascript/browser objects such as
 * window, document.
 *
*
*
 */
public class Compiler extends AbstractCompiler {
  CompilerOptions options = null;

  private PassConfig passes = null;

  // The externs inputs
  private List<CompilerInput> externs;

  // The JS source modules
  private List<JSModule> modules;

  // The graph of the JS source modules
  private JSModuleGraph moduleGraph;

  // The JS source inputs
  private List<CompilerInput> inputs;

  // error manager to which error management is delegated
  private ErrorManager errorManager;

  // Cached data structures.
  private SymbolTable symbolTable = null;

  // Parse tree root nodes
  Node externsRoot;
  Node jsRoot;
  Node externAndJsRoot;

  private Map<String, CompilerInput> inputsByName;

  /** The source code map */
  private SourceMap sourceMap;

  /** The externs created from the exports.  */
  private String externExports = null;

  /**
   * Ids for function inlining so that each declared name remains
   * unique.
   */
  private int uniqueNameId = 0;

  /**
   * Whether the optional "normalization" pass has been run.  Passes that
   * depend on the assumptions made there should check this value.
   */
  private boolean normalized = false;

  /** Whether to use threads. */
  private boolean useThreads = true;

  /**
   * Whether to assume there are references to the RegExp Global object
   * properties.
   */
  private boolean hasRegExpGlobalReferences = true;

  /** The function information map */
  private FunctionInformationMap functionInformationMap;

  /** Debugging information */
  private final StringBuilder debugLog = new StringBuilder();

  /** Detects Google-specific coding conventions. */
  CodingConvention defaultCodingConvention = new GoogleCodingConvention();

  private JSTypeRegistry typeRegistry;
  private Config parserConfig = null;

  private ReverseAbstractInterpreter abstractInterpreter;
  private TypeValidator typeValidator;

  public PerformanceTracker tracker;

  // The oldErrorReporter exists so we can get errors from the JSTypeRegistry.
  private final com.google.javascript.rhino.ErrorReporter oldErrorReporter =
      RhinoErrorReporter.forOldRhino(this);

  // This error reporter gets the messages from the current Rhino parser.
  private final ErrorReporter defaultErrorReporter =
      RhinoErrorReporter.forNewRhino(this);

  /** Error strings used for reporting JSErrors */
  public static final DiagnosticType OPTIMIZE_LOOP_ERROR = DiagnosticType.error(
      "JSC_OPTIMIZE_LOOP_ERROR",
      "Exceeded max number of optimization iterations: {0}");
  public static final DiagnosticType MOTION_ITERATIONS_ERROR =
      DiagnosticType.error("JSC_OPTIMIZE_LOOP_ERROR",
          "Exceeded max number of code motion iterations: {0}");

  private static final long COMPILER_STACK_SIZE = 1048576L;


  /**
   * Logger for the whole com.google.javascript.jscomp domain -
   * setting configuration for this logger affects all loggers
   *  in other classes within the compiler.
   */
  private static final Logger logger =
      Logger.getLogger("com.google.javascript.jscomp");

  private final PrintStream outStream;

  /**
   * Creates a Compiler that reports errors and warnings to its logger.
   */
  public Compiler() {
    this((PrintStream) null);
  }

  /**
   * Creates n Compiler that reports errors and warnings to an output
   * stream.
   */
  public Compiler(PrintStream stream) {
    addChangeHandler(recentChange);
    outStream = stream;
  }

  /**
   * Creates a Compiler that uses a custom error manager.
   */
  public Compiler(ErrorManager errorManager) {
    this();
    setErrorManager(errorManager);
  }

  /**
   * Acquires the symbol table.
   */
  @Override
  SymbolTable acquireSymbolTable() {
    if (symbolTable == null) {
      symbolTable = new SymbolTable(this);
    }
    symbolTable.acquire();
    return symbolTable;
  }

  /**
   * Sets the error manager.
   *
   * @param errorManager the error manager, it cannot be {@code null}
   */
  public void setErrorManager(ErrorManager errorManager) {
    Preconditions.checkNotNull(
        errorManager, "the error manager cannot be null");
    this.errorManager = errorManager;
  }

  /**
   * Creates a message formatter instance corresponding to the value of
   * {@link CompilerOptions}.
   */
  private MessageFormatter createMessageFormatter() {
    boolean colorize = options.shouldColorizeErrorOutput();
    return options.errorFormat.toFormatter(this, colorize);
  }

  /**
   * Initialize the compiler options. Only necessary if you're not doing
   * a normal compile() job.
   */
  public void initOptions(CompilerOptions options) {
    this.options = options;
    if (errorManager == null) {
      if (outStream == null) {
        setErrorManager(
            new LoggerErrorManager(createMessageFormatter(), logger));
      } else {
        PrintStreamErrorManager printer =
            new PrintStreamErrorManager(createMessageFormatter(), outStream);
        printer.setSummaryDetailLevel(options.summaryDetailLevel);
        setErrorManager(printer);
      }
    }
  }

  /**
   * Initializes the instance state needed for a compile job.
   */
  public void init(JSSourceFile[] externs, JSSourceFile[] inputs,
      CompilerOptions options) {
    init(Lists.<JSSourceFile>newArrayList(externs),
        Lists.<JSSourceFile>newArrayList(inputs), options);
  }

  /**
   * Initializes the instance state needed for a compile job.
   */
  public void init(List<JSSourceFile> externs, List<JSSourceFile> inputs,
      CompilerOptions options) {
    initOptions(options);

    this.externs = makeCompilerInput(externs, true);
    this.modules = null;
    this.moduleGraph = null;
    this.inputs = makeCompilerInput(inputs, false);
    initBasedOnOptions();

    initInputsByNameMap();
  }

  static final DiagnosticType MODULE_DEPENDENCY_ERROR =
      DiagnosticType.error("JSC_MODULE_DEPENDENCY_ERROR",
          "Bad dependency: {0} -> {1}. "
              + "Modules must be listed in dependency order.");

  /**
   * Initializes the instance state needed for a compile job if the sources
   * are in modules.
   */
  public void init(JSSourceFile[] externs, JSModule[] modules,
      CompilerOptions options) {
    initModules(Lists.<JSSourceFile>newArrayList(externs),
         Lists.<JSModule>newArrayList(modules), options);
  }

  /**
   * Initializes the instance state needed for a compile job if the sources
   * are in modules.
   */
  public void initModules(
      List<JSSourceFile> externs, List<JSModule> modules,
      CompilerOptions options) {
    initOptions(options);

    checkFirstModule(modules);
    fillEmptyModules(modules);

    this.externs = makeCompilerInput(externs, true);
    this.modules = modules;
    // Generate the module graph, and report any errors in the module
    // specification as errors.
    try {
      this.moduleGraph = new JSModuleGraph(modules);
    } catch (JSModuleGraph.ModuleDependenceException e) {
      // problems with the module format.  Report as an error.  The
      // message gives all details.
      report(JSError.make(MODULE_DEPENDENCY_ERROR,
          e.getModule().getName(), e.getDependentModule().getName()));
      return;
    }
    this.inputs = getAllInputsFromModules();
    initBasedOnOptions();

    initInputsByNameMap();
  }

  /**
   * Do any initialization that is dependent on the compiler options.
   */
  private void initBasedOnOptions() {
    // Create the source map if necessary.
    if (options.sourceMapOutputPath != null) {
      sourceMap = new SourceMap();
    }
  }

  private List<CompilerInput> makeCompilerInput(
      List<JSSourceFile> files, boolean isExtern) {
    List<CompilerInput> inputs = Lists.newArrayList();
    for (JSSourceFile file : files) {
      inputs.add(new CompilerInput(file, isExtern));
    }
    return inputs;
  }

  private static final DiagnosticType EMPTY_MODULE_LIST_ERROR =
      DiagnosticType.error("JSC_EMPTY_MODULE_LIST_ERROR",
          "At least one module must be provided");

  private static final DiagnosticType EMPTY_ROOT_MODULE_ERROR =
      DiagnosticType.error("JSC_EMPTY_ROOT_MODULE_ERROR",
          "Root module '{0}' must contain at least one source code input");

  /**
   * Verifies that at least one module has been provided and that the first one
   * has at least one source code input.
   */
  private void checkFirstModule(List<JSModule> modules) {
    if (modules.isEmpty()) {
      report(JSError.make(EMPTY_MODULE_LIST_ERROR));
    } else if (modules.get(0).getInputs().isEmpty()) {
      report(JSError.make(EMPTY_ROOT_MODULE_ERROR,
          modules.get(0).getName()));
    }
  }

  /**
   * Fill any empty modules with a place holder file. It makes any cross module
   * motion easier.
   */
  private void fillEmptyModules(List<JSModule> modules) {
    for (JSModule module : modules) {
      if (module.getInputs().isEmpty()) {
        module.add(JSSourceFile.fromCode("[" + module.getName() + "]", ""));
      }
    }
  }

  static final DiagnosticType DUPLICATE_INPUT_IN_MODULES =
      DiagnosticType.error("JSC_DUPLICATE_INPUT_IN_MODULES_ERROR",
          "Two modules cannot contain the same input, but module {0} and {1} "
              + "both include \"{2}\"");

  /**
   * Rebuilds the internal list of inputs by iterating over all modules.
   * This is necessary if inputs have been added to or removed from a module
   * after the {@link #init(JSSourceFile[], JSModule[], CompilerOptions)} call.
   */
  public void rebuildInputsFromModules() {
    inputs = getAllInputsFromModules();
    initInputsByNameMap();
  }

  /**
   * Builds a single list of all module inputs. Verifies that it contains no
   * duplicates.
   */
  private List<CompilerInput> getAllInputsFromModules() {
    List<CompilerInput> inputs = Lists.newArrayList();
    Map<String, JSModule> inputMap = Maps.newHashMap();
    for (JSModule module : modules) {
      for (CompilerInput input : module.getInputs()) {
        String inputName = input.getName();
        JSModule firstModule = inputMap.get(inputName);
        if (firstModule == null) {
          inputs.add(input);
          inputMap.put(inputName, module);
        } else {
          report(JSError.make(DUPLICATE_INPUT_IN_MODULES,
              firstModule.getName(), module.getName(), inputName));
        }
      }
    }
    if (hasErrors()) {

      // There's no reason to bother parsing the code.
      return ImmutableList.of();
    }

    return inputs;
  }

  static final DiagnosticType DUPLICATE_INPUT =
      DiagnosticType.error("JSC_DUPLICATE_INPUT", "Duplicate input: {0}");
  static final DiagnosticType DUPLICATE_EXTERN_INPUT =
      DiagnosticType.error("JSC_DUPLICATE_EXTERN_INPUT",
          "Duplicate extern input: {0}");

  /**
   * Creates a map to make looking up an input by name fast. Also checks for
   * duplicate inputs.
   */
  void initInputsByNameMap() {
    inputsByName = new HashMap<String, CompilerInput>();
    for (CompilerInput input : externs) {
      String name = input.getName();
      if (!inputsByName.containsKey(name)) {
        inputsByName.put(name, input);
      } else {
        report(JSError.make(DUPLICATE_EXTERN_INPUT, name));
      }
    }
    for (CompilerInput input : inputs) {
      String name = input.getName();
      if (!inputsByName.containsKey(name)) {
        inputsByName.put(name, input);
      } else {
        report(JSError.make(DUPLICATE_INPUT, name));
      }
    }
  }

  public Result compile(
      JSSourceFile extern, JSSourceFile input, CompilerOptions options) {
     return compile(extern, new JSSourceFile[] { input }, options);
  }

  public Result compile(
      JSSourceFile extern, JSSourceFile[] input, CompilerOptions options) {
     return compile(new JSSourceFile[] { extern }, input, options);
  }

  public Result compile(
      JSSourceFile extern, JSModule[] modules, CompilerOptions options) {
     return compile(new JSSourceFile[] { extern }, modules, options);
  }

  /**
   * Compiles a list of inputs.
   */
  public Result compile(JSSourceFile[] externs,
                        JSSourceFile[] inputs,
                        CompilerOptions options) {
    return compile(Lists.<JSSourceFile>newArrayList(externs),
        Lists.<JSSourceFile>newArrayList(inputs),
        options);
  }

  /**
   * Compiles a list of inputs.
   */
  public Result compile(List<JSSourceFile> externs,
      List<JSSourceFile> inputs, CompilerOptions options) {
    // The compile method should only be called once.
    Preconditions.checkState(jsRoot == null);

    try {
      init(externs, inputs, options);
      if (hasErrors()) {
        return getResult();
      }
      return compile();
    } finally {
      Tracer t = newTracer("generateReport");
      errorManager.generateReport();
      stopTracer(t, "generateReport");
    }
  }

  /**
   * Compiles a list of modules.
   */
  public Result compile(JSSourceFile[] externs,
                        JSModule[] modules,
                        CompilerOptions options) {
    return compileModules(Lists.<JSSourceFile>newArrayList(externs),
        Lists.<JSModule>newArrayList(modules),
        options);
  }

  /**
   * Compiles a list of modules.
   */
  public Result compileModules(List<JSSourceFile> externs,
      List<JSModule> modules, CompilerOptions options) {
    // The compile method should only be called once.
    Preconditions.checkState(jsRoot == null);

    try {
      initModules(externs, modules, options);
      if (hasErrors()) {
        return getResult();
      }
      return compile();
    } finally {
      Tracer t = newTracer("generateReport");
      errorManager.generateReport();
      stopTracer(t, "generateReport");
    }
  }

  private Result compile() {
    return runInCompilerThread(new Callable<Result>() {
      public Result call() throws Exception {
        compileInternal();
        return getResult();
      }
    });
  }

  /**
   * Disable threads. This is for clients that run on AppEngine and
   * don't have threads.
   */
  public void disableThreads() {
    useThreads = false;
  }

  @SuppressWarnings("unchecked")
  private <T> T runInCompilerThread(final Callable<T> callable) {

    // Under JRE 1.6, the jscompiler overflows the stack when running on some
    // large or complex js code. Here we start a new thread with a larger
    // stack in order to let the compiler do its thing, without having to
    // increase the stack size for *every* thread (which is what -Xss does).
    // Might want to add thread pool support for clients that compile a lot.

    final boolean dumpTraceReport = options.tracer.isOn();
    final Object[] result = new Object[1];
    final Throwable[] exception = new Throwable[1];
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          if (dumpTraceReport) {
            Tracer.initCurrentThreadTrace();
          }
          result[0] = callable.call();
        } catch (Throwable e) {
          exception[0] = e;
        } finally {
          if (dumpTraceReport) {
            Tracer.logAndClearCurrentThreadTrace();
          }
        }
      }
    };

    if (useThreads) {
      Thread th = new Thread(null, runnable, "jscompiler", COMPILER_STACK_SIZE);
      th.start();
      while (true) {
        try {
          th.join();
          break;
        } catch (InterruptedException ignore) {
          // ignore
        }
      }
    } else {
      runnable.run();
    }

    // Pass on any exception caught by the runnable object.
    if (exception[0] != null) {
      throw new RuntimeException(exception[0]);
    }

    return (T) result[0];
  }

  private void compileInternal() {
    parse();
    if (hasErrors()) {
      return;
    }

    if (!precheck()) {
      return;
    }

    if (options.nameAnonymousFunctionsOnly) {
      // TODO(nicksantos): Move this into an instrument() phase maybe?
      check();
      return;
    }

    if (!options.skipAllPasses) {
      check();
      if (hasErrors()) {
        return;
      }

      if (options.externExportsPath != null) {
        externExports();
      }

      // IDE-mode is defined to stop here, before the heavy rewriting begins.
      if (!options.ideMode) {
        optimize();
      }
    }

    if (options.recordFunctionInformation) {
      recordFunctionInformation();
    }

    if (options.devMode == DevMode.START_AND_END) {
      runSanityCheck();
    }
  }

  public void parse() {
    parseInputs();
  }

  PassConfig getPassConfig() {
    if (passes == null) {
      passes = createPassConfigInternal();
    }
    return passes;
  }

  /**
   * Create the passes object. Clients should use setPassConfig instead of
   * overriding this.
   */
  PassConfig createPassConfigInternal() {
    return new DefaultPassConfig(options);
  }

  /**
   * @param passes The PassConfig to use with this Compiler.
   * @throws NullPointerException if passes is null
   * @throws IllegalStateException if this.passes has already been assigned
   */
  public void setPassConfig(PassConfig passes) {
    // Important to check for null because if setPassConfig(null) is
    // called before this.passes is set, getPassConfig() will create a
    // new PassConfig object and use that, which is probably not what
    // the client wanted since he or she probably meant to use their
    // own PassConfig object.
    Preconditions.checkNotNull(passes);

    if (this.passes != null) {
      throw new IllegalStateException("this.passes has already been assigned");
    }
    this.passes = passes;
  }

  /**
   * Carry out any special checks or procedures that need to be done before
   * proceeding with rest of the compilation process.
   *
   * @return true, to continue with compilation
   */
  boolean precheck() {
    return true;
  }

  public void check() {
    runCustomPasses(CustomPassExecutionTime.BEFORE_CHECKS);

    PhaseOptimizer phaseOptimizer = new PhaseOptimizer(this, tracker);
    if (options.devMode == DevMode.EVERY_PASS) {
      phaseOptimizer.setSanityCheck(sanityCheck);
    }
    phaseOptimizer.consume(getPassConfig().getChecks());
    phaseOptimizer.process(externsRoot, jsRoot);
    if (hasErrors()) {
      return;
    }

    // TODO(nicksantos): clean this up. The flow here is too hard to follow.
    if (options.nameAnonymousFunctionsOnly) {
      return;
    }

    if (options.removeTryCatchFinally) {
      removeTryCatchFinally();
    }

    if (!options.stripTypes.isEmpty() ||
        !options.stripNameSuffixes.isEmpty() ||
        !options.stripTypePrefixes.isEmpty() ||
        !options.stripNamePrefixes.isEmpty()) {
      stripCode(options.stripTypes, options.stripNameSuffixes,
          options.stripTypePrefixes, options.stripNamePrefixes);
    }

    runCustomPasses(CustomPassExecutionTime.BEFORE_OPTIMIZATIONS);
  }

  private void externExports() {
    logger.info("Creating extern file for exports");
    startPass("externExports");

    ExternExportsPass pass = new ExternExportsPass(this);
    process(pass);

    externExports = pass.getGeneratedExterns();

    endPass();
  }

  void process(CompilerPass p) {
    p.process(externsRoot, jsRoot);
  }

  private final PassFactory sanityCheck =
      new PassFactory("sanityCheck", false) {
    @Override
    protected CompilerPass createInternal(AbstractCompiler compiler) {
      return new SanityCheck(compiler);
    }
  };

  private void maybeSanityCheck() {
    if (options.devMode == DevMode.EVERY_PASS) {
      runSanityCheck();
    }
  }

  private void runSanityCheck() {
    sanityCheck.create(this).process(externsRoot, jsRoot);
  }

  /**
   * Removes try/catch/finally statements for easier debugging.
   */
  void removeTryCatchFinally() {
    logger.info("Remove try/catch/finally");
    startPass("removeTryCatchFinally");
    RemoveTryCatch r = new RemoveTryCatch(this);
    process(r);
    endPass();
  }

  /**
   * Strips code for smaller compiled code. This is useful for removing debug
   * statements to prevent leaking them publicly.
   */
  void stripCode(Set<String> stripTypes, Set<String> stripNameSuffixes,
      Set<String> stripTypePrefixes, Set<String> stripNamePrefixes) {
    logger.info("Strip code");
    startPass("stripCode");
    StripCode r = new StripCode(this, stripTypes, stripNameSuffixes,
        stripTypePrefixes, stripNamePrefixes);
    process(r);
    endPass();
  }

  /**
   * Runs custom passes that are designated to run at a particular time.
   */
  private void runCustomPasses(CustomPassExecutionTime executionTime) {
    if (options.customPasses != null) {
      Tracer t = newTracer("runCustomPasses");
      try {
        for (CompilerPass p : options.customPasses.get(executionTime)) {
          process(p);
        }
      } finally {
        stopTracer(t, "runCustomPasses");
      }
    }
  }

  private Tracer currentTracer = null;
  private String currentPassName = null;

  /**
   * Marks the beginning of a pass.
   */
  void startPass(String passName) {
    Preconditions.checkState(currentTracer == null);
    currentPassName = passName;
    currentTracer = newTracer(passName);
  }

  /**
   * Marks the end of a pass.
   */
  void endPass() {
    Preconditions.checkState(currentTracer != null,
        "Tracer should not be null at the end of a pass.");
    stopTracer(currentTracer, currentPassName);
    String passToCheck = currentPassName;
    currentPassName = null;
    currentTracer = null;

    maybeSanityCheck();
  }

  /**
   * Returns a new tracer for the given pass name.
   */
  Tracer newTracer(String passName) {
    String comment = passName
        + (recentChange.hasCodeChanged() ? " on recently changed AST" : "");
    if (options.tracer.isOn()) {
      tracker.recordPassStart(passName);
    }
    return new Tracer("Compiler", comment);
  }

  void stopTracer(Tracer t, String passName) {
    long result = t.stop();
    if (options.tracer.isOn()) {
      tracker.recordPassStop(passName, result);
    }
  }

  /**
   * Returns the result of the compilation.
   */
  public Result getResult() {
    PassConfig.State state = getPassConfig().getIntermediateState();
    return new Result(getErrors(), getWarnings(), debugLog.toString(),
        state.variableMap, state.propertyMap,
        state.anonymousFunctionNameMap, state.stringMap, functionInformationMap,
        sourceMap, externExports, state.cssNames);
  }

  /**
   * Returns an array constructed from errors + temporary warnings.
   */
  public JSError[] getMessages() {
    return getErrors();
  }

  /**
   * Returns the array of errors (never null).
   */
  public JSError[] getErrors() {
    return errorManager.getErrors();
  }

  /**
   * Returns the array of warnings (never null).
   */
  public JSError[] getWarnings() {
    return errorManager.getWarnings();
  }

  /**
   * Returns the root node of the AST, which includes both externs and source.
   */
  public Node getRoot() {
    return externAndJsRoot;
  }

  /**
   * Creates a new id for making unique names.
   */
  private int nextUniqueNameId() {
    return uniqueNameId++;
  }

  /**
   * Resets the unique name id counter
   */
  @VisibleForTesting
  void resetUniqueNameId() {
    uniqueNameId = 0;
  }

  @Override
  Supplier<String> getUniqueNameIdSupplier() {
    final Compiler self = this;
    return new Supplier<String>() {
      public String get() {
        return String.valueOf(self.nextUniqueNameId());
      }
    };
  }

  /**
   * Set if the normalization pass has been done.
   * Note: non-private to enable test cases that require the Normalize pass.
   */
  @Override
  void setNormalized() {
    normalized = true;
  }

  /**
   * Set once unnormalizing passes have been start.
   * Note: non-private to enable test cases that require the Normalize pass.
   */
  @Override
  void setUnnormalized() {
    normalized = false;
  }

  @Override
  boolean isNormalized() {
    return normalized;
  }

  @Override
  boolean areNodesEqualForInlining(Node n1, Node n2) {
    if (options.ambiguateProperties ||
        options.disambiguateProperties) {
      // The type based optimizations require that type information is preserved
      // during other optimizations.
      return n1.checkTreeTypeAwareEqualsSilent(n2);
    } else {
      return n1.checkTreeEqualsSilent(n2);
    }
  }

  //------------------------------------------------------------------------
  // Inputs
  //------------------------------------------------------------------------

  // TODO(nicksantos): Decide which parts of these belong in an AbstractCompiler
  // interface, and which ones should always be injected.

  @Override
  public CompilerInput getInput(String name) {
    return inputsByName.get(name);
  }

  @Override
  public CompilerInput newExternInput(String name) {
    if (inputsByName.containsKey(name)) {
      throw new IllegalArgumentException("Conflicting externs name: " + name);
    }
    SourceAst ast = new SyntheticAst(name);
    CompilerInput input = new CompilerInput(ast, name, true);
    inputsByName.put(name, input);
    externsRoot.addChildToFront(ast.getAstRoot(this));
    return input;
  }

  /** Add a source input dynamically. Intended for incremental compilation. */
  void addIncrementalSourceAst(JsAst ast) {
    String sourceName = ast.getSourceFile().getName();
    Preconditions.checkState(
        getInput(sourceName) == null,
        "Duplicate input of name " + sourceName);
    inputsByName.put(sourceName, new CompilerInput(ast));
  }

  @Override
  JSModuleGraph getModuleGraph() {
    return moduleGraph;
  }

  @Override
  public JSTypeRegistry getTypeRegistry() {
    if (typeRegistry == null) {
      typeRegistry = new JSTypeRegistry(oldErrorReporter, options.looseTypes);
    }
    return typeRegistry;
  }

  @Override
  ScopeCreator getScopeCreator() {
    return getPassConfig().getScopeCreator();
  }

  @Override
  public Scope getTopScope() {
    return getPassConfig().getTopScope();
  }

  @Override
  public ReverseAbstractInterpreter getReverseAbstractInterpreter() {
    if (abstractInterpreter == null) {
      ChainableReverseAbstractInterpreter interpreter =
          new SemanticReverseAbstractInterpreter(
              getCodingConvention(), getTypeRegistry());
      if (options.closurePass) {
        interpreter = new ClosureReverseAbstractInterpreter(
            getCodingConvention(), getTypeRegistry())
            .append(interpreter).getFirst();
      }
      abstractInterpreter = interpreter;
    }
    return abstractInterpreter;
  }

  @Override
  TypeValidator getTypeValidator() {
    if (typeValidator == null) {
      typeValidator = new TypeValidator(this);
    }
    return typeValidator;
  }

  //------------------------------------------------------------------------
  // Parsing
  //------------------------------------------------------------------------

  /**
   * Parses the externs and main inputs.
   *
   * @return A synthetic root node whose two children are the externs root
   *     and the main root
   */
  Node parseInputs() {
    boolean devMode = options.devMode != DevMode.OFF;

    // If old roots exist (we are parsing a second time), detach each of the
    // individual file parse trees.
    if (externsRoot != null) {
      externsRoot.detachChildren();
    }
    if (jsRoot != null) {
      jsRoot.detachChildren();
    }

    // Parse main js sources.
    jsRoot = new Node(Token.BLOCK);
    jsRoot.setIsSyntheticBlock(true);

    if (options.tracer.isOn()) {
      tracker = new PerformanceTracker(jsRoot,
          options.tracer == TracerMode.ALL);
      addChangeHandler(tracker.getCodeChangeHandler());
    }

    Tracer tracer = newTracer("parseInputs");

    try {
      // Parse externs sources.
      externsRoot = new Node(Token.BLOCK);
      externsRoot.setIsSyntheticBlock(true);
      for (CompilerInput input : externs) {
        Node n = input.getAstRoot(this);
        if (hasErrors()) {
          return null;
        }
        externsRoot.addChildToBack(n);
      }

      if (options.manageClosureDependencies) {
        for (CompilerInput input : inputs) {
          input.setCompiler(this);
        }

        SortedDependencies<CompilerInput> sorter =
            new SortedDependencies<CompilerInput>(inputs);
        inputs = Lists.newArrayList(
            sorter.getSortedDependenciesOf(
                sorter.getInputsWithoutProvides()));
      }

      List<CompilerInput> annotatedExterns = Lists.newArrayList();
      Iterator<CompilerInput> inputIterator = inputs.iterator();
      while (inputIterator.hasNext()) {
        CompilerInput input = inputIterator.next();
        Node n = input.getAstRoot(this);
        if (hasErrors()) {
          return null;
        }

        // Inputs can have a null AST during initial parse.
        if (n == null) {
          continue;
        }

        if (n.getJSDocInfo() != null && n.getJSDocInfo().isExterns()) {
          // If the input file is explicitly marked as an externs file, then
          // assume the programmer made a mistake and throw it into
          // the externs pile anyways.
          externsRoot.addChildToBack(n);
          input.setIsExtern(true);
          inputIterator.remove();
          externs.add(input);
          continue;
        }

        if (devMode) {
          runSanityCheck();
          if (hasErrors()) {
            return null;
          }
        }

        if (options.sourceMapOutputPath != null ||
            options.nameReferenceReportPath != null) {

          // Annotate the nodes in the tree with information from the
          // input file. This information is used to construct the SourceMap.
          SourceInformationAnnotator sia =
              new SourceInformationAnnotator(input.getName());
          NodeTraversal.traverse(this, n, sia);
        }

        jsRoot.addChildToBack(n);
      }

      externAndJsRoot = new Node(Token.BLOCK, externsRoot, jsRoot);
      externAndJsRoot.setIsSyntheticBlock(true);

      return externAndJsRoot;
    } finally {
      stopTracer(tracer, "parseInputs");
    }
  }

  public Node parse(JSSourceFile file) {
    initCompilerOptionsIfTesting();
    addToDebugLog("Parsing: " + file.getName());
    return new JsAst(file).getAstRoot(this);
  }

  @Override
  Node parseSyntheticCode(String js) {
    CompilerInput input = new CompilerInput(
        JSSourceFile.fromCode(" [synthetic] ", js));
    inputsByName.put(input.getName(), input);
    return input.getAstRoot(this);
  }

  void initCompilerOptionsIfTesting() {
    if (options == null) {
      // initialization for tests that don't initialize the compiler
      // by the normal mechanisms.
      initOptions(new CompilerOptions());
    }
  }

  @Override
  Node parseSyntheticCode(String fileName, String js) {
    initCompilerOptionsIfTesting();
    return parse(JSSourceFile.fromCode(fileName, js));
  }

  Node parseTestCode(String js) {
    initCompilerOptionsIfTesting();
    CompilerInput input = new CompilerInput(
        JSSourceFile.fromCode(" [testcode] ", js));
    if (inputsByName == null) {
      inputsByName = Maps.newHashMap();
    }
    inputsByName.put(input.getName(), input);
    return input.getAstRoot(this);
  }

  @Override
  ErrorReporter getDefaultErrorReporter() {
    return defaultErrorReporter;
  }

  //------------------------------------------------------------------------
  // Convert back to source code
  //------------------------------------------------------------------------

  /**
   * Converts the main parse tree back to js code.
   */
  public String toSource() {
    return runInCompilerThread(new Callable<String>() {
      public String call() throws Exception {
        Tracer tracer = newTracer("toSource");
        try {
          CodeBuilder cb = new CodeBuilder();
          if (jsRoot != null) {
            int i = 0;
            for (Node scriptNode = jsRoot.getFirstChild();
                 scriptNode != null;
                 scriptNode = scriptNode.getNext()) {
              toSource(cb, i++, scriptNode);
            }
          }
          return cb.toString();
        } finally {
          stopTracer(tracer, "toSource");
        }
      }
    });
  }

  /**
   * Converts the parse tree for each input back to js code.
   */
  public String[] toSourceArray() {
    return runInCompilerThread(new Callable<String[]>() {
      public String[] call() throws Exception {
        Tracer tracer = newTracer("toSourceArray");
        try {
          int numInputs = inputs.size();
          String[] sources = new String[numInputs];
          CodeBuilder cb = new CodeBuilder();
          for (int i = 0; i < numInputs; i++) {
            Node scriptNode = inputs.get(i).getAstRoot(Compiler.this);
            cb.reset();
            toSource(cb, i, scriptNode);
            sources[i] = cb.toString();
          }
          return sources;
        } finally {
          stopTracer(tracer, "toSourceArray");
        }
      }
    });
  }

  /**
   * Converts the parse tree for a module back to js code.
   */
  public String toSource(final JSModule module) {
    return runInCompilerThread(new Callable<String>() {
      public String call() throws Exception {
        List<CompilerInput> inputs = module.getInputs();
        int numInputs = inputs.size();
        if (numInputs == 0) {
          return "";
        }
        CodeBuilder cb = new CodeBuilder();
        for (int i = 0; i < numInputs; i++) {
          Node scriptNode = inputs.get(i).getAstRoot(Compiler.this);
          if (scriptNode == null) {
            throw new IllegalArgumentException(
                "Bad module: " + module.getName());
          }
          toSource(cb, i, scriptNode);
        }
        return cb.toString();
      }
    });
  }


  /**
   * Converts the parse tree for each input in a module back to js code.
   */
  public String[] toSourceArray(final JSModule module) {
    return runInCompilerThread(new Callable<String[]>() {
      public String[] call() throws Exception {
        List<CompilerInput> inputs = module.getInputs();
        int numInputs = inputs.size();
        if (numInputs == 0) {
          return new String[0];
        }

        String[] sources = new String[numInputs];
        CodeBuilder cb = new CodeBuilder();
        for (int i = 0; i < numInputs; i++) {
          Node scriptNode = inputs.get(i).getAstRoot(Compiler.this);
          if (scriptNode == null) {
            throw new IllegalArgumentException(
                "Bad module input: " + inputs.get(i).getName());
          }

          cb.reset();
          toSource(cb, i, scriptNode);
          sources[i] = cb.toString();
        }
        return sources;
      }
    });
  }

  /**
   * Writes out js code from a root node. If printing input delimiters, this
   * method will attach a comment to the start of the text indicating which
   * input the output derived from. If there were any preserve annotations
   * within the root's source, they will also be printed in a block comment
   * at the beginning of the output.
   */
  public void toSource(final CodeBuilder cb,
                       final int inputSeqNum,
                       final Node root) {
    runInCompilerThread(new Callable<Void>() {
      public Void call() throws Exception {
        if (options.printInputDelimiter) {
          if ((cb.getLength() > 0) && !cb.endsWith("\n")) {
            cb.append("\n");  // Make sure that the label starts on a new line
          }
          Preconditions.checkState(root.getType() == Token.SCRIPT);

          String delimiter = options.inputDelimiter;

          String sourceName = (String)root.getProp(Node.SOURCENAME_PROP);
          Preconditions.checkState(sourceName != null);
          Preconditions.checkState(!sourceName.isEmpty());

          delimiter = delimiter.replaceAll("%name%", sourceName)
            .replaceAll("%num%", String.valueOf(inputSeqNum));

          cb.append(delimiter)
            .append("\n");
        }
        if (root.getJSDocInfo() != null &&
            root.getJSDocInfo().getLicense() != null) {
          cb.append("/*\n")
            .append(root.getJSDocInfo().getLicense())
            .append("*/\n");
        }

        // If there is a valid source map, then indicate to it that the current
        // root node's mappings are offset by the given string builder buffer.
        if (options.sourceMapOutputPath != null) {
          sourceMap.setStartingPosition(
              cb.getLineIndex(), cb.getColumnIndex());
        }

        String code = toSource(root);
        if (!code.isEmpty()) {
          cb.append(code);
          if (!code.endsWith(";")) {
            cb.append(";");
          }
        }
        return null;
      }
    });
  }

  /**
   * Generates JavaScript source code for an AST.
   */
  @Override
  String toSource(Node n) {
    initCompilerOptionsIfTesting();

    CodePrinter.Builder builder = new CodePrinter.Builder(n);
    builder.setPrettyPrint(options.prettyPrint);
    builder.setLineBreak(options.lineBreak);
    builder.setSourceMap(sourceMap);
    builder.setOutputCharset(options.outputCharset);
    return builder.build();
  }

  /**
   * Stores a buffer of text to which more can be appended.  This is just like a
   * StringBuilder except that we also track the number of lines.
   */
  public static class CodeBuilder {
    private final StringBuilder sb = new StringBuilder();
    private int lineCount = 0;

    /** Removes all text, but leaves the line count unchanged. */
    void reset() {
      sb.setLength(0);
    }

    /** Appends the given string to the text buffer. */
    CodeBuilder append(String str) {
      sb.append(str);

      // Move the line count to the end of the new text.
      int index = -1;
      while ((index = str.indexOf('\n', index + 1)) >= 0) {
        ++lineCount;
      }

      return this;
    }

    /** Returns all text in the text buffer. */
    @Override
    public String toString() {
      return sb.toString();
    }

    /** Returns the length of the text buffer. */
    public int getLength() {
      return sb.length();
    }

    /** Returns the (zero-based) index of the last line in the text buffer. */
    int getLineIndex() {
      return lineCount;
    }

    /** Returns the (zero-based) index of the last column in the text buffer. */
    int getColumnIndex() {
      int index = sb.lastIndexOf("\n");
      return (index >= 0) ? sb.length() - (index + 1) : sb.length();
    }

    /** Determines whether the text ends with the given suffix. */
    boolean endsWith(String suffix) {
      return (sb.length() > suffix.length())
          && suffix.equals(sb.substring(sb.length() - suffix.length()));
    }
  }

  //------------------------------------------------------------------------
  // Optimizations
  //------------------------------------------------------------------------

  public void optimize() {
    // Ideally, this pass should be the first pass run, however:
    // 1) VariableReferenceCheck reports unexpected warnings if Normalize
    // is done first.
    // 2) ReplaceMessages, stripCode, and potentially custom passes rely on
    // unmodified local names.
    normalize();

    PhaseOptimizer phaseOptimizer = new PhaseOptimizer(this, tracker);
    if (options.devMode == DevMode.EVERY_PASS) {
      phaseOptimizer.setSanityCheck(sanityCheck);
    }
    phaseOptimizer.consume(getPassConfig().getOptimizations());
    phaseOptimizer.process(externsRoot, jsRoot);
    if (hasErrors()) {
      return;
    }
  }

  @Override
  void setCssRenamingMap(CssRenamingMap map) {
    options.cssRenamingMap = map;
  }

  @Override
  CssRenamingMap getCssRenamingMap() {
    return options.cssRenamingMap;
  }

  /**
   * Reprocesses the current defines over the AST.  This is used by GwtCompiler
   * to generate N outputs for different targets from the same (checked) AST.
   * For each target, we apply the target-specific defines by calling
   * {@code processDefines} and then {@code optimize} to optimize the AST
   * specifically for that target.
   */
  public void processDefines() {
    (new DefaultPassConfig(options)).processDefines.create(this)
        .process(externsRoot, jsRoot);
  }

  boolean isInliningForbidden() {
    return options.propertyRenaming == PropertyRenamingPolicy.HEURISTIC ||
        options.propertyRenaming ==
            PropertyRenamingPolicy.AGGRESSIVE_HEURISTIC;
  }

  /** Control Flow Analysis. */
  ControlFlowGraph<Node> computeCFG() {
    logger.info("Computing Control Flow Graph");
    Tracer tracer = newTracer("computeCFG");
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(this, true);
    process(cfa);
    stopTracer(tracer, "computeCFG");
    return cfa.getCfg();
  }

  public void normalize() {
    logger.info("Normalizing");
    startPass("normalize");
    process(new Normalize(this, false));
    setNormalized();
    endPass();
  }

  @Override
  void prepareAst(Node root) {
    Tracer tracer = newTracer("prepareAst");
    CompilerPass pass = new PrepareAst(this);
    pass.process(null, root);
    stopTracer(tracer, "prepareAst");
  }

  void recordFunctionInformation() {
    logger.info("Recording function information");
    startPass("recordFunctionInformation");
    RecordFunctionInformation recordFunctionInfoPass =
        new RecordFunctionInformation(
            this, getPassConfig().getIntermediateState().functionNames);
    process(recordFunctionInfoPass);
    functionInformationMap = recordFunctionInfoPass.getMap();
    endPass();
  }

  protected final CodeChangeHandler.RecentChange recentChange =
      new CodeChangeHandler.RecentChange();
  private final List<CodeChangeHandler> codeChangeHandlers =
      Lists.<CodeChangeHandler>newArrayList();

  @Override
  void addChangeHandler(CodeChangeHandler handler) {
    codeChangeHandlers.add(handler);
  }

  @Override
  void removeChangeHandler(CodeChangeHandler handler) {
    codeChangeHandlers.remove(handler);
  }

  /**
   * All passes should call reportCodeChange() when they alter
   * the JS tree structure. This is verified by CompilerTestCase.
   * This allows us to optimize to a fixed point.
   */
  @Override
  public void reportCodeChange() {
    for (CodeChangeHandler handler : codeChangeHandlers) {
      handler.reportChange();
    }
  }

  @Override
  public CodingConvention getCodingConvention() {
    CodingConvention convention = options.getCodingConvention();
    convention = convention != null ? convention : defaultCodingConvention;
    return convention;
  }

  @Override
  public boolean isIdeMode() {
    return options.ideMode;
  }

  @Override
  Config getParserConfig() {
    if (parserConfig == null) {
      parserConfig = ParserRunner.createConfig(
          getTypeRegistry(), isIdeMode());
    }
    return parserConfig;
  }

  @Override
  public boolean isTypeCheckingEnabled() {
    return options.checkTypes;
  }


  //------------------------------------------------------------------------
  // Error reporting
  //------------------------------------------------------------------------

  @Override
  public void report(JSError error) {
    CheckLevel level = error.level;
    WarningsGuard guard = options.getWarningsGuard();
    if (guard != null) {
      CheckLevel newLevel = guard.level(error);
      if (newLevel != null) {
        level = newLevel;
      }
    }

    if (level.isOn()) {
      errorManager.report(level, error);
    }
  }

  /**
   * Report an internal error.
   */
  @Override
  void throwInternalError(String message, Exception cause) {
    String finalMessage =
      "INTERNAL COMPILER ERROR.\n" +
      "Please report this problem.\n" + message;

    RuntimeException e = new RuntimeException(finalMessage, cause);
    if (cause != null) {
      e.setStackTrace(cause.getStackTrace());
    }
    throw e;
  }


  /**
   * Gets the number of errors.
   */
  public int getErrorCount() {
    return errorManager.getErrorCount();
  }

  /**
   * Gets the number of warnings.
   */
  public int getWarningCount() {
    return errorManager.getWarningCount();
  }

  @Override
  boolean hasHaltingErrors() {
    return !isIdeMode() && getErrorCount() > 0;
  }

  /**
   * Consults the {@link ErrorManager} to see if we've encountered errors
   * that should halt compilation. <p>
   *
   * If {@link CompilerOptions#ideMode} is {@code true}, this function
   * always returns {@code false} without consulting the error manager. The
   * error manager will continue to be told about new errors and warnings, but
   * the compiler will complete compilation of all inputs.<p>
   */
  public boolean hasErrors() {
    return hasHaltingErrors();
  }

  /** Called from the compiler passes, adds debug info */
  @Override
  void addToDebugLog(String str) {
    debugLog.append(str);
    debugLog.append('\n');
    logger.fine(str);
  }

  private SourceFile getSourceFileByName(String sourceName) {
    if (inputsByName.containsKey(sourceName)) {
      return inputsByName.get(sourceName).getSourceFile();
    }
    return null;
  }

  public String getSourceLine(String sourceName, int lineNumber) {
    if (lineNumber < 1) {
      return null;
    }
    SourceFile input = getSourceFileByName(sourceName);
    if (input != null) {
      return input.getLine(lineNumber);
    }
    return null;
  }

  public Region getSourceRegion(String sourceName, int lineNumber) {
    if (lineNumber < 1) {
      return null;
    }
    SourceFile input = getSourceFileByName(sourceName);
    if (input != null) {
      return input.getRegion(lineNumber);
    }
    return null;
  }

  //------------------------------------------------------------------------
  // Package-private helpers
  //------------------------------------------------------------------------

  @Override
  Node getNodeForCodeInsertion(JSModule module) {
    if (module == null) {
      if (inputs.isEmpty()) {
        throw new IllegalStateException("No inputs");
      }

      return inputs.get(0).getAstRoot(this);
    }

    List<CompilerInput> moduleInputs = module.getInputs();
    if (moduleInputs.size() > 0) {
      return moduleInputs.get(0).getAstRoot(this);
    }
    throw new IllegalStateException("Root module has no inputs");
  }

  public SourceMap getSourceMap() {
    return sourceMap;
  }

  VariableMap getVariableMap() {
    return getPassConfig().getIntermediateState().variableMap;
  }

  VariableMap getPropertyMap() {
    return getPassConfig().getIntermediateState().propertyMap;
  }

  CompilerOptions getOptions() {
    return options;
  }

  FunctionInformationMap getFunctionalInformationMap() {
    return functionInformationMap;
  }

  /**
   * Sets the logging level for the com.google.javascript.jscomp package.
   */
  public static void setLoggingLevel(Level level) {
    logger.setLevel(level);
  }

  /** Gets the DOT graph of the AST generated at the end of compilation. */
  public String getAstDotGraph() throws IOException {
    if (jsRoot != null) {
      ControlFlowAnalysis cfa = new ControlFlowAnalysis(this, true);
      cfa.process(null, jsRoot);
      return DotFormatter.toDot(jsRoot, cfa.getCfg());
    } else {
      return "";
    }
  }

  @Override
  public ErrorManager getErrorManager() {
    if (options == null) {
      initOptions(new CompilerOptions());
    }
    return errorManager;
  }

  /**
   * Stores the internal compiler state just before optimization is performed.
   * This can be saved and restored in order to efficiently optimize multiple
   * different output targets without having to perform checking multiple times.
   *
   * NOTE: This does not include all parts of the compiler's internal state. In
   * particular, JSSourceFiles and CompilerOptions are not recorded. In
   * order to recreate a Compiler instance from scratch, you would need to
   * call {@code init} with the same arguments as in the initial creation before
   * restoring intermediate state.
   */
  public static class IntermediateState implements Serializable {
    private static final long serialVersionUID = 1L;

    Node externsRoot;
    private Node jsRoot;
    private List<CompilerInput> externs;
    private List<CompilerInput> inputs;
    private List<JSModule> modules;
    private PassConfig.State passConfigState;
    private JSTypeRegistry typeRegistry;
    private boolean normalized;

    private IntermediateState() {}
  }

  /**
   * Returns the current internal state, excluding the input files and modules.
   */
  public IntermediateState getState() {
    IntermediateState state = new IntermediateState();
    state.externsRoot = externsRoot;
    state.jsRoot = jsRoot;
    state.externs = externs;
    state.inputs = inputs;
    state.modules = modules;
    state.passConfigState = getPassConfig().getIntermediateState();
    state.typeRegistry = typeRegistry;
    state.normalized = normalized;

    return state;
  }

  /**
   * Sets the internal state to the capture given.  Note that this assumes that
   * the input files are already set up.
   */
  public void setState(IntermediateState state) {
    externsRoot = state.externsRoot;
    jsRoot = state.jsRoot;
    externs = state.externs;
    inputs = state.inputs;
    modules = state.modules;
    passes = createPassConfigInternal();
    getPassConfig().setIntermediateState(state.passConfigState);
    typeRegistry = state.typeRegistry;
    normalized = state.normalized;
  }

  @Override
  boolean hasRegExpGlobalReferences() {
    return hasRegExpGlobalReferences;
  }

  @Override
  void setHasRegExpGlobalReferences(boolean references) {
    hasRegExpGlobalReferences = references;
  }
}
