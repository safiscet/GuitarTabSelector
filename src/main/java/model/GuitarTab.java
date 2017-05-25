package model;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class GuitarTab {

    private String name;
    private String path;
    private String format;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "GuitarTab{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
