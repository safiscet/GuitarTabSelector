package de.safiscet.guitartabselector;

import de.safiscet.guitartabselector.exceptions.InvalidConfigurationException;
import de.safiscet.guitartabselector.exceptions.NoSuchGuitarTabException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import de.safiscet.guitartabselector.model.GuitarTab;
import de.safiscet.guitartabselector.model.GuitarTabConfiguration;
import de.safiscet.guitartabselector.service.GuitarTabConfigurationService;
import de.safiscet.guitartabselector.service.GuitarTabDirectoryService;
import de.safiscet.guitartabselector.service.RandomGuitarTabService;
import de.safiscet.guitartabselector.FormatUtils;
import de.safiscet.guitartabselector.GuitarTabUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabSelectorController implements Initializable {

    private RandomGuitarTabService randomGuitarTabService;
    private GuitarTab currentTab;
    private ObservableList<String> formats = FXCollections.observableArrayList();
    private GuitarTabConfigurationService configurationService = new GuitarTabConfigurationService();
    private GuitarTabConfiguration config;

    @FXML
    public Label currentTabTitle;
    @FXML
    public Label currentTabPath;
    @FXML
    public ListView<String> formatsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            config = configurationService.getFromJsonFile(getClass().getResource("/dummy-data/config.json").getPath());
        } catch (InvalidConfigurationException e) {
            handleException(e);
        }
        randomGuitarTabService = new RandomGuitarTabService(config, new GuitarTabDirectoryService(config));
        formatsListView.setItems(formats);
    }

    public void previousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            updateUI();
        } catch (NoSuchGuitarTabException e) {
            handleException(e);
        }
    }

    public void openTab() {
        try {
            GuitarTabUtils.openDefaultGuitarTab(currentTab, config);
        } catch (NoSuchGuitarTabException e) {
            handleException(e);
        }
    }

    public void nextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            updateUI();
        } catch (NoSuchGuitarTabException e) {
            handleException(e);
        }
    }

    private void updateUI() {
        currentTabTitle.setText(currentTab.getName());
        currentTabPath.setText(currentTab.getPath());
        formats.setAll(FormatUtils.getOrderedFormats(currentTab, config.getFormatRanking()));
    }

    private void handleException(Exception e) {
        //TODO: show the exception message or something later
        //TODO: Create exception mapper to differ between user and technical exceptions
        e.printStackTrace();
    }
}
