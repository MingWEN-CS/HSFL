/*
 * Copyright 2009 Google Inc.
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
import com.google.common.collect.Maps;
import com.google.common.flags.DocLevel;
import com.google.common.flags.Flag;
import com.google.common.flags.FlagSpec;
import com.google.common.flags.Flags;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;
import com.google.protobuf.CodedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


/**
 * AbstractCompilerRunner encapsulates the logic required to run the Compiler.
 * This class is designed to be extended and used to create other Java classes
 * that behave the same as running the Compiler. Example:
 *
 * <pre>
 * class MyCompilerRunner extends
 *     AbstractCompilerRunner<MyCompiler, MyOptions> {
 *   MyCompilerRunner(String[] args) {
 *     super(args);
 *   }
 *
 *   &#064;Override
 *   protected MyOptions createOptions() {
 *     MyOptions options = new MyOptions();
 *     CompilerFlagTranslator.setOptionsFromFlags(options);
 *     addMyCrazyCompilerPassThatOutputsAnExtraFile(options);
 *     return options;
 *   }
 *
 *   &#064;Override
 *   protected MyCompiler createCompiler() {
 *     return new MyCompiler();
 *   }
 *
 *   public static void main(String[] args) {
 *     (new MyCompilerRunner(args)).run();
 *   }
 * }
 * </pre>
 *
*
 */
