package de.safiscet.guitartabselector.service;

import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.safiscet.guitartabselector.exceptions.InvalidConfigurationException;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.util.FormatUtils;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stefan Fritsch
 */
class GuitarTabConfigurationServiceTest {

    private GuitarTabConfigurationService underTest;


    @BeforeEach
    void init() {
        underTest = new GuitarTabConfigurationService();
    }


    @Test
    void getFromJsonFile() throws InvalidConfigurationException {
        final URL testconfigUrl = getClass().getResource("/testconfig.json");
        final GuitarTabConfiguration config = underTest.getFromJsonFile(testconfigUrl.getPath());

        assertThat(config.getRootPath()).isEqualTo("RootPath");
        assertThat(config.getFormatRanking()).containsExactly("Format1", "Format2");
        assertThat(config.getExcludedPaths()).containsExactly("ExcludedPath1", "ExcludedPath2");
    }


    @Test
    void getFromJsonFile_EmptyFormatRanking_AddsDefault() throws InvalidConfigurationException {
        final URL testconfigUrl = getClass().getResource("/testconfigWithoutFormats.json");
        final GuitarTabConfiguration config = underTest.getFromJsonFile(testconfigUrl.getPath());

        assertThat(config.getRootPath()).isEqualTo("RootPath");
        assertThat(config.getFormatRanking()).containsExactlyElementsOf(FormatUtils.getDefaultFormats());
        assertThat(config.getExcludedPaths()).containsExactly("ExcludedPath1", "ExcludedPath2");
    }


    @Test
    void getFromJsonFile_FileDoesNotExist() {
        final String invalidPath = "doesNotExist.json";

        Assertions.assertThrows(InvalidConfigurationException.class,
                () -> underTest.getFromJsonFile(invalidPath));
    }


    @Test
    void getFromJsonFile_InvalidFile() {
        final URL testconfigUrl = getClass().getResource("/invalid.json");

        Assertions.assertThrows(InvalidConfigurationException.class,
                () -> underTest.getFromJsonFile(testconfigUrl.getPath()));
    }
}