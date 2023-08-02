package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.sootfx;

import soot.G;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SootConnector {

    public static void setupSoot(String mainClass, String classPaths, boolean appOnly, String androidJars) {
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_include_all(true);
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
//        Options.v().set_soot_classpath(System.getProperty("java.home") + "/lib/rt.jar");
        Options.v().set_src_prec(Options.src_prec_class);
        List<String> cps = new ArrayList<>();
        cps.add(classPaths);
        Options.v().set_process_dir(cps);
        Options.v().set_keep_line_number(true);
        // set spark options for construct call graphs
        Options.v().setPhaseOption("cg.spark", "on");
        Options.v().setPhaseOption("cg.spark", "string-constants:true");
        Options.v().set_app(appOnly);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_android_jars(androidJars);
        Options.v().set_whole_program(true);
        if (mainClass != null) {
            Options.v().set_main_class(mainClass);
        }
        Scene.v().loadNecessaryClasses();
        //SootMethod mainMethod = Scene.v().getMainMethod();
        //Scene.v().setEntryPoints(Collections.singletonList(mainMethod));

        HashMap opt = new HashMap();
        opt.put("enabled", "true");
        opt.put("vta", "true");
        //opt.put("apponly","true");
        //SparkTransformer.v().transform("", opt);
        CHATransformer.v().transform();
    }
}