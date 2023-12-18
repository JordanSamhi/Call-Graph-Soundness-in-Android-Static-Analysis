package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import com.jordansamhi.androspecter.network.RedisManager;
import com.jordansamhi.androspecter.printers.Writer;
import com.jordansamhi.experiments.callgraphsoundness.utils.DataCollector;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * This abstract class serves as the foundation for extracting and processing call graph information from Android apps.
 * <p>
 * It provides a `run()` method which connects to a Redis server, fetches SHA values and processes Android apps accordingly.
 * The class is designed to continuously operate, constantly fetching new SHA values from the Redis server and processing them.
 * <p>
 * For each SHA, it identifies an APK file, processes it to generate a call graph using different algorithms, collects data
 * from the call graph generation process and deletes the APK file from the system.
 * <p>
 * Subclasses of `MethodsExtractorBase` must provide implementation for `initEnv()`, `buildCallGraph()`, `getApkPath()`,
 * and `getAlgos()` abstract methods. The `run()` method utilizes these methods to perform its operations.
 *
 * @author Jordan Samhi
 * @see #run()
 * @see #initEnv(String)
 * @see #buildCallGraph(String, String)
 * @see #getApkPath(String)
 * @see #getAlgos()
 */
public abstract class MethodsExtractorBase {

    /**
     * This method starts the process of extracting and processing call graph information from Android apps.
     * <p>
     * The method initiates a connection to the Redis server, fetches the necessary data (SHA values), and performs operations
     * on the apps corresponding to these values. The method is intended to run indefinitely,
     * continuously fetching new SHAs from the Redis server and processing them.
     * <p>
     * Each SHA is used to locate an APK file, which is then processed to generate a call graph.
     * The call graph building process is performed for multiple algorithms and is implemented with a 10-minute timeout.
     * <p>
     * Data from the call graph generation process is collected, and the APK file is deleted from the local system once the process is complete.
     *
     * @throws InterruptedException if the Thread.sleep() call is interrupted.
     * @throws IOException          if there are issues deleting the APK file or other IO related operations.
     * @throws ExecutionException   if an exception was thrown inside of the Callable task.
     * @throws TimeoutException     if the call graph building process exceeds the 10-minute limit.
     */
    public void run() {
        String redisSrv = CommandLineOptions.v().getOptionValue("redis-srv");
        String redisPwd = CommandLineOptions.v().getOptionValue("redis-pwd");
        String redisPort = CommandLineOptions.v().getOptionValue("redis-port");
        String redisRoot = CommandLineOptions.v().getOptionValue("redis-root");
        String redisSpop = String.format("%s:pop", redisRoot);
        String redisSuccess = String.format("%s:success", redisRoot);

        RedisManager rm = new RedisManager(redisSrv, redisPort, redisPwd);

        List<String> algos = this.getAlgos();
        DataCollector dc = new DataCollector(rm);

        try {
            while (true) {
                String pop = rm.spop(redisSpop);
                if (pop == null) {
                    Writer.v().pwarning("No SHA received, sleeping for 30 sec...");
                    Thread.sleep(30000);
                    continue;
                }
                Writer.v().psuccess(String.format("SHA well received: %s", pop));
                String apkPath = this.getApkPath(pop);

                Writer.v().pinfo("Initializing Environment");
                this.initEnv(apkPath);

                for (String algo : algos) {
                    String successString = String.format("%s-%s", pop, algo);
                    if (rm.sismember(redisSuccess, successString)) {
                        continue;
                    }
                    final CallGraph[] cg = {null};
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Future<String> future = executor.submit(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            Writer.v().pinfo(String.format("Processing %s call graph algorithm", algo));
                            buildCallGraph(algo, pop);
                            cg[0] = Scene.v().getCallGraph();
                            Writer.v().psuccess("Call graph built");
                            return "Task completed successfully";
                        }
                    });
                    try {
                        String result = future.get(60, TimeUnit.MINUTES);
                        dc.collect(result, pop, algo, cg[0]);
                    } catch (TimeoutException e) {
                        Writer.v().perror("Timeout reached");
                    } catch (ExecutionException e) {
                        Writer.v().perror(String.format("An exception occurred within the task: %s", e.getMessage()));
                    }
                }
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

    /**
     * Initializes the environment needed to process the APK file.
     *
     * @param apkPath the path to the APK file that will be processed.
     */
    protected abstract void initEnv(String apkPath);

    /**
     * Builds the call graph for a given APK file using the specified algorithm.
     *
     * @param algo    the name of the algorithm to be used in call graph construction.
     * @param appName the name of the application being analyzed.
     */
    protected abstract void buildCallGraph(String algo, String appName);

    /**
     * Returns the APK file path for the given SHA value.
     *
     * @param pop the SHA value used to locate the APK file.
     * @return the APK file path.
     */
    protected abstract String getApkPath(String pop);

    /**
     * Returns a list of the call graph construction algorithms to be used in the analysis.
     *
     * @return a list of algorithm names.
     */
    protected abstract List<String> getAlgos();
}