package de.safiscet.guitartabselector;

import java.net.URL;
import java.util.ResourceBundle;
import de.safiscet.guitartabselector.exceptions.InvalidConfigurationException;
import de.safiscet.guitartabselector.exceptions.NoSuchGuitarTabException;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.service.GuitarTabConfigurationService;
import de.safiscet.guitartabselector.service.GuitarTabSelectorFactory;
import de.safiscet.guitartabselector.service.RandomGuitarTabService;
import de.safiscet.guitartabselector.util.FormatUtils;
import de.safiscet.guitartabselector.util.GuitarTabUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabSelectorController implements Initializable {

    private RandomGuitarTabService randomGuitarTabService;
    private GuitarTab currentTab;
    private final ObservableList<String> formats = FXCollections.observableArrayList();
    private final GuitarTabConfigurationService configurationService = new GuitarTabConfigurationService();
    private GuitarTabConfiguration config;

    @FXML
    public Label currentTabTitle;
    @FXML
    public Label currentTabPath;
    @FXML
    public ListView<String> formatsListView;


    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        try {
            config = configurationService.getFromJsonFile(getClass().getResource("/dummy-data/config.json").getPath());
        } catch (final InvalidConfigurationException e) {
            handleException(e);
        }
        final GuitarTabSelectorFactory guitarTabSelectorFactory = new GuitarTabSelectorFactory();
        randomGuitarTabService = guitarTabSelectorFactory.createRandomGuitarTabService(config);
        formatsListView.setItems(formats);
        //TODO: show default view if no tab is loaded or try to load the first tab automatically
        //TODO: If no config is loaded, ask to create/load one
    }


    public void previousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            updateUI();
        } catch (final NoSuchGuitarTabException e) {
            handleException(e);
        }
    }


    public void openTab() {
        try {
            GuitarTabUtils.openDefaultGuitarTab(currentTab, config);
        } catch (final NoSuchGuitarTabException e) {
            handleException(e);
        }
    }


    public void nextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            updateUI();
        } catch (final NoSuchGuitarTabException e) {
            handleException(e);
        }
    }


    private void updateUI() {
        //TODO: maybe use binding
        currentTabTitle.setText(currentTab.getName());
        currentTabPath.setText(currentTab.getPath());
        formats.setAll(FormatUtils.getOrderedFormats(currentTab, config.getFormatRanking()));
    }


    private void handleException(final Exception e) {
        //TODO: Handle some exceptions later individually, e.g. ask to start again if no next tab exists. This is just the default error handling!
        //TODO: Maybe create exception mapper or superclasses to differ between user and technical exceptions
        //TODO: differ between error, warning and info
        final Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText(null); // removes the big header text
        alert.show();
        // TODO: remove this later or use actual logging
        e.printStackTrace();
    }
}
