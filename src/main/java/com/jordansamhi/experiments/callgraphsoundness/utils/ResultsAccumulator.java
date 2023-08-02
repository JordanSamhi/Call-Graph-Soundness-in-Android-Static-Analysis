package com.jordansamhi.experiments.callgraphsoundness.utils;


public class ResultsAccumulator {

    private static ResultsAccumulator instance;

    private String appName;
    private int numMethodsInApp;
    private int numNonLibraryMethodsInApp;
    private int numMethodsInCallGraph;
    private int numMethodsNonLibraryInCallGraph;
    private int numStmtInApp;
    private int numNonLibraryStmtInApp;
    private int numStmtInCallGraph;
    private int numStmtNonLibraryInCallGraph;
    private int numUnusedMethods;
    private int numUnusedNonLibraryMethods;
    private int numUnusedStmt;
    private int numUnusedNonLibraryStmt;
    private int numEdgesInCallGraph;
    private int numEdgesInCallGraphToNonLibrary;

    private String cgAlgo;

    private ResultsAccumulator() {
        this.setAppName("");
    }

    public static ResultsAccumulator v() {
        if (instance == null) {
            instance = new ResultsAccumulator();
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void printVectorResults() {
        System.out.println(this.generateVector());
    }

    public String getVectorResults() {
        return this.generateVector();
    }

    private String generateVector() {
        return String.format("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", this.getAppName(), this.getCgAlgo(),
                this.getNumMethodsInApp(), this.getNumNonLibraryMethodsInApp(), this.getNumMethodsInCallGraph(),
                this.getNumStmtInApp(), this.getNumNonLibraryStmtInApp(), this.getNumStmtInCallGraph(),
                this.getNumUnusedMethods(), this.getNumUnusedNonLibraryMethods(),
                this.getNumUnusedStmt(), this.getNumUnusedNonLibraryStmt(), this.getNumMethodsNonLibraryInCallGraph(),
                this.getNumStmtNonLibraryInCallGraph(), this.getNumEdgesInCallGraph(), this.getNumEdgesInCallGraphToNonLibrary());
    }

    public void printResults() {
        System.out.println("Results:");
        System.out.printf(" - App name: %s%n", this.getAppName());
        System.out.printf(" - Call graph algorithm: %s%n", this.getCgAlgo());
        System.out.printf(" - Total number of methods: %s%n", this.getNumMethodsInApp());
        System.out.printf(" - Total number of non-library methods: %s%n", this.getNumNonLibraryMethodsInApp());
        System.out.printf(" - Total number of methods in Call graph: %s%n", this.getNumMethodsInCallGraph());
        System.out.printf(" - Total number of methods non-library in Call graph: %s%n", this.getNumMethodsNonLibraryInCallGraph());
        System.out.printf(" - Total number of statements: %s%n", this.getNumStmtInApp());
        System.out.printf(" - Total number of non-library statements: %s%n", this.getNumNonLibraryStmtInApp());
        System.out.printf(" - Total number of statements in Call graph: %s%n", this.getNumStmtInCallGraph());
        System.out.printf(" - Total number of statements non-library in Call graph: %s%n", this.getNumStmtNonLibraryInCallGraph());
        System.out.printf(" - Total number of unused methods: %s%n", this.getNumUnusedMethods());
        System.out.printf(" - Total number of unused non-library methods: %s%n", this.getNumUnusedNonLibraryMethods());
        System.out.printf(" - Total number of unused statements: %s%n", this.getNumUnusedStmt());
        System.out.printf(" - Total number of unused non-library statements: %s%n", this.getNumUnusedNonLibraryStmt());
        System.out.printf(" - Total number of Edges in the Call Graph: %s%n", this.getNumEdgesInCallGraph());
        System.out.printf(" - Total number of Edges in the Call Graph Non Library: %s%n", this.getNumEdgesInCallGraphToNonLibrary());
    }

    public int getNumMethodsInApp() {
        return numMethodsInApp;
    }

    public void setNumMethodsInApp(int numMethodsInApp) {
        this.numMethodsInApp = numMethodsInApp;
    }

    public int getNumNonLibraryMethodsInApp() {
        return numNonLibraryMethodsInApp;
    }

    public void setNumNonLibraryMethodsInApp(int numNonLibraryMethodsInApp) {
        this.numNonLibraryMethodsInApp = numNonLibraryMethodsInApp;
    }

    public int getNumMethodsInCallGraph() {
        return numMethodsInCallGraph;
    }

    public int getNumMethodsNonLibraryInCallGraph() {
        return numMethodsNonLibraryInCallGraph;
    }

    public void setNumMethodsInCallGraph(int numMethodsInCallGraph) {
        this.numMethodsInCallGraph = numMethodsInCallGraph;
    }

    public void setNumMethodsNonLibraryInCallGraph(int numMethodsNonLibraryInCallGraph) {
        this.numMethodsNonLibraryInCallGraph = numMethodsNonLibraryInCallGraph;
    }

    public int getNumStmtInApp() {
        return numStmtInApp;
    }

    public void setNumStmtInApp(int numStmtInApp) {
        this.numStmtInApp = numStmtInApp;
    }

    public int getNumNonLibraryStmtInApp() {
        return numNonLibraryStmtInApp;
    }

    public void setNumNonLibraryStmtInApp(int numNonLibraryStmtInApp) {
        this.numNonLibraryStmtInApp = numNonLibraryStmtInApp;
    }

    public int getNumStmtInCallGraph() {
        return numStmtInCallGraph;
    }

    public int getNumStmtNonLibraryInCallGraph() {
        return numStmtNonLibraryInCallGraph;
    }

    public void setNumStmtInCallGraph(int numStmtInCallGraph) {
        this.numStmtInCallGraph = numStmtInCallGraph;
    }

    public void setNumStmtNonLibraryInCallGraph(int numStmtNonLibraryInCallGraph) {
        this.numStmtNonLibraryInCallGraph = numStmtNonLibraryInCallGraph;
    }

    public int getNumUnusedMethods() {
        return numUnusedMethods;
    }

    public void setNumUnusedMethods(int numUnusedMethods) {
        this.numUnusedMethods = numUnusedMethods;
    }

    public int getNumUnusedNonLibraryMethods() {
        return numUnusedNonLibraryMethods;
    }

    public void setNumUnusedNonLibraryMethods(int numUnusedNonLibraryMethods) {
        this.numUnusedNonLibraryMethods = numUnusedNonLibraryMethods;
    }

    public int getNumUnusedStmt() {
        return numUnusedStmt;
    }

    public void setNumUnusedStmt(int numUnusedStmt) {
        this.numUnusedStmt = numUnusedStmt;
    }

    public int getNumUnusedNonLibraryStmt() {
        return numUnusedNonLibraryStmt;
    }

    public void setNumUnusedNonLibraryStmt(int numUnusedNonLibraryStmt) {
        this.numUnusedNonLibraryStmt = numUnusedNonLibraryStmt;
    }

    public String getCgAlgo() {
        return cgAlgo;
    }

    public void setCgAlgo(String cgAlgo) {
        this.cgAlgo = cgAlgo;
    }

    public int getNumEdgesInCallGraph() {
        return numEdgesInCallGraph;
    }

    public void setNumEdgesInCallGraph(int numEdgesInCallGraph) {
        this.numEdgesInCallGraph = numEdgesInCallGraph;
    }

    public int getNumEdgesInCallGraphToNonLibrary() {
        return numEdgesInCallGraphToNonLibrary;
    }

    public void setNumEdgesInCallGraphToNonLibrary(int numEdgesInCallGraphToNonLibrary) {
        this.numEdgesInCallGraphToNonLibrary = numEdgesInCallGraphToNonLibrary;
    }
}