package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import soot.PackManager;
import soot.Scene;
import soot.options.Options;

import java.util.Collections;
import java.util.List;

public class MethodsExtractorACID extends MethodsExtractorBase {

    private AndroZooUtils au;

    public MethodsExtractorACID() {
        String apikey = CommandLineOptions.v().getOptionValue("apikey");
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        soot.G.reset();
        Options.v().set_process_dir(Collections.singletonList(apkPath));
        Options.v().set_whole_program(true);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
        Options.v().set_android_jars(platformsPath);
        Scene.v().loadNecessaryClasses();
        PackManager.v().runPacks();
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