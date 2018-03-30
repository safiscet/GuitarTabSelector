package de.safiscet.guitartabselector.util;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import de.safiscet.guitartabselector.FormatUtils;
import de.safiscet.guitartabselector.model.GuitarTab;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
class FormatUtilsTest {

    @Test
    void getOrderedFormats_EmptyTabFormats() {
        final GuitarTab tab = new GuitarTab("test", "");
        final List<String> orderedFormats = FormatUtils.getOrderedFormats(tab, Arrays.asList("gp5", "pdf"));
        assertThat(orderedFormats).isEmpty();
    }


    @Test
    void getOrderedFormats_ExampleFormats() {
        final GuitarTab tab = new GuitarTab("test", "", "f3", "f1", "f2");
        final List<String> formatRanking = Arrays.asList("f1", "f2");

        final List<String> orderedFormats = FormatUtils.getOrderedFormats(tab, formatRanking);


        assertThat(orderedFormats).hasSize(2);
        assertThat(orderedFormats).containsExactlyElementsOf(formatRanking);
    }


    @Test
    void getOptimalFormat() {
        final GuitarTab tab = new GuitarTab("test", "", "f2", "f3", "f4");
        final List<String> formatRanking = Arrays.asList("f1", "f2", "f3");

        assertThat(FormatUtils.getOptimalFormat(tab, formatRanking)).isEqualTo("f2");
    }


    @Test
    void getOptimalFormat_NoOptimalSolution() {
        final GuitarTab tab = new GuitarTab("test", "", "f2", "f3");
        final List<String> formatRanking = Arrays.asList("f1", "f4");

        Assertions.assertThrows(IllegalArgumentException.class, () -> FormatUtils.getOptimalFormat(tab, formatRanking));
    }

}