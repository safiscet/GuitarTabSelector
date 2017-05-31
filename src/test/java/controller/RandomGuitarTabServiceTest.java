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
import static org.hamcrest.core.Is.is;
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
        initTest(provider);

        underTest.getNextTab();
    }

    @Test(expected = NoSuchGuitarTabException.class)
    public void noGuitarTabs_PreviousTab() throws NoSuchGuitarTabException {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        when(provider.getAllGuitarTabs()).thenReturn(Collections.emptyList());
        initTest(provider);

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
        initTest(provider);

        List<GuitarTab> results = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            results.add(underTest.getNextTab());
        }

        assertThat(results, Matchers.containsInAnyOrder(tabs.toArray()));
    }

    @Test
    public void someGuitarTabs_GetAllTabs() {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        List<GuitarTab> tabs = Arrays.asList(
                new GuitarTab("first", ""),
                new GuitarTab("second", ""),
                new GuitarTab("third", ""),
                new GuitarTab("fourth", "")
        );
        when(provider.getAllGuitarTabs()).thenReturn(tabs);
        initTest(provider);

        assertThat(underTest.getAllTabs(), Matchers.containsInAnyOrder(tabs.toArray()));
    }

    @Test
    public void someGuitarTabs_numberOfTabs() {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        List<GuitarTab> tabs = Arrays.asList(
                new GuitarTab("first", ""),
                new GuitarTab("second", ""),
                new GuitarTab("third", ""),
                new GuitarTab("fourth", ""),
                new GuitarTab("fifth", "")
        );
        when(provider.getAllGuitarTabs()).thenReturn(tabs);
        initTest(provider);

        assertThat(underTest.getNumberOfTabs(), is(tabs.size()));
    }

    private void initTest(GuitarTabProvider providerToUse) {
        GuitarTabConfiguration config = new FakeGuitarTabConfiguration();
        underTest = new RandomGuitarTabService(config, providerToUse);
    }

}