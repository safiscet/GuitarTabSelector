package service;

import interfaces.GuitarTabCollector;
import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class GuitarTabDirectoryService implements GuitarTabCollector, GuitarTabProvider {

    private GuitarTab currentTab;
    private List<GuitarTab> guitarTabs;
    private List<String> formats;
    private List<String> discardedFormats = new ArrayList<>();

    public GuitarTabDirectoryService(GuitarTabConfiguration config) {
        guitarTabs = new ArrayList<>();
        formats = config.getFormatRanking();
        DirectoryVisitor directoryVisitor = new DirectoryVisitor(config, this);
        directoryVisitor.startVisiting();
    }

    @Override
    public void notifyNewGuitarTab(String name, String path, String format) {
        if (!isSupportedFormat(format)) {
            if (!discardedFormats.contains(format.toLowerCase())) {
                discardedFormats.add(format);
                System.out.println("Discarded format: " + format);
            }
            return;
        }
        if(isCurrentTabAsOtherExtension(name, path, format)) {
            currentTab.addFormat(format);
        } else {
            currentTab = new GuitarTab();
            currentTab.setName(name);
            currentTab.setPath(path);
            currentTab.addFormat(format);
            guitarTabs.add(currentTab);
        }
    }

    private boolean isSupportedFormat(String format) {
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

    private boolean isCurrentTabAsOtherExtension(String name, String path, String format) {
        return currentTab != null &&
                StringUtils.equalsIgnoreCase(currentTab.getName(), name);
    }
}
