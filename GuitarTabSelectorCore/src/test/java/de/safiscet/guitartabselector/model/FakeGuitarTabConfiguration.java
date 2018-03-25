package de.safiscet.guitartabselector.model;


import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class FakeGuitarTabConfiguration extends GuitarTabConfiguration {

    @Override
    public String getRootPath() {
        throw new NotImplementedException("Call to getRootPath for fake object");
    }

    @Override
    public Collection<String> getExcludedPaths() {
        throw new NotImplementedException("Call to getExcludedPath for fake object");
    }

    @Override
    public List<String> getFormatRanking() {
        throw new NotImplementedException("Call to getFormatRanking for fake object");
    }
}
