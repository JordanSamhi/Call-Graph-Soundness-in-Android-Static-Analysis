package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.FlowdroidUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import soot.jimple.infoflow.android.SetupApplication;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * `MethodsExtractorICCTA` is a subclass of `MethodsExtractorBase` that provides specific implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses the ICCTA (Inter-Component Communication) approach for method extraction and call graph construction.
 * It uses both FlowdroidUtils and AndroZooUtils, fetching APKs from AndroZoo and initializing the environment
 * with Flowdroid.
 * <p>
 * During call graph construction, it searches for an ICC (Inter-Component Communication) model associated with the application.
 * If such a model exists, it is used during the call graph generation.
 * <p>
 * This class supports multiple algorithms for call graph construction: CHA, RTA, VTA, and SPARK.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorICCTA extends MethodsExtractorBase {

    private FlowdroidUtils fu;
    private AndroZooUtils au;

    public MethodsExtractorICCTA() {
        String apikey = CommandLineOptions.v().getOptionValue("apikey");
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        this.fu = new FlowdroidUtils(apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
        SetupApplication sa = fu.initializeFlowdroid(platformsPath, null, algo);

        File model = new File(String.format("./icc_models/%s.txt", appName));
        if (model.exists()) {
            sa.getConfig().getIccConfig().setIccModel(model.getAbsolutePath());
        }
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