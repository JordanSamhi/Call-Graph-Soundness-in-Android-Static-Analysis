package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.FlowdroidUtils;
import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;
import soot.jimple.infoflow.android.SetupApplication;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * `MethodsExtractorRAICC` is a subclass of `MethodsExtractorBase` that provides specific implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses the RAICC technique for method extraction and call graph construction. The environment is
 * initialized with a new FlowdroidUtils instance based on the APK path.
 * <p>
 * The APK paths are obtained from the `apps_instrumented` directory based on the SHA value. For call graph construction,
 * the class uses CHA, RTA, VTA and SPARK algorithms.
 * <p>
 * It uses an ICC (Inter-Component Communication) model if available. The ICC model file is determined based on the application name.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorRAICC extends MethodsExtractorBase {

    private FlowdroidUtils fu;

    @Override
    protected void initEnv(String apkPath) {
        this.fu = new FlowdroidUtils(apkPath);
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        String platformsPath = CommandLineOptionsMethodsExtractor.v().getPlatforms();
        SetupApplication sa = fu.initializeFlowdroid(platformsPath, null, algo, false);

        File model = new File(String.format("./icc_models/%s.txt", appName));
        if (model.exists()) {
            sa.getConfig().getIccConfig().setIccModel(model.getAbsolutePath());
        }
    }

    @Override
    protected String getApkPath(String pop) {
        return String.format("./apps_instrumented/%s.apk", pop);
    }

    @Override
    protected List<String> getAlgos() {
        return Arrays.asList("CHA", "RTA", "VTA", "SPARK");
    }
}