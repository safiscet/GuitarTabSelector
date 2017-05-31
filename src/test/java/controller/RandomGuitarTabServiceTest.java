package controller;

import interfaces.GuitarTabProvider;
import model.FakeGuitarTabConfiguration;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import model.NoSuchGuitarTabException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomGuitarTabServiceTest {

    private RandomGuitarTabService underTest;

    @Test(expected = NoSuchGuitarTabException.class)
    public void noGuitarTabs_NextTab() throws NoSuchGuitarTabException {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        when(provider.getAllGuitarTabs()).thenReturn(Collections.emptyList());
        init(provider);

        underTest.getNextTab();
    }

    @Test(expected = NoSuchGuitarTabException.class)
    public void noGuitarTabs_PreviousTab() throws NoSuchGuitarTabException {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        when(provider.getAllGuitarTabs()).thenReturn(Collections.emptyList());
        init(provider);

        underTest.getPreviousTab();
    }

    @Test
    public void someGuitarTabs_ResultInAnyOrder() throws NoSuchGuitarTabException {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        List<GuitarTab> tabs = Arrays.asList(
                new GuitarTab("first", ""),
                new GuitarTab("second", ""),
                new GuitarTab("third", "")
        );
        when(provider.getAllGuitarTabs()).thenReturn(tabs);
        init(provider);

        List<GuitarTab> results = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            results.add(underTest.getNextTab());
        }

        assertThat(results, Matchers.containsInAnyOrder(tabs.toArray()));
    }

    private void init(GuitarTabProvider providerToUse) {
        GuitarTabConfiguration config = new FakeGuitarTabConfiguration();
        underTest = new RandomGuitarTabService(config, providerToUse);
    }

}