package controller;

import model.GuitarTabConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
public class RandomGuitarTabServiceTest {

    //TODO
    private RandomGuitarTabService underTest;

    @Before
    public void init() {
        underTest = new RandomGuitarTabService(new GuitarTabConfiguration());
    }

    @Test
    public void noGuitarTabs_NextTab() {

    }

    @Test
    public void noGuitarTabs_PreviousTab() {

    }

}