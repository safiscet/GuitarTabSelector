package controller;

import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class RandomGuitarTabService {

    private final List<GuitarTab> guitarTabs;
    private final ListIterator<GuitarTab> iterator;

    public RandomGuitarTabService(GuitarTabConfiguration config) {
        GuitarTabDirectoryService directoryService = new GuitarTabDirectoryService(config);
        guitarTabs = new ArrayList<>(directoryService.getAllGuitarTabs());
        System.out.println(guitarTabs);
        iterator = guitarTabs.listIterator();
    }

    public GuitarTab getNextTab() throws NoSuchGuitarTabException {
        if(iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new NoSuchGuitarTabException("There is no next guitar tab.");
        }
    }

    public GuitarTab getPreviousTab() throws NoSuchGuitarTabException {
        if(iterator.hasPrevious()) {
            return iterator.previous();
        } else {
            throw new NoSuchGuitarTabException("There is no previous guitar tab.");
        }
    }

    public List<GuitarTab> getAllTabs() {
        return guitarTabs;
    }
}
