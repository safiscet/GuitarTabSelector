package model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.List;

/**
 * Created by Stefan Fritsch on 31.05.2017.
 */
public class FakeGuitarTabConfiguration extends GuitarTabConfiguration {

    @Override
    public String getRootPath() {
        throw new NotImplementedException();
    }

    @Override
    public Collection<String> getExcludedPaths() {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getFormatRanking() {
        throw new NotImplementedException();
    }
}
