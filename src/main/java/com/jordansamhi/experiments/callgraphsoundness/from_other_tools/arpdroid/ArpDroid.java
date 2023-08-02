package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.arpdroid;

import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.jimple.infoflow.android.SetupApplication;
import soot.options.Options;

import java.util.Collections;

public class ArpDroid {

    private static SetupApplication analyzer;

    public static void init(String platforms, String apkPath) {
        G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_validate(true);
        Options.v().set_output_format(Options.output_format_dex);
        Options.v().set_process_dir(Collections.singletonList(apkPath));
        Options.v().set_android_jars(platforms);
        Options.v().set_src_prec(Options.src_prec_apk);
        Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);
        Scene.v().addBasicClass("android.app.Activity", SootClass.BODIES);
        Scene.v().addBasicClass("android.app.Service", SootClass.HIERARCHY);
        analyzer = new SetupApplication(platforms, apkPath);
        analyzer.getConfig().setTaintAnalysisEnabled(false);
        analyzer.setCallbackFile(ArpDroid.class.getClassLoader().getResource("AndroidCallbacks.txt").getFile());
        Scene.v().loadNecessaryClasses();
    }

    public static void buildCallGraph() {
        ArpDroid.analyzer.constructCallgraph();
    }
}
