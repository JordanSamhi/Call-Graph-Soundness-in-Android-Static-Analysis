package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;
import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.sootfx.SootConnector;
import soot.PackManager;

import java.util.Collections;
import java.util.List;

/**
 * `MethodsExtractorSootFX` is a subclass of `MethodsExtractorBase` that provides specific implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses the SootFX technique for method extraction and call graph construction. The environment is
 * initialized by resetting the Soot global state. APK paths are retrieved using AndroZooUtils which are determined based
 * on SHA values and an API key.
 * <p>
 * The class uses the SPARK algorithm for call graph construction. The process of building a call graph is done using
 * the SootConnector to setup Soot and then running Soot packs.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorSootFX extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;

    public MethodsExtractorSootFX() {
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
        SootConnector.setupSoot(null, this.apkPath, false, platformsPath);
        try {
            PackManager.v().runPacks();
        } catch (Exception f) {
            f.printStackTrace();
        }
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