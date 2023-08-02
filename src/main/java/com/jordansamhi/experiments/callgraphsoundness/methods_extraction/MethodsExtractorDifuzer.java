package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorDifuzer extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;

    public MethodsExtractorDifuzer() {
        String apikey = CommandLineOptionsMethodsExtractor.v().getAndroZooApiKey();
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        soot.G.reset();
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        InfoflowAndroidConfiguration ifac = new InfoflowAndroidConfiguration();
        ifac.setIgnoreFlowsInSystemPackages(false);
        ifac.getAnalysisFileConfig().setAndroidPlatformDir(platformsPath);
        ifac.getAnalysisFileConfig().setTargetAPKFile(this.apkPath);
        ifac.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        SetupApplication sa = new SetupApplication(ifac);
        sa.getConfig().setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.CHA);
        sa.constructCallgraph();
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