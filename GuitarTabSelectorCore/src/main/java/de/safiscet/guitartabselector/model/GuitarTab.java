package de.safiscet.guitartabselector.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class GuitarTab {

    private String name;
    private String path;
    private Set<String> formats = new HashSet<>();

    public GuitarTab() {
    }


    public GuitarTab(String name, String path, String... formats) {
        this.name = name;
        this.path = path;
        this.formats = new HashSet<>(Arrays.asList(formats));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addFormat(String format) {
        formats.add(format);
    }

    public Set<String> getFormats() {
        return formats;
    }

    @Override
    public String toString() {
        return "GuitarTab{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", formats=" + formats +
                '}';
    }
}
