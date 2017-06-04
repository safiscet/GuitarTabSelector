package util;

import model.GuitarTab;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class FormatUtilsTest {

    @Test
    public void getOrderedFormats_EmptyTabFormats() {
        GuitarTab tab = new GuitarTab("test", "");
        List<String> orderedFormats = FormatUtils.getOrderedFormats(tab, Arrays.asList("gp5", "pdf"));
        assertThat(orderedFormats.size(), is(0));
    }

    @Test
    public void getOrderedFormats_ExampleFormats() {
        GuitarTab tab = new GuitarTab("test", "", "f3", "f1", "f2");
        List<String> formatRanking = Arrays.asList("f1", "f2");

        List<String> orderedFormats = FormatUtils.getOrderedFormats(tab, formatRanking);

        assertThat(orderedFormats.size(), is(2));
        assertThat(orderedFormats.get(0), is(formatRanking.get(0)));
        assertThat(orderedFormats.get(1), is(formatRanking.get(1)));
    }

    @Test
    public void getOptimalFormat() {
        GuitarTab tab = new GuitarTab("test", "", "f2", "f3", "f4");
        List<String> formatRanking = Arrays.asList("f1", "f2", "f3");

        assertThat(FormatUtils.getOptimalFormat(tab, formatRanking), is("f2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOptimalFormat_NoOptimalSolution() {
        GuitarTab tab = new GuitarTab("test", "", "f2", "f3");
        List<String> formatRanking = Arrays.asList("f1", "f4");

        FormatUtils.getOptimalFormat(tab, formatRanking);
    }

}