package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.gator.Gator;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorGator extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;

    public MethodsExtractorGator() {
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        Gator.setupAndInvokeSoot(platformsPath, this.apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        Gator.buildCallGraph();
    }

    @Override
    protected String getApkPath(String pop) {
        this.apkPath = this.au.getApk(pop);
        return this.apkPath;
    }

    @Override
    protected List<String> getAlgos() {
        return Collections.singletonList("CHA");
    }
}