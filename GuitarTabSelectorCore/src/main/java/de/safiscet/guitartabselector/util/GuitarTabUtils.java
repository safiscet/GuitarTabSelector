package de.safiscet.guitartabselector.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import de.safiscet.guitartabselector.exceptions.NoSuchGuitarTabException;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabUtils {

    private GuitarTabUtils() {

    }


    public static void openDefaultGuitarTab(final GuitarTab guitarTab,
                                            final GuitarTabConfiguration config) throws NoSuchGuitarTabException {
        final String optimalFormat = FormatUtils.getOptimalFormat(guitarTab, config.getFormatRanking());
        openGuitarTab(guitarTab, optimalFormat);
    }


    public static void openGuitarTab(final GuitarTab guitarTab, final String format) throws NoSuchGuitarTabException {
        if (guitarTab == null) {
            System.out.println("You have to select a guitar tab before opening it.");
            return;
        }
        if (!tabContainsFormat(guitarTab, format)) {
            throw new NoSuchGuitarTabException(
                    "The format " + format + " does not exist for guitar tab " + guitarTab.getName());
        }
        try {
            final Desktop desktop = Desktop.getDesktop();
            final File parent = new File(guitarTab.getPath());
            final Path tabPath = parent.toPath().resolve(guitarTab.getName() + "." + format);
            desktop.open(tabPath.toFile());
        } catch (final IOException e) {
            throw new NoSuchGuitarTabException(
                    "The guitar tab file " + guitarTab.getName() + format + " could not be opened.");
        }
    }


    public static boolean tabContainsFormat(final GuitarTab guitarTab, final String format) {
        for (final String tabFormat : guitarTab.getFormats()) {
            if (FormatUtils.equalsFormat(tabFormat, format)) {
                return true;
            }
        }
        return false;
    }
}
