package com.jordansamhi.experiments.callgraphsoundness.utils;

import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.network.RedisManager;
import com.jordansamhi.androspecter.printers.Writer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DataCollector {

    RedisManager rm;

    public DataCollector(RedisManager rm) {
        this.rm = rm;
    }

    public void collect(String result, String appName, String algo, CallGraph cg) {
        if (result.equals("Task completed successfully")) {
            String redisRoot = CommandLineOptionsMethodsExtractor.v().getRedisRoot();
            String redisSuccess = String.format("%s:success", redisRoot);
            String listVector = String.format("%s:numbers", redisRoot);
            String listMethodsnotincallgraph = String.format("%s:methodsnotincallgraph", redisRoot);
            String listMethodsnotincallgraphNonLibrary = String.format("%s:methodsnotincallgraphNonLibrary", redisRoot);
            String listMethodsincallgraph = String.format("%s:methodsincallgraph", redisRoot);
            String listMethodsincallgraphNonLibrary = String.format("%s:methodsincallgraphNonLibrary", redisRoot);
            String listEdgesInCallGraph = String.format("%s:edgesInCallGraph", redisRoot);
            String listEdgesInCallGraphNonLibrary = String.format("%s:edgesInCallGraphNonLibrary", redisRoot);

            SootUtils su = new SootUtils();

            Set<SootMethod> allMethods = su.getAllMethods();
            Set<SootMethod> allNonLibraryMethods = su.getAllMethodsExceptLibraries();
            Set<SootMethod> methodsInCG = su.getMethodsInCallGraph(cg);
            Set<SootMethod> methodsInCGNonLibrary = su.getMethodsInCallGraphExceptLibraries(cg);

            Set<SootMethod> allMethodsNotUsed = new HashSet<>(allMethods);
            allMethodsNotUsed.removeAll(methodsInCG);
            Set<SootMethod> AllMethodsNonLibrariesNotUsed = new HashSet<>(allNonLibraryMethods);
            AllMethodsNonLibrariesNotUsed.removeAll(methodsInCG);

            ResultsAccumulator.v().setCgAlgo(algo);

            ResultsAccumulator.v().setNumMethodsInApp(allMethods.size());
            ResultsAccumulator.v().setNumNonLibraryMethodsInApp(allNonLibraryMethods.size());
            ResultsAccumulator.v().setNumMethodsInCallGraph(methodsInCG.size());
            ResultsAccumulator.v().setNumMethodsNonLibraryInCallGraph(methodsInCGNonLibrary.size());
            ResultsAccumulator.v().setNumStmtInApp(su.getNumberOfStmtInApp());
            ResultsAccumulator.v().setNumNonLibraryStmtInApp(su.getNumberOfStmt(allNonLibraryMethods));
            ResultsAccumulator.v().setNumStmtInCallGraph(su.getNumberOfStmt(methodsInCG));
            ResultsAccumulator.v().setNumStmtNonLibraryInCallGraph(su.getNumberOfStmt(methodsInCGNonLibrary));
            ResultsAccumulator.v().setNumUnusedMethods(allMethodsNotUsed.size());
            ResultsAccumulator.v().setNumUnusedNonLibraryMethods(AllMethodsNonLibrariesNotUsed.size());
            ResultsAccumulator.v().setNumUnusedStmt(su.getNumberOfStmt(allMethodsNotUsed));
            ResultsAccumulator.v().setNumUnusedNonLibraryStmt(su.getNumberOfStmt(AllMethodsNonLibrariesNotUsed));
            ResultsAccumulator.v().setNumEdgesInCallGraph(su.countEdgesInCallGraph(cg));
            ResultsAccumulator.v().setNumEdgesInCallGraphToNonLibrary(su.countEdgesWithNonLibraryTargets(cg));

            String vector = ResultsAccumulator.v().getVectorResults();

            String methodsnotincallgraph = String.format("%s;%s;%s", appName, algo, allMethodsNotUsed.stream().map(m -> String.format("%s", m.getSignature())).collect(Collectors.joining("|")));
            String methodsnotincallgraphNotLibrary = String.format("%s;%s;%s", appName, algo, AllMethodsNonLibrariesNotUsed.stream().map(m -> String.format("%s", m.getSignature())).collect(Collectors.joining("|")));
            String methodsincallgraph = String.format("%s;%s;%s", appName, algo, methodsInCG.stream().map(m -> String.format("%s", m.getSignature())).collect(Collectors.joining("|")));
            String methodsincallgraphNotLibrary = String.format("%s;%s;%s", appName, algo, methodsInCGNonLibrary.stream().map(m -> String.format("%s", m.getSignature())).collect(Collectors.joining("|")));

            Set<String> edges = new HashSet<>();
            Set<String> edgesNonLibraryTgt = new HashSet<>();
            SootMethod src;
            SootMethod tgt;
            SootClass parentClass;
            String edge_str;
            for (Edge e : cg) {
                tgt = e.tgt();
                src = e.src();
                if (tgt != null && src != null) {
                    edge_str = String.format("%s-->%s", src.method(), tgt.method());
                    edges.add(edge_str);
                    parentClass = tgt.getDeclaringClass();
                    if (su.getNonLibraryClasses().contains(parentClass)) {
                        edgesNonLibraryTgt.add(edge_str);
                    }
                }
            }

            String edgesInCg = String.format("%s;%s;%s", appName, algo, edges.stream().map(e -> String.format("%s", e)).collect(Collectors.joining("|")));
            String edgesInCgNL = String.format("%s;%s;%s", appName, algo, edgesNonLibraryTgt.stream().map(e -> String.format("%s", e)).collect(Collectors.joining("|")));

            Writer.v().pinfo("Sending results to Redis server");

            rm.sadd(listVector, vector);
            rm.sadd(listMethodsnotincallgraph, methodsnotincallgraph);
            rm.sadd(listMethodsnotincallgraphNonLibrary, methodsnotincallgraphNotLibrary);
            rm.sadd(listMethodsincallgraph, methodsincallgraph);
            rm.sadd(listMethodsincallgraphNonLibrary, methodsincallgraphNotLibrary);
            rm.sadd(listEdgesInCallGraph, edgesInCg);
            rm.sadd(listEdgesInCallGraphNonLibrary, edgesInCgNL);

            String successString = String.format("%s-%s", appName, algo);
            rm.sadd(redisSuccess, successString);
            Writer.v().psuccess("Data successfully sent to Redis server");
            Writer.v().psuccess(result);
        }
    }
}
