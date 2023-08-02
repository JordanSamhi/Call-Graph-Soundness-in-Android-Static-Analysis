package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.arpdroid.ArpDroid;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorArpDroid extends MethodsExtractorBase {

    private AndroZooUtils au;

    public MethodsExtractorArpDroid() {
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        ArpDroid.init(platformsPath, apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        ArpDroid.buildCallGraph();
    }

    @Override
    protected String getApkPath(String pop) {
        return this.au.getApk(pop);
    }

    @Override
    protected List<String> getAlgos() {
        return Collections.singletonList("SPARK");
    }
}