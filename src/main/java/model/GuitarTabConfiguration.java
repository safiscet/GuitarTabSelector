package model;

import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class GuitarTabConfiguration {

    private String rootPath;
    private Collection<String> exludedPaths;
    private List<String> formatRanking;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Collection<String> getExludedPaths() {
        return exludedPaths;
    }

    public void setExludedPaths(Collection<String> exludedPaths) {
        this.exludedPaths = exludedPaths;
    }

    public List<String> getFormatRanking() {
        return formatRanking;
    }

    public void setFormatRanking(List<String> formatRanking) {
        this.formatRanking = formatRanking;
    }

    @Override
    public String toString() {
        return "GuitarTabConfiguration{" +
                "rootPath='" + rootPath + '\'' +
                ", exludedPaths=" + exludedPaths +
                ", formatRanking=" + formatRanking +
                '}';
    }
}
