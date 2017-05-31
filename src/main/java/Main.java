import controller.GuitarTabDirectoryService;
import controller.RandomGuitarTabService;
import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class Main {

    private static final String rootArg = "root";
    private static final String excludedArg = "excluded";

    private static GuitarTab currentTab;
    private static RandomGuitarTabService randomGuitarTabService;

    public static void main(String[] args) {
        GuitarTabConfiguration config = createConfiguration(args);
        GuitarTabProvider guitarTabProvider = new GuitarTabDirectoryService(config);
        randomGuitarTabService = new RandomGuitarTabService(config, guitarTabProvider);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Found " + randomGuitarTabService.getNumberOfTabs() + " different guitar tabs in total.\n");
        System.out.println("Control via command line input:");
        System.out.println("'next' \t Next Tab");
        System.out.println("'prev' \t Previous Tab");

        while (true) {
            System.out.println("");
            String input = scanner.nextLine();
            handleInput(input);
        }
    }

    private static void handleInput(String input) {
        switch (input) {
            case "next":
                handleNextTab();
                break;
            case "prev":
                handlePreviousTab();
                break;
            default:
                System.out.println("Input " + input + " could not be interpreted.");
                break;
        }
    }

    private static void handleNextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            System.out.println("Selected Tab: " + currentTab);
        } catch (NoSuchGuitarTabException e) {
            System.out.println("There is no next tab!");
        }
    }

    private static void handlePreviousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            System.out.println("Selected Tab: " + currentTab);
        } catch (NoSuchGuitarTabException e) {
            System.out.println("There is no previous tab!");
        }
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
