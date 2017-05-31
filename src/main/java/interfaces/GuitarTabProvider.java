package interfaces;

import model.GuitarTab;

import java.util.Collection;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public interface GuitarTabProvider {

    Collection<GuitarTab> getAllGuitarTabs();
}
