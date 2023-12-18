package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.arpdroid.ArpDroid;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorArpDroid extends MethodsExtractorBase {

    private AndroZooUtils au;

    public MethodsExtractorArpDroid() {
        String apikey = CommandLineOptions.v().getOptionValue("apikey");
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
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