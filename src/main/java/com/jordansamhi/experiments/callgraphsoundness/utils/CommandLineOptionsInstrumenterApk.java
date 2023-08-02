package com.jordansamhi.experiments.callgraphsoundness.utils;

import com.jordansamhi.experiments.utils.Constants;
import org.apache.commons.cli.*;
import org.javatuples.Triplet;

/**
 * This class sets the different option for the application
 *
 * @author Jordan Samhi
 */
public class CommandLineOptionsInstrumenterApk {

    private static final Triplet<String, String, String> PLATFORMS = new Triplet<>("platforms", "p", "Platform file");
    private static final Triplet<String, String, String> APK_PATH = new Triplet<>("apk", "a", "Apk file");
    private static final Triplet<String, String, String> OUTPUT_PATH = new Triplet<>("output", "o", "Output path");
    private static final Triplet<String, String, String> HELP = new Triplet<>("help", "h", "Print this message");

    private final Options options;
    private final Options firstOptions;
    private final CommandLineParser parser;
    private CommandLine cmdLine;

    private static CommandLineOptionsInstrumenterApk instance;

    public CommandLineOptionsInstrumenterApk() {
        this.options = new Options();
        this.firstOptions = new Options();
        this.initOptions();
        this.parser = new DefaultParser();
    }

    public static CommandLineOptionsInstrumenterApk v() {
        if (instance == null) {
            instance = new CommandLineOptionsInstrumenterApk();
        }
        return instance;
    }

    public void parseArgs(String[] args) {
        this.parse(args);
    }

    /**
     * This method does the parsing of the arguments.
     * It distinguished, real options and help option.
     *
     * @param args the arguments of the application
     */
    private void parse(String[] args) {
        HelpFormatter formatter;
        try {
            CommandLine cmdFirstLine = this.parser.parse(this.firstOptions, args, true);
            if (cmdFirstLine.hasOption(HELP.getValue0())) {
                formatter = new HelpFormatter();
                formatter.printHelp(Constants.TOOL_NAME, this.options, true);
                System.exit(0);
            }
            this.cmdLine = this.parser.parse(this.options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Initialization of all recognized options
     */
    private void initOptions() {
        final Option platform = Option.builder(PLATFORMS.getValue1())
                .longOpt(PLATFORMS.getValue0())
                .desc(PLATFORMS.getValue2())
                .hasArg(true)
                .argName(PLATFORMS.getValue0())
                .required(true)
                .build();

        final Option output = Option.builder(OUTPUT_PATH.getValue1())
                .longOpt(OUTPUT_PATH.getValue0())
                .desc(OUTPUT_PATH.getValue2())
                .hasArg(true)
                .argName(OUTPUT_PATH.getValue0())
                .required(true)
                .build();

        final Option apkPath = Option.builder(APK_PATH.getValue1())
                .longOpt(APK_PATH.getValue0())
                .desc(APK_PATH.getValue2())
                .hasArg(true)
                .argName(APK_PATH.getValue0())
                .required(true)
                .build();

        final Option help = Option.builder(HELP.getValue1())
                .longOpt(HELP.getValue0())
                .desc(HELP.getValue2())
                .argName(HELP.getValue0())
                .build();

        this.firstOptions.addOption(help);

        this.options.addOption(platform);
        this.options.addOption(apkPath);
        this.options.addOption(output);

        for (Option o : this.firstOptions.getOptions()) {
            this.options.addOption(o);
        }
    }

    public String getPlatforms() {
        return cmdLine.getOptionValue(PLATFORMS.getValue0());
    }

    public String getOutput() {
        return cmdLine.getOptionValue(OUTPUT_PATH.getValue0());
    }

    public String getApk() {
        return cmdLine.getOptionValue(APK_PATH.getValue0());
    }
}
