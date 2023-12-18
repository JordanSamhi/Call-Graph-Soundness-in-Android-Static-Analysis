package com.jordansamhi.experiments.callgraphsoundness.utils;

import com.jordansamhi.androspecter.SootUtils;
import com.jordansamhi.androspecter.network.RedisManager;
import com.jordansamhi.androspecter.printers.Writer;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import static com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions.v;

public class DataCollector {

    RedisManager rm;

    public DataCollector(RedisManager rm) {
        this.rm = rm;
    }

    public void collect(String result, String appName, String algo, CallGraph cg) {
        if (result.equals("Task completed successfully")) {
            System.out.println("HERE");
            String redisRoot = v().getOptionValue("redis-root");
            String redisSuccess = String.format("%s:success", redisRoot);
            String listMethod = String.format("%s:methods", redisRoot);
            String listCG = String.format("%s:callgraph", redisRoot);

            SootUtils su = new SootUtils();
            Set<SootMethod> allMethods = su.getAllMethods();

            Map<SootMethod, List<SootMethod>> adjacencyList = new HashMap<>();
            for (Edge e : cg) {
                if (e != null) {
                    SootMethod srcMethod = e.src();
                    SootMethod tgtMethod = e.tgt();
                    adjacencyList.putIfAbsent(srcMethod, new ArrayList<>());
                    adjacencyList.get(srcMethod).add(tgtMethod);
                }
            }

            String adjacencyListStr = adjacencyList.entrySet().stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .map(entry -> entry.getKey().toString() + "->" +
                            entry.getValue().stream()
                                    .filter(Objects::nonNull)
                                    .map(SootMethod::toString)
                                    .collect(Collectors.joining("|")))
                    .collect(Collectors.joining("@"));

            String allMethodsStrToSend = String.format("%s;%s;%s", appName, algo, allMethods.stream().map(e -> String.format("%s", e)).collect(Collectors.joining("|")));
            String adjacencyListToSend = String.format("%s;%s;%s", appName, algo, adjacencyListStr);

            Writer.v().pinfo("Sending results to Redis server");

            byte[] compressedAdjacencyList = compress(adjacencyListToSend);
            byte[] compressedAllMethods = compress(allMethodsStrToSend);

            String base64AdjacencyList = Base64.getEncoder().encodeToString(compressedAdjacencyList);
            String base64AllMethods = Base64.getEncoder().encodeToString(compressedAllMethods);

            rm.sadd(listMethod, base64AllMethods);
            rm.sadd(listCG, base64AdjacencyList);

            String successString = String.format("%s-%s", appName, algo);
            rm.sadd(redisSuccess, successString);
            Writer.v().psuccess("Data successfully sent to Redis server");
            Writer.v().psuccess(result);
        }
    }


    public byte[] compress(String str) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

