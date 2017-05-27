package model;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class GuitarTab {

    private String name;
    private String path;
    private Set<String> formats = new HashSet<>();

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
