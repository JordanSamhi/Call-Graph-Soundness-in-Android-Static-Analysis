package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.Jicer;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.Parameters;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.config.Config;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MethodsExtractorJicer extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;
    com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.Jicer jicer;

    public MethodsExtractorJicer() {
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        this.jicer = new Jicer();
        Parameters.getInstance().setInputApkFile(new File(apkPath));
        Config.getInstance().platformsPath = platformsPath;
        this.jicer.init(platformsPath, apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        this.jicer.buildCallGraph();
    }

    @Override
    protected String getApkPath(String pop) {
        this.apkPath = this.au.getApk(pop);
        return this.apkPath;
    }

    @Override
    protected List<String> getAlgos() {
        return Collections.singletonList("SPARK");
    }
}