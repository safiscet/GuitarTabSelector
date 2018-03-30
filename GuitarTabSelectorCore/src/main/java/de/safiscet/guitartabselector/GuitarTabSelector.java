package de.safiscet.guitartabselector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import de.safiscet.guitartabselector.exceptions.InvalidConfigurationException;
import de.safiscet.guitartabselector.exceptions.NoSuchGuitarTabException;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.service.GuitarTabConfigurationService;
import de.safiscet.guitartabselector.service.GuitarTabSelectorFactory;
import de.safiscet.guitartabselector.service.RandomGuitarTabService;
import de.safiscet.guitartabselector.util.FormatUtils;
import de.safiscet.guitartabselector.util.GuitarTabUtils;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class GuitarTabSelector {

    private static final String ROOT_ARG = "r";
    private static final String ROOT_ARG_LONG = "root";
    private static final String EXCLUDED_ARG = "e";
    private static final String EXCLUDED_ARG_LONG = "excluded";
    private static final String FORMAT_ARG = "f";
    private static final String FORMAT_ARG_LONG = "formats";
    private static final String HELP_ARG = "h";
    private static final String HELP_ARG_LONG = "help";
    private static final String CONFIG_ARG = "c";
    private static final String CONFIG_ARG_LONG = "config";

    private static final List<String> defaultFormats = FormatUtils.getDefaultFormats();

    private GuitarTab currentTab;
    private GuitarTabConfiguration config;
    private final GuitarTabConfigurationService configService;
    private final RandomGuitarTabService randomGuitarTabService;


    public static void main(final String[] args) {
        final GuitarTabSelector selector = new GuitarTabSelector(args);
        selector.start();
    }


    private GuitarTabSelector(final String[] args) {
        configService = new GuitarTabConfigurationService();
        createConfiguration(args);
        final GuitarTabSelectorFactory guitarTabSelectorFactory = new GuitarTabSelectorFactory();
        randomGuitarTabService = guitarTabSelectorFactory.createRandomGuitarTabService(config);
    }


    private void start() {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Found " + randomGuitarTabService.getNumberOfTabs() + " different guitar tabs in total.\n");
        System.out.println("Control via command line input:");
        System.out.println("next, n \t - \t Get next Tab");
        System.out.println("prev, p \t - \t Get previous Tab");
        System.out.println("open, o \t - \t Open the current tab");
        System.out.println("exit, e \t - \t Exit");

        System.out.println();
        handleNextTab();

        while (true) {
            System.out.println("");
            final String input = scanner.nextLine();
            handleInput(input);
        }
    }


    private void handleInput(final String input) {
        if (StringUtils.equalsAnyIgnoreCase(input, "next", "n")) {
            handleNextTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "prev", "p")) {
            handlePreviousTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "open", "o")) {
            handleOpenDefaultTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "exit", "e")) {
            System.out.println("Shutting down...");
            System.exit(0);
        } else {
            if (GuitarTabUtils.tabContainsFormat(currentTab, input)) {
                handleOpenTab(input);
            } else {
                System.out.println("Input " + input + " could not be interpreted.");
            }
        }
    }


    private void handleNextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            System.out.println("Selected Tab: " + currentTab.getName());
            System.out.println("Path: " + currentTab.getPath());
            System.out.println("Available Formats: "
                    + FormatUtils.getOrderedFormats(currentTab, config.getFormatRanking()));
        } catch (final NoSuchGuitarTabException e) {
            System.out.println("There is no next tab!");
        }
    }


    private void handlePreviousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            System.out.println("Selected Tab: " + currentTab);
        } catch (final NoSuchGuitarTabException e) {
            System.out.println("There is no previous tab!");
        }
    }


    private void handleOpenDefaultTab() {
        try {
            GuitarTabUtils.openDefaultGuitarTab(currentTab, config);
        } catch (final NoSuchGuitarTabException e) {
            System.out.println("The guitar tab could not be opened: " + e.getMessage());
        }
    }


    private void handleOpenTab(final String format) {
        try {
            GuitarTabUtils.openGuitarTab(currentTab, format);
        } catch (final NoSuchGuitarTabException e) {
            System.out.println("The guitar tab could not be opened: " + e.getMessage());
        }
    }


    private void createConfiguration(final String[] args) {
        final Options options = getOptions();
        final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption(CONFIG_ARG)) {
                final String configPath = cmd.getOptionValue(CONFIG_ARG);
                config = configService.getFromJsonFile(configPath);
            } else {
                config = new GuitarTabConfiguration();
            }

            if (cmd.hasOption(ROOT_ARG)) {
                config.setRootPath(cmd.getOptionValue(ROOT_ARG));
            }

            if (cmd.hasOption(EXCLUDED_ARG)) {
                final String[] excludes = cmd.getOptionValues(EXCLUDED_ARG);
                config.setExcludedPaths(new ArrayList<>(Arrays.asList(excludes)));
            }

            if (cmd.hasOption(FORMAT_ARG)) {
                final String[] formats = cmd.getOptionValues(FORMAT_ARG);
                config.setFormatRanking(new ArrayList<>(Arrays.asList(formats)));
            } else {
                config.setFormatRanking(defaultFormats);
            }

            if (StringUtils.isEmpty(config.getRootPath())) {
                throw new InvalidConfigurationException("There must be a root path!");
            }

        } catch (final ParseException e) {
            help(options);
            throw new IllegalArgumentException(e);
        } catch (final InvalidConfigurationException e) {
            System.out.println("The configuration was invalid. " + e.getMessage());
            help(options);
            throw new IllegalArgumentException(e);
        }
    }


    private void help(final Options options) {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
    }


    private Options getOptions() {
        final Options options = new Options();

        final Option rootPath = Option.builder(ROOT_ARG)
                .longOpt(ROOT_ARG_LONG)
                .argName("root dir")
                .hasArg()
                .desc("The root directory for the recursive guitar tab search")
                .build();

        final Option excludedPaths = Option.builder(EXCLUDED_ARG)
                .longOpt(EXCLUDED_ARG_LONG)
                .argName("excluded dirs")
                .hasArgs()
                .desc("Excluded directories for the recursive guitar tab search")
                .build();

        final Option formats = Option.builder(FORMAT_ARG)
                .longOpt(FORMAT_ARG_LONG)
                .argName("preferred formats")
                .hasArgs()
                .desc("Ordered formats that should be supported")
                .build();

        final Option config = Option.builder(CONFIG_ARG)
                .longOpt(CONFIG_ARG_LONG)
                .argName("config file")
                .hasArg()
                .desc("JSON config file")
                .build();

        options.addOption(rootPath);
        options.addOption(excludedPaths);
        options.addOption(formats);
        options.addOption(config);
        options.addOption(HELP_ARG, HELP_ARG_LONG, false, "show help.");
        return options;
    }
}
