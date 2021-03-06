package de.safiscet.guitartabselector; /**
 * Created by Stefan Fritsch on 04.06.2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends Application {
    public static void main(final String[] args) {
        launch(args);
    }


    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Parent root = FXMLLoader.load(getClass().getResource("/fxml/guitarTabSelector.fxml"));

        final Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("Guitar Tab Selector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}