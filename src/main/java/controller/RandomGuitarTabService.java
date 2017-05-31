package controller;

import interfaces.GuitarTabProvider;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stefan Fritsch on 25.05.2017.
 */
public class RandomGuitarTabService {

    private final List<GuitarTab> guitarTabs;
    private int currentPosition = -1;

    public RandomGuitarTabService(GuitarTabConfiguration config, GuitarTabProvider guitarTabProvider) {
        guitarTabs = new ArrayList<>(guitarTabProvider.getAllGuitarTabs());
        Collections.shuffle(guitarTabs);
    }

    public GuitarTab getNextTab() throws NoSuchGuitarTabException {
        if (currentPosition + 1 < guitarTabs.size()) {
            currentPosition++;
            return guitarTabs.get(currentPosition);
        } else {
            throw new NoSuchGuitarTabException("There is no next guitar tab.");
        }
    }

    public GuitarTab getPreviousTab() throws NoSuchGuitarTabException {
        if (currentPosition - 1 >= 0) {
            currentPosition--;
            return guitarTabs.get(currentPosition);
        } else {
            throw new NoSuchGuitarTabException("There is no previous guitar tab.");
        }
    }

    public List<GuitarTab> getAllTabs() {
        return guitarTabs;
    }

    public int getNumberOfTabs() {
        return guitarTabs.size();
    }

}
