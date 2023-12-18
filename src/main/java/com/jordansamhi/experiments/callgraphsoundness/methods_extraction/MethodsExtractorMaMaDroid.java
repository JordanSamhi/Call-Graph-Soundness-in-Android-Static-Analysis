package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.AndroZooUtils;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;
import soot.PackManager;
import soot.Scene;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.options.Options;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * `MethodsExtractorMaMaDroid` is a subclass of `MethodsExtractorBase` that provides specific implementations
 * for initializing the environment, building a call graph, retrieving APK paths, and listing call graph construction algorithms.
 * <p>
 * This class uses the MaMaDroid approach for method extraction and call graph construction. It employs the Soot framework for Java optimization
 * and defines specific package inclusions and exclusions for the Soot setup.
 * <p>
 * The APK paths are fetched from AndroZoo, and the environment is initialized by resetting the Soot environment and setting up FlowdroidUtils.
 * The call graph construction uses the SPARK algorithm and includes specific configuration settings related to access paths,
 * static field tracking, and phase options for various components.
 *
 * @author Jordan Samhi
 * @see MethodsExtractorBase
 */
public class MethodsExtractorMaMaDroid extends MethodsExtractorBase {

    private AndroZooUtils au;
    private String apkPath;

    public MethodsExtractorMaMaDroid() {
        String apikey = CommandLineOptions.v().getOptionValue("apikey");
        this.au = new AndroZooUtils(apikey);
    }

    @Override
    protected void initEnv(String apkPath) {
        soot.G.reset();
    }

    @Override
    protected void buildCallGraph(String algo, String appName) {
        List<String> toInclude = Arrays.asList("java.", "android.", "org.", "com.", "javax.");
        List<String> toExclude = Collections.singletonList("soot.");
        String platformsPath = CommandLineOptions.v().getOptionValue("platforms");
        InfoflowAndroidConfiguration ifac = new InfoflowAndroidConfiguration();
        URL resource = MethodsExtractorMaMaDroid.class.getClassLoader().getResource("SourcesAndSinks.txt");
        String sourcesAndSinksFilePath = Paths.get(resource.getPath()).toString();
        ifac.getAnalysisFileConfig().setSourceSinkFile(sourcesAndSinksFilePath);
        ifac.getAnalysisFileConfig().setTargetAPKFile(apkPath);
        ifac.getAnalysisFileConfig().setAndroidPlatformDir(platformsPath);
        ifac.setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        SetupApplication app = new SetupApplication(ifac);
        app.getConfig().setStaticFieldTrackingMode(InfoflowConfiguration.StaticFieldTrackingMode.None);
        app.getConfig().getAccessPathConfiguration().setAccessPathLength(1);
        app.getConfig().setFlowSensitiveAliasing(false);
        app.getConfig().setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);

        PackManager.v().getPack("cg");
        PackManager.v().getPack("jb");
        PackManager.v().getPack("wjap.cgg");
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(apkPath));
        Options.v().set_android_jars(platformsPath);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_app(true);
        Options.v().set_include(toInclude);
        Options.v().set_exclude(toExclude);
        Options.v().set_output_format(Options.output_format_xml);
        Options.v().setPhaseOption("cg", "safe-newinstance:true");
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("wjap.cgg", "show-lib-meths:true");
        Options.v().setPhaseOption("jb", "use-original-names:true");
        Scene.v().loadNecessaryClasses();
        app.constructCallgraph();
        Options.v().set_main_class(app.getDummyMainMethod().getSignature());
        Scene.v().setEntryPoints(Collections.singletonList(app.getDummyMainMethod()));
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