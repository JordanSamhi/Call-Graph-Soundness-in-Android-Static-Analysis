package com.jordansamhi.experiments.callgraphsoundness.utils;

import com.jordansamhi.experiments.utils.Constants;
import org.apache.commons.cli.*;
import org.javatuples.Triplet;

/**
 * This class sets the different option for the application
 *
 * @author Jordan Samhi
 */
public class CommandLineOptionsInstrumenterRedis {

    private static final Triplet<String, String, String> PLATFORMS = new Triplet<>("platforms", "p", "Platform file");
    private static final Triplet<String, String, String> APK_PATH = new Triplet<>("file", "a", "Apk file");
    private static final Triplet<String, String, String> OUTPUT_PATH = new Triplet<>("output", "o", "Output path");
    private static final Triplet<String, String, String> HELP = new Triplet<>("help", "h", "Print this message");
    private static final Triplet<String, String, String> ANDROZOO_API_KEY = new Triplet<>("apikey", "a", "AndroZoo API key");
    private static final Triplet<String, String, String> REDIS_SERVER = new Triplet<>("redis-srv", "s", "Sets the redis server address");
    private static final Triplet<String, String, String> REDIS_PORT = new Triplet<>("redis-port", "n", "Sets the redis port to connect to");
    private static final Triplet<String, String, String> REDIS_PWD = new Triplet<>("redis-pwd", "w", "Sets the redis password");
    private static final Triplet<String, String, String> REDIS_ROOT = new Triplet<>("redis-root", "r", "Sets the redis root list/set");

    private final Options options;
    private final Options firstOptions;
    private final CommandLineParser parser;
    private CommandLine cmdLine;

    private static CommandLineOptionsInstrumenterRedis instance;

    public CommandLineOptionsInstrumenterRedis() {
        this.options = new Options();
        this.firstOptions = new Options();
        this.initOptions();
        this.parser = new DefaultParser();
    }

    public static CommandLineOptionsInstrumenterRedis v() {
        if (instance == null) {
            instance = new CommandLineOptionsInstrumenterRedis();
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

        final Option redisServer = Option.builder(REDIS_SERVER.getValue1())
                .longOpt(REDIS_SERVER.getValue0())
                .desc(REDIS_SERVER.getValue2())
                .hasArg(true)
                .argName(REDIS_SERVER.getValue0())
                .required(true)
                .build();

        final Option redisPort = Option.builder(REDIS_PORT.getValue1())
                .longOpt(REDIS_PORT.getValue0())
                .desc(REDIS_PORT.getValue2())
                .hasArg(true)
                .argName(REDIS_PORT.getValue0())
                .required(true)
                .build();

        final Option redisPwd = Option.builder(REDIS_PWD.getValue1())
                .longOpt(REDIS_PWD.getValue0())
                .desc(REDIS_PWD.getValue2())
                .hasArg(true)
                .argName(REDIS_PWD.getValue0())
                .required(true)
                .build();

        final Option redisRoot = Option.builder(REDIS_ROOT.getValue1())
                .longOpt(REDIS_ROOT.getValue0())
                .desc(REDIS_ROOT.getValue2())
                .hasArg(true)
                .argName(REDIS_ROOT.getValue0())
                .required(true)
                .build();

        final Option apikey = Option.builder(ANDROZOO_API_KEY.getValue1())
                .longOpt(ANDROZOO_API_KEY.getValue0())
                .desc(ANDROZOO_API_KEY.getValue2())
                .hasArg(true)
                .argName(ANDROZOO_API_KEY.getValue0())
                .required(true)
                .build();

        this.firstOptions.addOption(help);

        this.options.addOption(platform);
        this.options.addOption(apkPath);
        this.options.addOption(output);
        this.options.addOption(apikey);
        this.options.addOption(redisPort);
        this.options.addOption(redisServer);
        this.options.addOption(redisPwd);
        this.options.addOption(redisRoot);

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

    public String getRedisServer() {
        return cmdLine.getOptionValue(REDIS_SERVER.getValue0());
    }

    public String getRedisRoot() {
        return cmdLine.getOptionValue(REDIS_ROOT.getValue0());
    }

    public String getRedisPwd() {
        return cmdLine.getOptionValue(REDIS_PWD.getValue0());
    }

    public String getRedisPort() {
        return cmdLine.getOptionValue(REDIS_PORT.getValue0());
    }

    public String getAndroZooApiKey() {
        return cmdLine.getOptionValue(ANDROZOO_API_KEY.getValue0());
    }
}
