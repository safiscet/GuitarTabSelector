package controller;

import exceptions.NoSuchGuitarTabException;
import interfaces.GuitarTabProvider;
import model.FakeGuitarTabConfiguration;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
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
        initTestWithRandomTabs(0);

        underTest.getNextTab();
    }

    @Test(expected = NoSuchGuitarTabException.class)
    public void noGuitarTabs_PreviousTab() throws NoSuchGuitarTabException {
        initTestWithRandomTabs(0);

        underTest.getPreviousTab();
    }

    @Test
    public void someGuitarTabs_ResultInAnyOrder() throws NoSuchGuitarTabException {
        List<GuitarTab> tabs = initTestWithRandomTabs(100);

        List<GuitarTab> results = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            results.add(underTest.getNextTab());
        }

        assertThat(results, Matchers.containsInAnyOrder(tabs.toArray()));
    }

    @Test
    public void someGuitarTabs_GetAllTabs() {
        List<GuitarTab> tabs = initTestWithRandomTabs(20);

        assertThat(underTest.getAllTabs(), Matchers.containsInAnyOrder(tabs.toArray()));
    }

    @Test
    public void someGuitarTabs_numberOfTabs() {
        List<GuitarTab> tabs = initTestWithRandomTabs(50);

        assertThat(underTest.getNumberOfTabs(), is(tabs.size()));
    }

    @Test
    public void nextAndPreviousTab_Chaining() throws NoSuchGuitarTabException {
        initTestWithRandomTabs(2);
        List<GuitarTab> tabs = underTest.getAllTabs();

        assertThat(underTest.getNextTab(), is(tabs.get(0)));
        assertThat(underTest.getNextTab(), is(tabs.get(1)));
        assertThat(underTest.getPreviousTab(), is(tabs.get(0)));
        assertThat(underTest.getNextTab(), is(tabs.get(1)));
    }

    @Test
    public void nextAndPreviousTab_MoreChaining() throws NoSuchGuitarTabException {
        initTestWithRandomTabs(4);
        List<GuitarTab> tabs = underTest.getAllTabs();

        assertThat(underTest.getNextTab(), is(tabs.get(0)));
        assertThat(underTest.getNextTab(), is(tabs.get(1)));
        assertThat(underTest.getNextTab(), is(tabs.get(2)));
        assertThat(underTest.getPreviousTab(), is(tabs.get(1)));
        assertThat(underTest.getNextTab(), is(tabs.get(2)));
        assertThat(underTest.getNextTab(), is(tabs.get(3)));
        assertThat(underTest.getPreviousTab(), is(tabs.get(2)));
        assertThat(underTest.getPreviousTab(), is(tabs.get(1)));
        assertThat(underTest.getPreviousTab(), is(tabs.get(0)));
    }

    /**
     * Initialize the RandomGuitarTabService with a mocked GuitarTabProvider that returns numberOfTabs generated GuitarTabs.
     * Returns the generated GuitarTabs for further testing.
     */
    private List<GuitarTab> initTestWithRandomTabs(int numberOfTabs) {
        GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        List<GuitarTab> tabs = tabsForTesting(numberOfTabs);
        when(provider.getAllGuitarTabs()).thenReturn(tabs);

        GuitarTabConfiguration config = new FakeGuitarTabConfiguration();
        underTest = new RandomGuitarTabService(config, provider);

        return tabs;
    }

    private List<GuitarTab> tabsForTesting(int numberOfTabs) {
        List<GuitarTab> tabs = new ArrayList<>();
        for (int i = 0; i < numberOfTabs; i++) {
            tabs.add(new GuitarTab("Tab" + i, ""));
        }
        return tabs;
    }
}