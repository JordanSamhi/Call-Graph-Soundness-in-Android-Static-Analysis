package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.FlowdroidUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

import java.util.Arrays;
import java.util.List;

/**
 * `MethodsExtractorFlowdroid` is a subclass of `MethodsExtractorBase` that provides implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses FlowDroid for method extraction and call graph construction, fetching APKs from AndroZoo.
 * <p>
 * FlowDroid is a static taint analysis tool for Android apps. An important step in static taint analysis is the
 * construction of a call graph. This class supports multiple algorithms for this purpose, namely: CHA, RTA, VTA, and SPARK.
 * <p>
 * On initialization, this class prepares a `FlowdroidUtils` instance for the provided APK path. For building the
 * call graph, it initializes FlowDroid with a platform path and a call graph construction algorithm.
 * <p>
 * The APK paths are retrieved from AndroZoo, a large database of Android applications.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorFlowdroid extends MethodsExtractorBase {

    private FlowdroidUtils fu;
    private AndroZooUtils au;

    public MethodsExtractorFlowdroid() {
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        this.fu = new FlowdroidUtils(apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        this.fu.initializeFlowdroid(platformsPath, null, algo, false);
    }

    @Override
    protected String getApkPath(String pop) {
        return this.au.getApk(pop);
    }

    @Override
    protected List<String> getAlgos() {
        return Arrays.asList("CHA", "RTA", "VTA", "SPARK");
    }
}