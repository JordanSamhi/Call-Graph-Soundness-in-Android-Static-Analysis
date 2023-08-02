package com.jordansamhi.experiments.callgraphsoundness;

import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.printers.Writer;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsInstrumenterApk;
import com.jordansamhi.experiments.callgraphsoundness.utils.ResultsAccumulator;
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
 * @see CommandLineOptionsInstrumenterApk
 * @see Writer
 * @see ResultsAccumulator
 * @see SootUtils
 * @see PackManager
 */
public class InstrumenterApk {
    public static void main(String[] args) {
        CommandLineOptionsInstrumenterApk.v().parseArgs(args);
        Writer.v().pinfo(String.format("Instrumentation started on %s\n", new Date()));
        String output = CommandLineOptionsInstrumenterApk.v().getOutput();
        String platformsPath = CommandLineOptionsInstrumenterApk.v().getPlatforms();
        String apkPath = CommandLineOptionsInstrumenterApk.v().getApk();

        try {
            String appName = FilenameUtils.getBaseName(apkPath);
            Writer.v().pinfo(String.format("Processing: %s", appName));
            ResultsAccumulator.v().setAppName(appName);

            Writer.v().pinfo("Loading Soot...");
            SootUtils su = new SootUtils();
            su.setupSootWithOutput(platformsPath, apkPath, output, false);
            Writer.v().psuccess("Soot loaded.");

            PackManager.v().getPack("jtp").add(com.jordansamhi.androspecter.instrumentation.Instrumenter.v().addLogToAllMethods("MY_LOGGER", "jtp.methodsLogger"));

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