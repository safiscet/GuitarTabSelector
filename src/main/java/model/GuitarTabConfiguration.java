package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class GuitarTabConfiguration {

    private String rootPath;
    private Collection<String> excludedPaths = new ArrayList<>();
    private List<String> formatRanking = new ArrayList<>();

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Collection<String> getExcludedPaths() {
        return excludedPaths;
    }

    public void setExcludedPaths(Collection<String> excludedPaths) {
        this.excludedPaths = excludedPaths;
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
                ", excludedPaths=" + excludedPaths +
                ", formatRanking=" + formatRanking +
                '}';
    }
}
