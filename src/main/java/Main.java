import controller.RandomGuitarTabService;
import model.GuitarTabConfiguration;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class Main {

    private static final String rootArg = "root";
    private static final String excludedArg = "excluded";

    public static void main(String[] args) {
        GuitarTabConfiguration config = null;
        config = createConfiguration(args);
        RandomGuitarTabService randomGuitarTabService = new RandomGuitarTabService(config);
    }

    private static GuitarTabConfiguration createConfiguration(String[] args) {
        Options options = getOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            GuitarTabConfiguration config = new GuitarTabConfiguration();

            config.setRootPath(cmd.getOptionValue(rootArg));

            if (cmd.hasOption(excludedArg)) {
                String[] excludes = cmd.getOptionValues(excludedArg);
                config.setExcludedPaths(new ArrayList<>(Arrays.asList(excludes)));
            }

            if (cmd.hasOption("h"))
                help(options);

            return config;
        } catch (ParseException e) {
            help(options);
            throw new IllegalArgumentException(e);
        }
    }

    private static void help(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
    }

    private static Options getOptions() {
        Options options = new Options();

        Option rootPath = Option.builder("r")
                .longOpt("root")
                .argName("root dir")
                .hasArg()
                .desc("The root directory for the recursive guitar tab search")
                .required()
                .build();

        Option excludedPaths = Option.builder("e")
                .longOpt("excluded")
                .argName("excluded dirs")
                .hasArgs()
                .desc("Excluded directories for the recursive guitar tab search")
                .build();

        options.addOption(rootPath);
        options.addOption(excludedPaths);
        options.addOption("h", "help", false, "show help.");
        return options;
    }
}
