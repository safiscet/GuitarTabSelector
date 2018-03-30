package de.safiscet.guitartabselector.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import de.safiscet.guitartabselector.interfaces.GuitarTabCollector;
import de.safiscet.guitartabselector.interfaces.GuitarTabProvider;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class GuitarTabDirectoryService implements GuitarTabCollector, GuitarTabProvider {

    private GuitarTab currentTab;
    private final List<GuitarTab> guitarTabs;
    private final List<String> formats;
    private final List<String> discardedFormats = new ArrayList<>();


    public GuitarTabDirectoryService(final GuitarTabConfiguration config, final DirectoryVisitor directoryVisitor) {
        guitarTabs = new ArrayList<>();
        formats = config.getFormatRanking();
        directoryVisitor.startVisiting(this);
    }


    @Override
    public void notifyNewGuitarTab(final String name, final String path, final String format) {
        if (!isSupportedFormat(format)) {
            if (!discardedFormats.contains(format.toLowerCase())) {
                discardedFormats.add(format);
                System.out.println("Discarded format: " + format);
            }
            return;
        }
        if (isCurrentTabAsOtherExtension(name, path, format)) {
            currentTab.addFormat(format);
        } else {
            currentTab = new GuitarTab();
            currentTab.setName(name);
            currentTab.setPath(path);
            currentTab.addFormat(format);
            guitarTabs.add(currentTab);
        }
    }


    private boolean isSupportedFormat(final String format) {
        return formats.stream()
                .anyMatch(supported -> StringUtils.equalsIgnoreCase(format, supported));
    }


    @Override
    public void notifyNewDirectory() {
        currentTab = null;
    }


    @Override
    public Collection<GuitarTab> getAllGuitarTabs() {
        return guitarTabs;
    }


    private boolean isCurrentTabAsOtherExtension(final String name, final String path, final String format) {
        return currentTab != null &&
                StringUtils.equalsIgnoreCase(currentTab.getName(), name);
    }
}
