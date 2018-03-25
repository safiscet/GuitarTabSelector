package de.safiscet.guitartabselector;

import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabUtils {

    public static void openDefaultGuitarTab(GuitarTab guitarTab, GuitarTabConfiguration config) throws IOException {
        String optimalFormat = FormatUtils.getOptimalFormat(guitarTab, config.getFormatRanking());
        openGuitarTab(guitarTab, optimalFormat);
    }

    public static void openGuitarTab(GuitarTab guitarTab, String format) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        if (guitarTab == null) {
            System.out.println("You have to select a guitar tab before opening it.");
            return;
        }
        File parent = new File(guitarTab.getPath());
        Path tabPath = parent.toPath().resolve(guitarTab.getName() + "." + format);
        desktop.open(tabPath.toFile());
    }
}
