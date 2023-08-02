package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.FlowdroidUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

import java.util.Arrays;
import java.util.List;

/**
 * `MethodsExtractorDroidRA` is a subclass of `MethodsExtractorBase` that provides specific implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses the DroidRA technique for method extraction and call graph construction. The environment is
 * initialized with the Flowdroid utility, which is later used to build the call graph. APK paths are determined based
 * on SHA values and a pre-determined structure.
 * <p>
 * The class uses four types of call graph construction algorithms: CHA, RTA, VTA, and SPARK.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorDroidRA extends MethodsExtractorBase {

    private FlowdroidUtils fu;

    @Override
    protected void initEnv(String apkPath) {
        this.fu = new FlowdroidUtils(apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        fu.initializeFlowdroid(platformsPath, null, algo, false);
    }

    @Override
    protected String getApkPath(String pop) {
        return String.format("./apps_instrumented/%s.apk", pop);
    }

    @Override
    protected List<String> getAlgos() {
        return Arrays.asList("CHA", "RTA", "VTA", "SPARK");
    }
}