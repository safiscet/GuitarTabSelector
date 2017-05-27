package interfaces;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public interface GuitarTabCollector {

    void notifyNewGuitarTab(String name, String path, String format);

    void notifyNewDirectory();
}
