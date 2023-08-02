package com.jordansamhi.experiments.callgraphsoundness;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.network.RedisManager;
import com.jordansamhi.androspecter.printers.Writer;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsInstrumenterRedis;
import com.jordansamhi.experiments.callgraphsoundness.utils.ResultsAccumulator;
import org.apache.commons.io.FilenameUtils;
import soot.PackManager;

import java.io.File;
import java.util.Date;

/**
 * The `InstrumenterRedis` class provides an entry point for the APK instrumentation process.
 * This version of the instrumentation process is designed to work with APKs fetched from a Redis server.
 * The main method of this class orchestrates the overall process by initializing various components and
 * executing necessary steps in a sequence.
 * <p>
 * The process begins by parsing command line options, establishing a connection with the Redis server,
 * and initializing AndroZooUtils with an API key. It then enters a loop where it tries to fetch an APK SHA from
 * the Redis server, which is then used to get the actual APK.
 * <p>
 * If the fetched SHA is null, the program sleeps for 30 seconds and tries again. Once a valid SHA is obtained,
 * the APK is loaded into Soot for instrumentation.
 * <p>
 * Two instrumentation packs are then added to the `PackManager`. The instrumented APK is written to the output
 * directory. If the APK file is deleted successfully after the process, a success message is logged.
 * <p>
 * Any exceptions occurring during the process are caught and logged.
 *
 * @author Jordan Samhi
 * @see CommandLineOptionsInstrumenterRedis
 * @see Writer
 * @see RedisManager
 * @see AndroZooUtils
 * @see ResultsAccumulator
 * @see SootUtils
 * @see PackManager
 */
public class InstrumenterRedis {
    public static void main(String[] args) {
        CommandLineOptionsInstrumenterRedis.v().parseArgs(args);
        Writer.v().pinfo(String.format("Instrumentation started on %s\n", new Date()));
        String output = CommandLineOptionsInstrumenterRedis.v().getOutput();
        String apikey = CommandLineOptionsInstrumenterRedis.v().getAndroZooApiKey();
        String platformsPath = CommandLineOptionsInstrumenterRedis.v().getPlatforms();
        String redisSrv = CommandLineOptionsInstrumenterRedis.v().getRedisServer();
        String redisPwd = CommandLineOptionsInstrumenterRedis.v().getRedisPwd();
        String redisPort = CommandLineOptionsInstrumenterRedis.v().getRedisPort();
        String redisRoot = CommandLineOptionsInstrumenterRedis.v().getRedisRoot();

        String redisSpop = String.format("%s:pop", redisRoot);
        RedisManager rm = new RedisManager(redisSrv, redisPort, redisPwd);
        AndroZooUtils au = new AndroZooUtils(apikey);

        try {
            while (true) {
                String pop = rm.spop(redisSpop);
                if (pop == null) {
                    Writer.v().pwarning("No SHA received, sleeping for 30 sec...");
                    Thread.sleep(30000);
                    continue;
                }
                Writer.v().psuccess(String.format("SHA well received: %s", pop));
                String apkPath = au.getApk(pop);
                String appName = FilenameUtils.getBaseName(apkPath);
                ResultsAccumulator.v().setAppName(appName);

                Writer.v().pinfo("Loading Soot...");
                SootUtils su = new SootUtils();
                su.setupSootWithOutput(platformsPath, apkPath, output, false);
                Writer.v().psuccess("Soot loaded.");

                PackManager.v().getPack("jtp").add(com.jordansamhi.androspecter.instrumentation.Instrumenter.v().addLogToAllMethodCalls("MY_LOGGER", "jtp.callsLogger"));
                PackManager.v().getPack("jtp").add(com.jordansamhi.androspecter.instrumentation.Instrumenter.v().addLogToAllMethods("MY_LOGGER", "jtp.methodsLogger"));

                Writer.v().pinfo("Instrumentation in progress..");
                PackManager.v().runPacks();
                Writer.v().psuccess("Instrumentation done.");

                Writer.v().pinfo(String.format("Writing new apk file in %s", output));
                PackManager.v().writeOutput();
                Writer.v().psuccess("Apk written.");

                if (new File(apkPath).delete()) {
                    Writer.v().psuccess("App deleted successfully");
                } else {
                    Writer.v().perror("Failed to delete file");
                }
            }
        } catch (Exception e) {
            Writer.v().perror(String.format("An exception occurred: %s", e.getMessage()));
        }
    }
}