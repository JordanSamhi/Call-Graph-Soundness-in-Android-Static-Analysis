package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;
import soot.Scene;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.options.Options;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorBackdroid extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;

    public MethodsExtractorBackdroid() {
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
        SetupApplication app = new SetupApplication(platformsPath, apkPath);
        app.getConfig().setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(apkPath));
        Options.v().set_android_jars(platformsPath);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_class);
        Options.v().setPhaseOption("cg.spark", "on");
        Scene.v().loadNecessaryClasses();
        app.constructCallgraph();
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