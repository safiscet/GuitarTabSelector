import exceptions.InvalidConfigurationException;
import exceptions.NoSuchGuitarTabException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import service.GuitarTabConfigurationService;
import service.GuitarTabDirectoryService;
import service.RandomGuitarTabService;
import util.FormatUtils;
import util.GuitarTabUtils;

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
            config = configurationService.getFromJsonFile("config.json");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        randomGuitarTabService = new RandomGuitarTabService(config, new GuitarTabDirectoryService(config));
        formatsListView.setItems(formats);
    }

    public void previousTab() {
        try {
            currentTab = randomGuitarTabService.getPreviousTab();
            updateUI();
        } catch (NoSuchGuitarTabException e) {
            e.printStackTrace();
        }
    }

    public void openTab() {
        GuitarTabUtils.openDefaultGuitarTab(currentTab, config);
    }

    public void nextTab() {
        try {
            currentTab = randomGuitarTabService.getNextTab();
            updateUI();
        } catch (NoSuchGuitarTabException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        currentTabTitle.setText(currentTab.getName());
        currentTabPath.setText(currentTab.getPath());
        formats.setAll(FormatUtils.getOrderedFormats(currentTab, config.getFormatRanking()));
    }
}
