package de.safiscet.guitartabselector.service;

import de.safiscet.guitartabselector.model.GuitarTabConfiguration;

/**
 * @author Stefan Fritsch
 */
public class GuitarTabSelectorFactory {

    public RandomGuitarTabService createRandomGuitarTabService(final GuitarTabConfiguration config) {
        final DirectoryVisitor directoryVisitor = new DirectoryVisitor(config);
        final GuitarTabDirectoryService guitarTabDirectoryService = new GuitarTabDirectoryService(config, directoryVisitor);
        return new RandomGuitarTabService(guitarTabDirectoryService);
    }
}
