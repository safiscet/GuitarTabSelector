import controller.GuitarTabDirectoryService;
import controller.RandomGuitarTabService;
import exceptions.NoSuchGuitarTabException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.GuitarTab;
import model.GuitarTabConfiguration;
import util.FormatUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * Created by Stefan Fritsch on 05.06.2017.
 */
public class GuitarTabSelectorController implements Initializable {

    private RandomGuitarTabService randomGuitarTabService;
    private GuitarTab currentTab;
    private ObservableList<String> formats = FXCollections.observableArrayList();
    private GuitarTabConfiguration config;

    @FXML
    public Label currentTabTitle;
    @FXML
    public Label currentTabPath;
    @FXML
    public ListView<String> formatsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config = new GuitarTabConfiguration();
        config.setRootPath("C:\\Users\\frits\\Documents\\Tabs\\Fingerstyle\\Soundtracks\\Anime");
        config.setFormatRanking(FormatUtils.getDefaultFormats());
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
        Desktop desktop = Desktop.getDesktop();
        if (currentTab == null) {
            System.out.println("You have to select a guitar tab before opening it.");
            return;
        }
        File parent = new File(currentTab.getPath());
        String optimalFormat = FormatUtils.getOptimalFormat(currentTab, config.getFormatRanking());
        Path tabPath = parent.toPath().resolve(currentTab.getName() + "." + optimalFormat);
        try {
            desktop.open(tabPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
