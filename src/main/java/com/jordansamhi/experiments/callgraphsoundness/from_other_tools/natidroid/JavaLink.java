package com.jordansamhi.experiments.callgraphsoundness.from_other_tools.natidroid;


import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.options.Options;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class JavaLink {

    static LinkedList<String> excludeList;
    static String mainClassStrLast = "ICameraService$Stub$Proxy";
    static String mainClassStr = "android.hardware." + mainClassStrLast;
    static ArrayList<String> starts_call = new ArrayList<String>();
    static SootClass mainClass = null;

    public static void main(String[] args) {
        String mainClassStrLast = "SensorNotificationService";
        String mainClassStr = "com.android.server." + mainClassStrLast;
        ArrayList<String> starts_call = new ArrayList<String>();
        starts_call.add("onLocationChanged(");
        ArrayList<String> supportClassList = new ArrayList<String>();
//        JavaLink.run(mainClassStrLast, mainClassStr, 1, starts_call, supportClassList);
    }

    public static void init(String mainClassStrLast, String mainClassStr, int mode, ArrayList<String> starts_call, ArrayList<String> supportClassList,
                            String platformsPath, String apkPath) {
        String mode1 = "wjtp.parent_call";
        String mode2 = "wjtp.permission_finder";
        String transform_str = mode1;
        if (mode == 1) {
            transform_str = mode1;
        } else if (mode == 2) {
            transform_str = mode2;
        }
        JavaLink.mainClassStrLast = mainClassStrLast;
        JavaLink.mainClassStr = mainClassStr;
        JavaLink.starts_call = starts_call;

        excludeJDKLibrary();

        Options.v().set_whole_program(true);
        Options.v().set_app(true);
        Options.v().set_validate(true);


        Options.v().set_src_prec(Options.src_prec_apk);
        List<String> apks = new ArrayList<>();
        apks.add(apkPath);
        Options.v().set_android_jars(platformsPath);
        Options.v().set_process_dir(apks);


        SootClass c = Scene.v().forceResolve(mainClassStr, SootClass.BODIES);
        for (String classTem : supportClassList) {
            Scene.v().forceResolve(classTem, SootClass.BODIES);
        }

        c.setApplicationClass();

        Scene.v().loadNecessaryClasses();


        //Set all entrypoint and find mainclass
        List<SootMethod> entryPoints = new ArrayList<SootMethod>();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (!sc.getName().contains("jdk.")) {

                if (sc.getName().equals(mainClassStr)) {
                    mainClass = sc;
                }

                entryPoints.addAll(sc.getMethods());
            }
        }

        if (mainClass == null) {
            System.err.println("The specified mainClass was not found");
            System.exit(0);
        }

        Scene.v().setEntryPoints(entryPoints);
    }

    public static void buildCallGraph() {
        //enable call graph
        enableCHACallGraph();

        PackManager.v().runPacks();

    }

    private static void excludeJDKLibrary() {
        Options.v().set_exclude(excludeList());
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
    }


    private static void enableCHACallGraph() {
        CHATransformer.v().transform();
    }

    private static LinkedList<String> excludeList() {
        if (excludeList == null) {
            excludeList = new LinkedList<String>();

            excludeList.add("java.");
            excludeList.add("javax.");
            excludeList.add("sun.");
            excludeList.add("sunw.");
            excludeList.add("com.sun.");
            excludeList.add("com.ibm.");
            excludeList.add("com.apple.");
            excludeList.add("apple.awt.");

            excludeList.add("android.os.Message");
            excludeList.add("android.os.Handler");

        }
        return excludeList;
    }
}

