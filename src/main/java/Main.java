import controller.GuitarTabDirectoryService;
import controller.RandomGuitarTabService;
import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class Main {

    private static final String rootArg = "r";
    private static final String rootArgLong = "root";
    private static final String excludedArg = "e";
    private static final String excludedArgLong = "excluded";
    private static final String formatArg = "f";
    private static final String formatArgLong = "formats";
    private static final String helpArg = "h";
    private static final String helpArgLong = "help";


    private static GuitarTab currentTab;
    private static RandomGuitarTabService randomGuitarTabService;

    public static void main(String[] args) {
        GuitarTabConfiguration config = createConfiguration(args);
        GuitarTabProvider guitarTabProvider = new GuitarTabDirectoryService(config);
        randomGuitarTabService = new RandomGuitarTabService(config, guitarTabProvider);

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

    private static void handleInput(String input) {
        if (StringUtils.equalsAnyIgnoreCase(input, "next", "n")) {
            handleNextTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "prev", "p")) {
            handlePreviousTab();
        } else if (StringUtils.equalsAnyIgnoreCase(input, "open", "o")) {
            handleOpenTab();
        } else {
            System.out.println("Input " + input + " could not be interpreted.");
        }
    }

    private static void handleNextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            System.out.println("Selected Tab: " + currentTab.getName());
            System.out.println("Path: " + currentTab.getPath());
            System.out.println("Available Formats: " + currentTab.getFormats());
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

    private static void handleOpenTab() {
        Desktop desktop = Desktop.getDesktop();
        if (currentTab == null) {
            System.out.println("You have to select a guitar tab before opening it.");
            return;
        }
        File file = new File(currentTab.getPath());
        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
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

            if (cmd.hasOption(formatArg)) {
                String[] formats = cmd.getOptionValues(formatArg);
                config.setFormatRanking(new ArrayList<>(Arrays.asList(formats)));
            }

            if (cmd.hasOption(helpArg)) {
                help(options);
            }

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

        Option rootPath = Option.builder(rootArg)
                .longOpt(rootArgLong)
                .argName("root dir")
                .hasArg()
                .desc("The root directory for the recursive guitar tab search")
                .required()
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

        options.addOption(rootPath);
        options.addOption(excludedPaths);
        options.addOption(formats);
        options.addOption(helpArg, helpArgLong, false, "show help.");
        return options;
    }
}
