package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer;

import com.jordansamhi.experiments.callgraphsoundness.from_other_tools.jicer.config.Config;
import soot.Scene;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        final Jicer jicer = new Jicer();
        String platforms = "";
        String apk_path = "";
        Parameters.getInstance().setInputApkFile(new File(apk_path));
        Config.getInstance().platformsPath = platforms;
        System.out.println("init...");
        jicer.init(platforms, apk_path);
        System.out.println("building cg...");
        jicer.buildCallGraph();
        CallGraph cg = Scene.v().getCallGraph();
        System.out.println(cg.size());
    }
}