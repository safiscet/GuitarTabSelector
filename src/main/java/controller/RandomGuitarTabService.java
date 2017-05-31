package controller;

import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class RandomGuitarTabService {

    private final List<GuitarTab> guitarTabs;
    private final ListIterator<GuitarTab> iterator;

    public RandomGuitarTabService(GuitarTabConfiguration config) {
        this(config, new GuitarTabDirectoryService(config));
    }

    public RandomGuitarTabService(GuitarTabConfiguration config, GuitarTabProvider guitarTabProvider) {
        guitarTabs = new ArrayList<>(guitarTabProvider.getAllGuitarTabs());
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
