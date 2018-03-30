package de.safiscet.guitartabselector.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import de.safiscet.guitartabselector.exceptions.NoSuchGuitarTabException;
import de.safiscet.guitartabselector.interfaces.GuitarTabProvider;
import de.safiscet.guitartabselector.model.FakeGuitarTabConfiguration;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.util.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Created by Stefan Fritsch on 27.05.2017.
 */
@ExtendWith(MockitoExtension.class)
class RandomGuitarTabServiceTest {

    private RandomGuitarTabService underTest;


    @Test
    void noGuitarTabs_NextTab() {
        initTestWithRandomTabs(0);

        assertThrows(NoSuchGuitarTabException.class, () -> underTest.getNextTab());
    }


    @Test
    void noGuitarTabs_PreviousTab() {
        initTestWithRandomTabs(0);

        assertThrows(NoSuchGuitarTabException.class, () -> underTest.getPreviousTab());
    }


    @Test
    void someGuitarTabs_ResultInAnyOrder() throws NoSuchGuitarTabException {
        final List<GuitarTab> tabs = initTestWithRandomTabs(100);

        final List<GuitarTab> results = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            results.add(underTest.getNextTab());
        }

        assertThat(results).containsExactlyInAnyOrderElementsOf(tabs);
    }


    @Test
    void someGuitarTabs_GetAllTabs() {
        final List<GuitarTab> tabs = initTestWithRandomTabs(20);

        assertThat(underTest.getAllTabs()).containsExactlyInAnyOrderElementsOf(tabs);
    }


    @Test
    void someGuitarTabs_numberOfTabs() {
        final List<GuitarTab> tabs = initTestWithRandomTabs(50);

        assertThat(underTest.getNumberOfTabs()).isEqualTo(tabs.size());
    }


    @Test
    void nextAndPreviousTab_Chaining() throws NoSuchGuitarTabException {
        initTestWithRandomTabs(2);
        final List<GuitarTab> tabs = underTest.getAllTabs();

        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(0));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(1));
        assertThat(underTest.getPreviousTab()).isEqualTo(tabs.get(0));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(1));
    }


    @Test
    void nextAndPreviousTab_MoreChaining() throws NoSuchGuitarTabException {
        initTestWithRandomTabs(4);
        final List<GuitarTab> tabs = underTest.getAllTabs();

        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(0));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(1));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(2));
        assertThat(underTest.getPreviousTab()).isEqualTo(tabs.get(1));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(2));
        assertThat(underTest.getNextTab()).isEqualTo(tabs.get(3));
        assertThat(underTest.getPreviousTab()).isEqualTo(tabs.get(2));
        assertThat(underTest.getPreviousTab()).isEqualTo(tabs.get(1));
        assertThat(underTest.getPreviousTab()).isEqualTo(tabs.get(0));
    }


    /**
     * Initialize the RandomGuitarTabService with a mocked GuitarTabProvider that returns numberOfTabs generated GuitarTabs.
     * Returns the generated GuitarTabs for further testing.
     */
    private List<GuitarTab> initTestWithRandomTabs(final int numberOfTabs) {
        final GuitarTabProvider provider = Mockito.mock(GuitarTabDirectoryService.class);
        final List<GuitarTab> tabs = tabsForTesting(numberOfTabs);
        when(provider.getAllGuitarTabs()).thenReturn(tabs);

        final GuitarTabConfiguration config = new FakeGuitarTabConfiguration();
        underTest = new RandomGuitarTabService(provider);

        return tabs;
    }


    private List<GuitarTab> tabsForTesting(final int numberOfTabs) {
        final List<GuitarTab> tabs = new ArrayList<>();
        for (int i = 0; i < numberOfTabs; i++) {
            tabs.add(new GuitarTab("Tab" + i, ""));
        }
        return tabs;
    }
}