package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.experiments.callgraphsoundness.utils.CommandLineOptionsMethodsExtractor;

/**
 * The `MethodsExtractorMain` class provides an entry point for the APK methods extraction process.
 * The process involves the use of various tools such as Flowdroid, ICCTA, RAICC, DroidRA, MaMaDroid, and SootFX.
 * <p>
 * The main method of this class initiates the process by parsing the command line options to determine
 * the specific tool to be used for the extraction. Depending on the tool specified, an appropriate
 * instance of `MethodsExtractorBase` is created and its `run` method is invoked.
 * <p>
 * If the specified tool is not recognized, an `IllegalArgumentException` is thrown.
 *
 * @author Jordan Samhi
 * @see CommandLineOptionsMethodsExtractor
 * @see MethodsExtractorBase
 * @see MethodsExtractorFlowdroid
 * @see MethodsExtractorICCTA
 * @see MethodsExtractorRAICC
 * @see MethodsExtractorDroidRA
 * @see MethodsExtractorMaMaDroid
 * @see MethodsExtractorSootFX
 */
public class MethodsExtractorMain {
    public static void main(String[] args) {
        CommandLineOptionsMethodsExtractor.v().parseArgs(args);
        String tool = CommandLineOptionsMethodsExtractor.v().getTool();

        MethodsExtractorBase meb;

        switch (tool) {
            case "flowdroid":
                meb = new MethodsExtractorFlowdroid();
                break;
            case "iccta":
                meb = new MethodsExtractorICCTA();
                break;
            case "raicc":
                meb = new MethodsExtractorRAICC();
                break;
            case "droidra":
                meb = new MethodsExtractorDroidRA();
                break;
            case "mamadroid":
                meb = new MethodsExtractorMaMaDroid();
                break;
            case "backdroid":
                meb = new MethodsExtractorBackdroid();
                break;
            case "sootfx":
                meb = new MethodsExtractorSootFX();
                break;
            case "natidroid":
                meb = new MethodsExtractorNatiDroid();
                break;
            case "gator":
                meb = new MethodsExtractorGator();
                break;
            case "arpdroid":
                meb = new MethodsExtractorArpDroid();
                break;
            case "acid":
                meb = new MethodsExtractorACID();
                break;
            case "difuzer":
                meb = new MethodsExtractorDifuzer();
                break;
            case "jicer":
                meb = new com.jordansamhi.experiments.callgraphsoundness.methods_extraction.MethodsExtractorJicer();
                break;
            default:
                throw new IllegalArgumentException("Invalid tool: " + tool);
        }
        meb.run();
    }
}