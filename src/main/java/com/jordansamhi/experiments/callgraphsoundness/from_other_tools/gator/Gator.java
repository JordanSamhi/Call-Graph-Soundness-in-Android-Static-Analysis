/*
 * Main.java - part of the GATOR project
 *
 * Copyright (c) 2018 The Ohio State University
 *
 * This file is distributed under the terms described in LICENSE in the
 * root directory.
 */
package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.gator;

import soot.PackManager;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class for SootAndroid.
 */
public class Gator {

    public static void main(String[] args) {
        String platforms = "";
        String apkt_path = "";
        Gator.setupAndInvokeSoot(platforms, apkt_path);
        Gator.buildCallGraph();
        CallGraph cg = Scene.v().getCallGraph();
    }

    public static void setupAndInvokeSoot(String android_jar, String apk_path) {
        Options.v().set_whole_program(true);
        Options.v().setPhaseOption("cg", "all-reachable:true");
        Options.v().setPhaseOption("cg.cha", "enabled:true");
        Options.v().setPhaseOption("wjtp.gui", "enabled:true");
        Options.v().set_output_format(Options.output_format_n);
        Options.v().set_keep_line_number(true);
        Options.v().set_process_multiple_dex(true);
        Options.v().set_allow_phantom_refs(true);
        List<String> apks = new ArrayList<>();
        apks.add(apk_path);
        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(apks);
        Options.v().set_android_jars(android_jar);
        Scene.v().loadNecessaryClasses();
    }

    public static void buildCallGraph() {
        PackManager.v().runPacks();
    }
}
