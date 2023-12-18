package com.jordansamhi.experiments.callgraphsoundness;

import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOption;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import com.jordansamhi.androspecter.instrumentation.Instrumenter;
import com.jordansamhi.androspecter.printers.Writer;
import org.apache.commons.io.FilenameUtils;
import soot.PackManager;

import java.util.Date;

/**
 * The `InstrumenterApk` class provides an entry point for the APK instrumentation process.
 * The main method of this class orchestrates the overall process by initializing various components and
 * executing necessary steps in a sequence.
 * <p>
 * The process starts by parsing command line options for the output directory, platform path, and APK path.
 * Subsequently, it prepares for the instrumentation by loading Soot, a framework for analyzing and transforming
 * Java and Android applications.
 * <p>
 * The application's methods are then instrumented to add logging functionality, using a pack from the
 * `PackManager`. The instrumented APK is written to the output directory.
 * <p>
 * Any exceptions occurring during the process are caught and logged.
 *
 * @author Jordan Samhi
 * @see Writer
 * @see SootUtils
 * @see PackManager
 */
public class InstrumenterApk {
    public static void main(String[] args) {
        CommandLineOptions options = CommandLineOptions.v();
        options.setAppName("AndroLibZoo FlowDroid Experiment");
        options.addOption(new CommandLineOption("apk", "a", "The APK to instrument", true, true));
        options.addOption(new CommandLineOption("platforms", "p", "Platform file", true, true));
        options.addOption(new CommandLineOption("output", "o", "Output folder", true, true));
        options.parseArgs(args);

        Writer.v().pinfo(String.format("Instrumentation started on %s\n", new Date()));
        String output = CommandLineOptions.v().getOptionValue("output");
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
        String apkPath = CommandLineOptions.v().getOptionValue("apk");

        try {
            String appName = FilenameUtils.getBaseName(apkPath);
            Writer.v().pinfo(String.format("Processing: %s", appName));

            Writer.v().pinfo("Loading Soot...");
            SootUtils su = new SootUtils();
            su.setupSootWithOutput(platformsPath, apkPath, output, false);
            Writer.v().psuccess("Soot loaded.");

            PackManager.v().getPack("jtp").add(Instrumenter.v().addLogToAllMethods("MY_LOGGER", "jtp.methodsLogger"));

            Writer.v().pinfo("Instrumentation in progress..");
            PackManager.v().runPacks();
            Writer.v().psuccess("Instrumentation done.");

            Writer.v().pinfo(String.format("Writing new apk file in %s", output));
            PackManager.v().writeOutput();
            Writer.v().psuccess("Apk written.");
        } catch (Exception e) {
            Writer.v().perror(String.format("An exception occurred: %s", e.getMessage()));
        }
    }
}