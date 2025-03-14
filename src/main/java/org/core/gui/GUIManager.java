package org.core.gui;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {
  Stage primaryStage;

  public GUIManager(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void init() throws IOException {
    URL location = getClass().getResource("/fxml/main.fxml");

    if (location == null) {
      throw new RuntimeException("FXML file not found");
    }

    Parent root = FXMLLoader.load(location);

    Scene scene = new Scene(root, 700, 500);

    primaryStage.setTitle("PT Auto Network");
    primaryStage.setScene(scene);
    primaryStage.show();

    primaryStage.setOnCloseRequest(_ -> System.exit(0));
  }
}
