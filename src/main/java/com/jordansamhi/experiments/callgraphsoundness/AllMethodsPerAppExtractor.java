package com.jordansamhi.experiments.callgraphsoundness;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.network.RedisManager;
import com.jordansamhi.androspecter.printers.Writer;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;
import com.jordansamhi.experiments.callgraphsoundness.utils.ResultsAccumulator;
import com.jordansamhi.experiments.utils.Constants;
import org.apache.commons.io.FilenameUtils;
import soot.SootMethod;

import java.io.File;
import java.util.Date;
import java.util.Set;

public class AllMethodsPerAppExtractor {
    public static void main(String[] args) {
        CommandLineOptionsMethodsExtractor.v().parseArgs(args);
        Writer.v().pinfo(String.format("%s v1.0 started on %s\n", Constants.TOOL_NAME, new Date()));
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        String redisSrv = CommandLineOptionsMethodsExtractor.v().getRedisServer();
        String redisPwd = CommandLineOptionsMethodsExtractor.v().getRedisPwd();
        String redisPort = CommandLineOptionsMethodsExtractor.v().getRedisPort();
        String redisRoot = CommandLineOptionsMethodsExtractor.v().getRedisRoot();

        String listVector = String.format("%s:numbers", redisRoot);

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

                Writer.v().pinfo("Initializing Environment");

                SootUtils su = new SootUtils();
                su.setupSoot(platformsPath, apkPath, true);

                Set<SootMethod> allMethods = su.getAllMethods();
                Set<SootMethod> allNonLibraryMethods = su.getAllMethodsExceptLibraries();

                ResultsAccumulator.v().setNumMethodsInApp(allMethods.size());
                ResultsAccumulator.v().setNumNonLibraryMethodsInApp(allNonLibraryMethods.size());

                String vector = ResultsAccumulator.v().getVectorResults();

                Writer.v().pinfo("Sending results to Redis server");

                rm.lpush(listVector, vector);
                Writer.v().psuccess("Data successfully sent to Redis server");
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