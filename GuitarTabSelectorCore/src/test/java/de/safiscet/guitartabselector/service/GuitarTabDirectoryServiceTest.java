package de.safiscet.guitartabselector.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.util.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stefan Fritsch
 */
@ExtendWith(MockitoExtension.class)
class GuitarTabDirectoryServiceTest {

    private static final String ROOT_PATH = "RootPath";
    private static final List<String> FORMATS = Arrays.asList("Format1", "Format2");
    private static final List<String> EXCLUDED_PATHS = Arrays.asList("Excluded1", "Excluded2");

    @Mock
    private DirectoryVisitor directoryVisitor;

    private GuitarTabDirectoryService underTest;


    @BeforeEach
    void init() {
        underTest = new GuitarTabDirectoryService(createTestConfiguration(), directoryVisitor);
    }


    @Test
    void emptyAtStart() {
        final Collection<GuitarTab> guitarTabs = underTest.getAllGuitarTabs();

        assertThat(guitarTabs).isEmpty();
    }


    @Test
    void collectSingleTabWithSingleFormat() {
        underTest.notifyNewDirectory();
        underTest.notifyNewGuitarTab("Name", "Path", "Format1");
        final Collection<GuitarTab> guitarTabs = underTest.getAllGuitarTabs();

        assertThat(guitarTabs).hasSize(1);

        final GuitarTab expected = new GuitarTab("Name", "Path", "Format1");
        assertTabsAreEqual(new ArrayList<>(guitarTabs).get(0), expected);
    }

    // TODO Add tests for multiple tabs, multiple formats, multiple directories and discarded formats


    private GuitarTabConfiguration createTestConfiguration() {
        final GuitarTabConfiguration config = new GuitarTabConfiguration();
        config.setRootPath(ROOT_PATH);
        config.setFormatRanking(FORMATS);
        config.setExcludedPaths(EXCLUDED_PATHS);
        return config;
    }


    private void assertTabsAreEqual(final GuitarTab first, final GuitarTab second) {
        assertThat(first.getName()).isEqualTo(second.getName());
        assertThat(first.getFormats()).containsExactlyInAnyOrderElementsOf(second.getFormats());
        assertThat(first.getPath()).isEqualTo(second.getPath());
    }
}