public abstract class AbstractCompilerRunner<A extends Compiler,
    B extends CompilerOptions> {

  @FlagSpec(help = "Prints out the parse tree and exits")
  public static final Flag<Boolean> FLAG_print_tree = Flag.value(false);

  @FlagSpec(help = "Prints a dot file describing the internal abstract syntax"
      + " tree and exits")
  public static final Flag<Boolean> FLAG_print_ast = Flag.value(false);

  @FlagSpec(help = "Turns on extra sanity checks.", altName = "dev_mode",
      docLevel = DocLevel.SECRET)
  public static final Flag<CompilerOptions.DevMode> FLAG_jscomp_dev_mode =
      Flag.value(CompilerOptions.DevMode.OFF);

  @FlagSpec(help = "The logging level (standard java.util.logging.Level"
      + " values) for Compiler progress. Does not control errors or"
      + " warnings for the JavaScript code under compilation.")
  public static final Flag<String> FLAG_logging_level =
      Flag.value(Level.WARNING.getName());

  @FlagSpec(help = "The file containing javascript externs. You may specify"
      + " multiple")
  public static final Flag<List<String>> FLAG_externs = Flag.stringCollector();

  @FlagSpec(help = "The javascript filename. You may specify multiple")
  public static final Flag<List<String>> FLAG_js = Flag.stringCollector();

  @FlagSpec(help = "Primary output filename. If not specified, output is " +
            "written to stdout.")
  static final Flag<String> FLAG_js_output_file = Flag.value("");

  @FlagSpec(help = "A javascript module specification. The format is "
      + "<name>:<num-js-files>[:[<dep>,...][:]]]. Module names must be "
      + "unique. Each dep is the name of a module that this module "
      + "depends on. Modules must be listed in dependency order, and js "
      + "source files must be listed in the corresponding order. Where "
      + "--module flags occur in relation to --js flags is unimportant.")
  public static final Flag<List<String>> FLAG_module = Flag.stringCollector();

  @FlagSpec(help = "File containing the serialized version of the variable "
      + "renaming map produced by a previous compilation.")
  public static final Flag<String> FLAG_variable_map_input_file =
      Flag.value("");

  @FlagSpec(help = "File containing the serialized version of the property "
      + "renaming map produced by a previous compilation.")
  public static final Flag<String> FLAG_property_map_input_file =
      Flag.value("");

  @FlagSpec(help = "File where the serialized version of the variable "
      + "renaming map produced should be saved.")
  public static final Flag<String> FLAG_variable_map_output_file =
      Flag.value("");

  @FlagSpec(help = "If true, variable renaming and property renaming map "
      + "files will be produced as {binary name}_vars_map.out and "
      + "{binary name}_props_map.out. Note that this flag cannot be used "
      + "in conjunction with either variable_map_output_file or "
      + "property_map_output_file.")
  public static final Flag<Boolean> FLAG_create_name_map_files =
      Flag.value(false);

  @FlagSpec(help = "File where the serialized version of the property "
      + "renaming map produced should be saved.")
  public static final Flag<String> FLAG_property_map_output_file =
      Flag.value("");

  @FlagSpec(help = "Check source validity but do not enforce Google style "
      + "rules and conventions, such as capitalized constant names or "
      + "opt_parameter.")
  public static final Flag<Boolean> FLAG_third_party = Flag.value(false);


  @FlagSpec(help = "Controls how detailed the compilation summary is. Values:"
      + " 0 (never print summary), 1 (print summary only if there are "
      + "errors or warnings), 2 (print summary if type checking is on, "
      + "see --check_types), 3 (always print summary). The default level "
      + "is 1.")
  public static final Flag<Integer> FLAG_summary_detail_level = Flag.value(1);

  @FlagSpec(help = "Interpolate output into this string at the place denoted"
      + " by the marker token %output%. See --output_wrapper_marker")
  public static final Flag<String> FLAG_output_wrapper = Flag.value("");

  @FlagSpec(help = "Use this token as output marker in the value of"
      + " --output_wrapper")
  public static final Flag<String> FLAG_output_wrapper_marker =
      Flag.value("%output%");

  @FlagSpec(help = "An output wrapper for a javascript module (optional). "
      + "The format is <name>:<wrapper>. The module name must correspond "
      + "with a module specified using --module. The wrapper must "
      + "contain %s as the code placeholder.")
  public static final Flag<List<String>> FLAG_module_wrapper =
      Flag.stringCollector();

  @FlagSpec(help = "Prefix for filenames of compiled js modules. "
      + "<module-name>.js will be appended to this prefix. Directories "
      + "will be created as needed. Use with --module.")
  public static final Flag<String> FLAG_module_output_path_prefix =
      Flag.value("./");

  @FlagSpec(help = "If specified, a source map file mapping the generated " +
            "source files back to the original source file will be " +
            "output to the specified path. If %module% is added, " +
            "a source map will be generated for each module, with the " +
            " module's name placed into the path at that spot.")
  public static final Flag<String> FLAG_create_source_map =
      Flag.value("");

  private PrintStream out;
  private final PrintStream err;
  private A compiler;

  public AbstractCompilerRunner(String[] args) {
    this(args, System.out, System.err);
  }

  public AbstractCompilerRunner(String[] args, PrintStream out,
      PrintStream err) {
    // Flags are read when a compiler is instantiated, so we parse them first.
    Flags.parse(args);

    this.out = out;
    this.err = err;
  }

  /**
   * Returns the instance of the Compiler to use when {@link #run()} is
   * called.
   */
  protected abstract A createCompiler();

  /**
   * Returns the instance of the Options to use when {@link #run()} is called.
   * createCompiler() is called before createOptions(), so getCompiler()
   * will not return null when createOptions() is called.
   */
  protected abstract B createOptions();

  final protected A getCompiler() {
    return compiler;
  }

  /**
   * Runs the Compiler and calls System.exit() with the exit status of the
   * compiler.
   */
  final public void run() {
    int result;
    try {
      result = doRun();
    } catch (AbstractCompilerRunner.FlagUsageException e) {
      System.err.println(e.getMessage());
      result = -1;
    } catch (Throwable t) {
      t.printStackTrace();
      result = -2;
    }
    System.exit(result);
  }

  /**
   * Returns the PrintStream for writing errors associated with this
   * AbstractCompilerRunner.
   */
  protected PrintStream getErrorPrintStream() {
    return err;
  }

  /**
   * An exception thrown when command-line flags are used incorrectly.
   */
  public static class FlagUsageException extends Exception {
    private static final long serialVersionUID = 1L;

    FlagUsageException(String message) {
      super(message);
    }
  }

  /**
   * Creates inputs from a list of files.
   *
   * @param files A list of filenames
   * @param allowStdIn Whether '-' is allowed appear as a filename to represent
   *        stdin. If true, '-' is only allowed to appear once.
   * @return An array of inputs
   */
  private static List<JSSourceFile> createInputs(List<String> files,
      boolean allowStdIn) throws FlagUsageException, IOException {
    List<JSSourceFile> inputs = new ArrayList<JSSourceFile>(files.size());
    boolean usingStdin = false;
    for (String filename : files) {
      if (!"-".equals(filename)) {
        inputs.add(JSSourceFile.fromFile(filename));
      } else {
        if (!allowStdIn) {
          throw new FlagUsageException("Can't specify stdin.");
        }
        if (usingStdin) {
          throw new FlagUsageException("Can't specify stdin twice.");
        }

        inputs.add(JSSourceFile.fromInputStream("stdin", System.in));
        usingStdin = true;
      }
    }
    return inputs;
  }

  /**
   * Creates js source code inputs from a list of files.
   */
  private static List<JSSourceFile> createSourceInputs(List<String> files)
      throws FlagUsageException, IOException {
    if (files.isEmpty()) {
      files = Collections.singletonList("-");
    }
    try {
      return createInputs(files, true);
    } catch (FlagUsageException e) {
      throw new FlagUsageException("Bad --js flag. " + e.getMessage());
    }
  }

  /**
   * Creates js extern inputs from a list of files.
   */
  private static List<JSSourceFile> createExternInputs(List<String> files)
      throws FlagUsageException, IOException {
    if (files.isEmpty()) {
      return ImmutableList.of(JSSourceFile.fromCode("/dev/null", ""));
    }
    try {
      return createInputs(files, false);
    } catch (FlagUsageException e) {
      throw new FlagUsageException("Bad --externs flag. " + e.getMessage());
    }
  }

  /**
   * Creates module objects from a list of module specifications.
   *
   * @param specs A list of module specifications, not null or empty. The spec
   *        format is: <code>name:num-js-files[:[dep,...][:]]</code>. Module
   *        names must not contain the ':' character.
   * @param jsFiles A list of js file paths, not null
   * @return An array of module objects
   */
  static JSModule[] createJsModules(List<String> specs, List<String> jsFiles)
      throws FlagUsageException, IOException {
    Preconditions.checkState(specs != null);
    Preconditions.checkState(!specs.isEmpty());
    Preconditions.checkState(jsFiles != null);

    final int totalNumJsFiles = jsFiles.size();
    int nextJsFileIndex = 0;

    Map<String, JSModule> modulesByName = Maps.newLinkedHashMap();
    for (String spec : specs) {

      // Format is "<name>:<num-js-files>[:[<dep>,...][:]]".
      String[] parts = spec.split(":");
      if (parts.length < 2 || parts.length > 4) {
        throw new FlagUsageException("Expected 2-4 colon-delimited parts in "
            + "module spec: " + spec);
      }

      // Parse module name.
      String name = parts[0];
      if (!TokenStream.isJSIdentifier(name)) {
        throw new FlagUsageException("Invalid module name: '" + name + "'");
      }
      if (modulesByName.containsKey(name)) {
        throw new FlagUsageException("Duplicate module name: " + name);
      }
      JSModule module = new JSModule(name);

      // Parse module inputs.
      int numJsFiles = -1;
      try {
        numJsFiles = Integer.parseInt(parts[1]);
      } catch (NumberFormatException ignored) {
        numJsFiles = -1;
      }
      if (numJsFiles < 1) {
        throw new FlagUsageException("Invalid js file count '" + parts[1]
            + "' for module: " + name);
      }
      if (nextJsFileIndex + numJsFiles > totalNumJsFiles) {
        throw new FlagUsageException("Not enough js files specified. Expected "
            + (nextJsFileIndex + numJsFiles - totalNumJsFiles)
            + " more in module:" + name);
      }
      List<String> moduleJsFiles =
          jsFiles.subList(nextJsFileIndex, nextJsFileIndex + numJsFiles);
      for (JSSourceFile input : createInputs(moduleJsFiles, false)) {
        module.add(input);
      }
      nextJsFileIndex += numJsFiles;

      if (parts.length > 2) {
        // Parse module dependencies.
        String depList = parts[2];
        if (depList.length() > 0) {
          String[] deps = depList.split(",");
          for (String dep : deps) {
            JSModule other = modulesByName.get(dep);
            if (other == null) {
              throw new FlagUsageException("Module '" + name
                  + "' depends on unknown module '" + dep
                  + "'. Be sure to list modules in dependency order.");
            }
            module.addDependency(other);
          }
        }
      }

      modulesByName.put(name, module);
    }

    if (nextJsFileIndex < totalNumJsFiles) {
      throw new FlagUsageException("Too many js files specified. Expected "
          + nextJsFileIndex + " but found " + totalNumJsFiles);
    }

    return modulesByName.values().toArray(new JSModule[modulesByName.size()]);
  }

  /**
   * Parses module wrapper specifications.
   *
   * @param specs A list of module wrapper specifications, not null. The spec
   *        format is: <code>name:wrapper</code>. Wrappers.
   * @param modules The JS modules whose wrappers are specified
   * @return A map from module name to module wrapper. Modules with no wrapper
   *         will have the empty string as their value in this map.
   */
  static Map<String, String> parseModuleWrappers(List<String> specs,
      JSModule[] modules) throws FlagUsageException {
    Preconditions.checkState(specs != null);

    Map<String, String> wrappers =
        Maps.newHashMapWithExpectedSize(modules.length);

    // Prepopulate the map with module names.
    for (JSModule m : modules) {
      wrappers.put(m.getName(), "");
    }

    for (String spec : specs) {

      // Format is "<name>:<wrapper>".
      int pos = spec.indexOf(':');
      if (pos == -1) {
        throw new FlagUsageException("Expected module wrapper to have "
            + "<name>:<wrapper> format: " + spec);
      }

      // Parse module name.
      String name = spec.substring(0, pos);
      if (!wrappers.containsKey(name)) {
        throw new FlagUsageException("Unknown module: '" + name + "'");
      }
      String wrapper = spec.substring(pos + 1);
      if (!wrapper.contains("%s")) {
        throw new FlagUsageException("No %s placeholder in module wrapper: '"
            + wrapper + "'");
      }
      wrappers.put(name, wrapper);
    }
    return wrappers;
  }

  /**
   * Writes code to an output stream, optionally wrapping it in an arbitrary
   * wrapper that contains a placeholder where the code should be inserted.
   */
  static void writeOutput(PrintStream out, Compiler compiler, String code,
      String wrapper, String codePlaceholder) {
    int pos = wrapper.indexOf(codePlaceholder);
    if (pos != -1) {
      String prefix = "";

      if (pos > 0) {
        prefix = wrapper.substring(0, pos);
        out.print(prefix);
      }

      out.print(code);

      int suffixStart = pos + codePlaceholder.length();
      if (suffixStart == wrapper.length()) {
        // Nothing after placeholder?
        // Make sure we always end output with a line feed.
        out.println();
      } else {
        out.println(wrapper.substring(suffixStart));
      }

      // If we have a source map, adjust its offsets to match
      // the code WITHIN the wrapper.
      if (compiler != null && compiler.getSourceMap() != null) {
        compiler.getSourceMap().setWrapperPrefix(prefix);
      }

    } else {
      out.println(code);
    }
  }

  /**
   * Creates any directories necessary to write a file that will have a given
   * path prefix.
   */
  private static void maybeCreateDirsForPath(String pathPrefix) {
    if (pathPrefix.length() > 0) {
      String dirName =
          pathPrefix.charAt(pathPrefix.length() - 1) == File.separatorChar
              ? pathPrefix.substring(0, pathPrefix.length() - 1) : new File(
                  pathPrefix).getParent();
      new File(dirName).mkdirs();
    }
  }

  /**
   * Parses command-line arguments and runs the compiler.
   *
   * @return system exit status
   */
  protected int doRun() throws FlagUsageException, IOException {
    Compiler.setLoggingLevel(Level.parse(FLAG_logging_level.get()));

    List<JSSourceFile> externsList = createExterns();
    JSSourceFile[] externs = new JSSourceFile[externsList.size()];
    externsList.toArray(externs);

    compiler = createCompiler();
    B options = createOptions();

    JSModule[] modules = null;
    Result result;

    if (FLAG_js_output_file.get().length() > 0) {
      options.jsOutputFile = FLAG_js_output_file.get();
    }

    if (FLAG_create_source_map.get().length() > 0) {
      options.sourceMapOutputPath = FLAG_create_source_map.get();
    }

    if (!FLAG_variable_map_input_file.get().equals("")) {
      options.inputVariableMapSerialized =
          VariableMap.load(FLAG_variable_map_input_file.get()).toBytes();
    }

    if (!FLAG_property_map_input_file.get().equals("")) {
      options.inputPropertyMapSerialized =
          VariableMap.load(FLAG_property_map_input_file.get()).toBytes();
    }

    if (!options.jsOutputFile.equals("")) {
      out = new PrintStream(options.jsOutputFile);
    }

    if (FLAG_third_party.get()) {

      // Enable the generic coding conventions.
      compiler.setCodingConvention(new DefaultCodingConvention());
    }

    ((PrintStreamErrorManager) compiler.getErrorManager())
        .setSummaryDetailLevel(FLAG_summary_detail_level.get());

    List<String> jsFiles = FLAG_js.get();
    List<String> moduleSpecs = FLAG_module.get();
    if (!moduleSpecs.isEmpty()) {
      modules = createJsModules(moduleSpecs, jsFiles);
      result = compiler.compile(externs, modules, options);
    } else {
      List<JSSourceFile> inputList = createSourceInputs(jsFiles);
      JSSourceFile[] inputs = new JSSourceFile[inputList.size()];
      inputList.toArray(inputs);
      result = compiler.compile(externs, inputs, options);
    }

    if (FLAG_print_ast.get()) {
      if (compiler.getRoot() == null) {
        return 1;
      } else {
        ControlFlowGraph<Node> cfg = compiler.computeCFG();
        DotFormatter.appendDot(compiler.getRoot(), cfg, out);
        out.println();
        return 0;
      }
    }

    if (FLAG_print_tree.get()) {
      if (compiler.getRoot() == null) {
        out.println("Code contains errors; no tree was generated.");
        return 1;
      } else {
        compiler.getRoot().appendStringTree(out);
        out.println("");
        return 0;
      }
    }

    if (result.success) {
      if (modules == null) {
        writeOutput(out, compiler, compiler.toSource(), FLAG_output_wrapper
            .get(), FLAG_output_wrapper_marker.get());

        // Output the source map if requested.
        outputSourceMap(options, options.jsOutputFile);
      } else {
        String moduleFilePrefix = FLAG_module_output_path_prefix.get();
        maybeCreateDirsForPath(moduleFilePrefix);
        Map<String, String> moduleWrappers =
            parseModuleWrappers(FLAG_module_wrapper.get(), modules);

        // If the source map path is in fact a pattern for each
        // module, create a stream per-module. Otherwise, create
        // a single source map.
        PrintStream mapOut = null;

        if (!shouldGenerateMapPerModule(options)) {
          mapOut = openSourceMapStream(options, moduleFilePrefix, null);
        }

        for (JSModule m : modules) {
          if (shouldGenerateMapPerModule(options)) {
            mapOut =
                openSourceMapStream(options, moduleFilePrefix, m.getName());
          }

          PrintStream ps =
              new PrintStream(new FileOutputStream(moduleFilePrefix
                  + m.getName() + ".js"));

          if (options.sourceMapOutputPath != null) {
            compiler.getSourceMap().reset();
          }

          writeOutput(ps, compiler, compiler.toSource(m), moduleWrappers.get(
              m.getName()), "%s");

          if (options.sourceMapOutputPath != null) {
            compiler.getSourceMap().appendTo(mapOut, m.getName());
          }

          ps.close();

          if (shouldGenerateMapPerModule(options) && mapOut != null) {
            mapOut.close();
            mapOut = null;
          }
        }

        if (mapOut != null) {
          mapOut.close();
        }
      }

      // Output the externs if required.
      if (options.externExportsPath != null) {
        PrintStream eeOut =
            openExternExportsStream(options, options.jsOutputFile);
        eeOut.append(result.externExport);
        eeOut.close();
      }

      // Output the variable and property name maps if requested.
      outputNameMaps(options);
    }

    // return 0 if no errors, the error count otherwise
    return Math.min(result.errors.length, 0x7f);
  }

  protected List<JSSourceFile> createExterns() throws FlagUsageException,
      IOException {
    return createExternInputs(FLAG_externs.get());
  }

  /**
   * Returns true if and only if a source map file should be generated for each
   * module, as opposed to one unified map. This is specified by having the
   * source map pattern include the %module% variable.
   */
  private boolean shouldGenerateMapPerModule(B options) {
    return options.sourceMapOutputPath != null
        && options.sourceMapOutputPath.contains("%module%");
  }

  /**
   * Returns a stream for outputting the generated externs file.
   *
   * @param options The options to the Compiler.
   * @param path The path of the generated JS source file.
   *
   * @return The stream or null if no extern-ed exports are being generated.
   */
  private PrintStream openExternExportsStream(B options,
      String path) throws IOException {
    if (options.externExportsPath == null) {
      return null;
    }

    String exPath = options.externExportsPath;

    if (!exPath.contains(File.separator)) {
      File outputFile = new File(path);
      exPath = outputFile.getParent() + File.separatorChar + exPath;
    }

    return new PrintStream(new FileOutputStream(exPath));
  }

  /**
   * Returns a stream to give to an instance of the SourceMap class to which it
   * can append the source map. If no source mapping was specified in the
   * options, this method returns null.
   *
   * @param options The options to the Compiler.
   * @param path The directory or a file in the directory in which to place the
   *        source map.
   * @param module If modules are being generated, the name of the current
   *        module.
   */
  private PrintStream openSourceMapStream(B options, String path,
      String module) throws IOException {
    if (options.sourceMapOutputPath == null) {
      return null;
    }

    String sourceMapPath = options.sourceMapOutputPath;

    // Replace the %module% "variable" with the name of the module.
    if (module != null) {
      sourceMapPath = sourceMapPath.replace("%module%", module);
    }

    String mapPath = null;

    if (sourceMapPath.contains(File.separator)) {
      mapPath = sourceMapPath;
    } else {
      File outputFile = new File(path);
      mapPath = outputFile.getParent() + File.separatorChar + sourceMapPath;
    }

    return new PrintStream(new FileOutputStream(mapPath));
  }

  /**
   * Outputs the source map found in the compiler to the proper path if one
   * exists.
   *
   * @param options The options to the Compiler.
   * @param path The path of the generated file for which the source map was
   *        created.
   */
  private void outputSourceMap(B options, String path)
      throws IOException {
    if (options.sourceMapOutputPath == null) {
      return;
    }

    File outputFile = new File(path);
    PrintStream out = openSourceMapStream(options, path, null);
    compiler.getSourceMap().appendTo(out, outputFile.getName());
    out.close();
  }

  /**
   * Returns the path at which to output map file(s) based on the path at which
   * the JS binary will be placed.
   *
   * @return The path in which to place the generated map file(s).
   */
  private static String getMapPath(String outputFile) {
    String basePath = "";

    if (outputFile.equals("")) {
      // If we have a js_module_binary rule, output the maps
      // at modulename_props_map.out, etc.
      if (!FLAG_module_output_path_prefix.get().equals("")) {
        basePath = FLAG_module_output_path_prefix.get();
      } else {
        basePath = "jscompiler";
      }
    } else {
      // Get the path of the output file.
      File file = new File(outputFile);

      String outputFileName = file.getName();

      // Strip the .js from the name.
      if (outputFileName.endsWith(".js")) {
        outputFileName =
            outputFileName.substring(0, outputFileName.length() - 3);
      }

      basePath = file.getParent() + File.separatorChar + outputFileName;
    }

    return basePath;
  }

  /**
   * Outputs the variable and property name maps for the specified compiler if
   * the proper FLAGS are set.
   */
  private void outputNameMaps(B options) throws FlagUsageException,
      IOException {

    String propertyMapOutputPath = null;
    String variableMapOutputPath = null;
    String functionInformationMapOutputPath = null;

    // Check the create_name_map_files FLAG.
    if (FLAG_create_name_map_files.get()) {
      String basePath = getMapPath(options.jsOutputFile);

      propertyMapOutputPath = basePath + "_props_map.out";
      variableMapOutputPath = basePath + "_vars_map.out";
      functionInformationMapOutputPath = basePath + "_functions_map.out";
    }

    // Check the individual FLAGS.
    if (!FLAG_variable_map_output_file.get().equals("")) {
      if (variableMapOutputPath != null) {
        throw new FlagUsageException("The flags variable_map_output_file and "
            + "create_name_map_files cannot both be used simultaniously.");
      }

      variableMapOutputPath = FLAG_variable_map_output_file.get();
    }

    if (!FLAG_property_map_output_file.get().equals("")) {
      if (propertyMapOutputPath != null) {
        throw new FlagUsageException("The flags property_map_output_file and "
            + "create_name_map_files cannot both be used simultaniously.");
      }

      propertyMapOutputPath = FLAG_property_map_output_file.get();
    }

    // Output the maps.
    if (variableMapOutputPath != null) {
      if (compiler.getVariableMap() != null) {
        compiler.getVariableMap().save(variableMapOutputPath);
      }
    }

    if (propertyMapOutputPath != null) {
      if (compiler.getPropertyMap() != null) {
        compiler.getPropertyMap().save(propertyMapOutputPath);
      }
    }

    if (functionInformationMapOutputPath != null) {
      if (compiler.getFunctionalInformationMap() != null) {
        FileOutputStream file =
            new FileOutputStream(functionInformationMapOutputPath);
        CodedOutputStream outputStream = CodedOutputStream.newInstance(file);
        compiler.getFunctionalInformationMap().writeTo(outputStream);
        outputStream.flush();
        file.flush();
        file.close();
      }
    }
  }
}
