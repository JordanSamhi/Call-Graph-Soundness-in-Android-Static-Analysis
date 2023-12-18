package com.jordansamhi.experiments.callgraphsoundness.methods_extraction;

import com.jordansamhi.androspecter.commonlineoptions.CommandLineOption;
import com.jordansamhi.androspecter.commonlineoptions.CommandLineOptions;

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
        CommandLineOptions options = CommandLineOptions.v();
        options.setAppName("AndroLibZoo FlowDroid Experiment");
        options.addOption(new CommandLineOption("apikey", "a", "AndroZoo API key", true, true));
        options.addOption(new CommandLineOption("platforms", "p", "Platform file", true, true));
        options.addOption(new CommandLineOption("redis-srv", "s", "Sets the redis server address", true, true));
        options.addOption(new CommandLineOption("redis-port", "n", "Sets the redis port to connect to", true, true));
        options.addOption(new CommandLineOption("redis-pwd", "w", "Sets the redis password", true, true));
        options.addOption(new CommandLineOption("redis-root", "o", "Sets the redis root list/set", true, true));
        options.addOption(new CommandLineOption("tool", "t", "The tool to use", true, true));
        options.parseArgs(args);
        String tool = CommandLineOptions.v().getOptionValue("tool");

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
            default:
                throw new IllegalArgumentException("Invalid tool: " + tool);
        }
        meb.run();
    }
}