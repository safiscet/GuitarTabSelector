import exceptions.InvalidConfigurationException;
import exceptions.NoSuchGuitarTabException;
import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import service.GuitarTabConfigurationService;
import service.GuitarTabDirectoryService;
import service.RandomGuitarTabService;
import util.FormatUtils;
import util.GuitarTabUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class GuitarTabSelector {

    private static final String rootArg = "r";
    private static final String rootArgLong = "root";
    private static final String excludedArg = "e";
    private static final String excludedArgLong = "excluded";
    private static final String formatArg = "f";
    private static final String formatArgLong = "formats";
    private static final String helpArg = "h";
    private static final String helpArgLong = "help";
    private static final String configArg = "c";
    private static final String configArgLong = "config";

    private static final List<String> defaultFormats = FormatUtils.getDefaultFormats();

    private GuitarTab currentTab;
    private GuitarTabConfiguration config;
    private GuitarTabConfigurationService configService;
    private RandomGuitarTabService randomGuitarTabService;

    public static void main(String[] args) {
        GuitarTabSelector selector = new GuitarTabSelector(args);
        selector.start();
    }

    private GuitarTabSelector(String[] args) {
        configService = new GuitarTabConfigurationService();
        createConfiguration(args);
        GuitarTabProvider guitarTabProvider = new GuitarTabDirectoryService(config);
        randomGuitarTabService = new RandomGuitarTabService(config, guitarTabProvider);
    }

    private void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Found " + randomGuitarTabService.getNumberOfTabs() + " different guitar tabs in total.\n");
        System.out.println("Control via command line input:");
        System.out.println("next, n \t - \t Get next Tab");
        System.out.println("prev, p \t - \t Get previous Tab");
        System.out.println("open, o \t - \t Open the current tab");
        System.out.println("exit, e \t - \t Exit");

        while (true) {
            System.out.println("");
            String input = scanner.nextLine();
            handleInput(input);
        }
    }

    private void handleInput(String input) {
        if (StringUtils.equalsAnyIgnoreCase(input, "next", "n")) {
            handleNextTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "prev", "p")) {
            handlePreviousTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "open", "o")) {
            handleOpenTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "exit", "e")) {
            System.out.println("Shutting down...");
            System.exit(0);
        } else {
            System.out.println("Input " + input + " could not be interpreted.");
        }
    }

    private void handleNextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            System.out.println("Selected Tab: " + currentTab.getName());
            System.out.println("Path: " + currentTab.getPath());
            System.out.println("Available Formats: "
                    + FormatUtils.getOrderedFormats(currentTab, config.getFormatRanking()));
        } catch (NoSuchGuitarTabException e) {
            System.out.println("There is no next tab!");
        }
    }

    private void handlePreviousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            System.out.println("Selected Tab: " + currentTab);
        } catch (NoSuchGuitarTabException e) {
            System.out.println("There is no previous tab!");
        }
    }

    private void handleOpenTab() {
        try {
            GuitarTabUtils.openDefaultGuitarTab(currentTab, config);
        } catch (IOException e) {
            System.out.println("The guitar tab could not be opened: " + e.getMessage());
        }
    }

    private void createConfiguration(String[] args) {
        Options options = getOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption(configArg)) {
                String configPath = cmd.getOptionValue(configArg);
                config = configService.getFromJsonFile(configPath);
            } else {
                config = new GuitarTabConfiguration();
            }

            if (cmd.hasOption(rootArg)) {
                config.setRootPath(cmd.getOptionValue(rootArg));
            }

            if (cmd.hasOption(excludedArg)) {
                String[] excludes = cmd.getOptionValues(excludedArg);
                config.setExcludedPaths(new ArrayList<>(Arrays.asList(excludes)));
            }

            if (cmd.hasOption(formatArg)) {
                String[] formats = cmd.getOptionValues(formatArg);
                config.setFormatRanking(new ArrayList<>(Arrays.asList(formats)));
            } else {
                config.setFormatRanking(defaultFormats);
            }

            if (StringUtils.isEmpty(config.getRootPath())) {
                throw new InvalidConfigurationException("There must be a root path!");
            }

        } catch (ParseException e) {
            help(options);
            throw new IllegalArgumentException(e);
        } catch (InvalidConfigurationException e) {
            System.out.println("The configuration was invalid. " + e.getMessage());
            help(options);
            throw new IllegalArgumentException(e);
        }
    }

    private void help(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
    }

    private Options getOptions() {
        Options options = new Options();

        Option rootPath = Option.builder(rootArg)
                .longOpt(rootArgLong)
                .argName("root dir")
                .hasArg()
                .desc("The root directory for the recursive guitar tab search")
                .build();

        Option excludedPaths = Option.builder(excludedArg)
                .longOpt(excludedArgLong)
                .argName("excluded dirs")
                .hasArgs()
                .desc("Excluded directories for the recursive guitar tab search")
                .build();

        Option formats = Option.builder(formatArg)
                .longOpt(formatArgLong)
                .argName("preferred formats")
                .hasArgs()
                .desc("Ordered formats that should be supported")
                .build();

        Option config = Option.builder(configArg)
                .longOpt(configArgLong)
                .argName("config file")
                .hasArg()
                .desc("JSON config file")
                .build();

        options.addOption(rootPath);
        options.addOption(excludedPaths);
        options.addOption(formats);
        options.addOption(config);
        options.addOption(helpArg, helpArgLong, false, "show help.");
        return options;
    }
}
