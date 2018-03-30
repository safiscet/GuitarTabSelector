package de.safiscet.guitartabselector.util;

import de.safiscet.guitartabselector.model.GuitarTab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class FormatUtils {


    public static List<String> getDefaultFormats() {
        return new ArrayList<>(Arrays.asList("gp5", "gpx", "pdf", "ptb", "gp4", "txt", "odt", "docx", "tab"));
    }

    /**
     * Returns the available formats for a given tab ordered by the given format preferences
     */
    public static List<String> getOrderedFormats(GuitarTab tab, List<String> formatRanking) {
        Set<String> tabFormats = tab.getFormats();
        List<String> orderedFormats = new ArrayList<>();

        for (String format : formatRanking) {
            if (tabFormats.contains(format.toLowerCase())) {
                orderedFormats.add(format);
            }
        }

        return orderedFormats;
    }

    public static String getOptimalFormat(GuitarTab tab, List<String> formatRanking) {
        for (String format : formatRanking) {
            if (tab.getFormats().contains(format.toLowerCase())) {
                return format;
            }
        }

        throw new IllegalArgumentException("There is no optimal solution for tab formats "
                + tab.getFormats() + " and format preferences " + formatRanking);
    }
}
