package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.natidroid.JavaLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodsExtractorNatiDroid extends MethodsExtractorBase {

    private AndroZooUtils au;

    public MethodsExtractorNatiDroid() {
        String apikey = CommandLineOptions.v().getOptionValue("apikey");
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        soot.G.reset();
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
        String mainClassStrLast = "SensorNotificationService";
        String mainClassStr = "com.android.server." + mainClassStrLast;
        ArrayList<String> starts_call = new ArrayList<String>();
        starts_call.add("onLocationChanged(");
        ArrayList<String> supportClassList = new ArrayList<String>();
        JavaLink.init(mainClassStrLast, mainClassStr, 1, starts_call, supportClassList, platformsPath, apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        JavaLink.buildCallGraph();
    }

    @Override
    protected String getApkPath(String pop) {
        return this.au.getApk(pop);
    }

    @Override
    protected List<String> getAlgos() {
        return Collections.singletonList("CHA");
    }
}