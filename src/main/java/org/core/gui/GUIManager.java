package org.core.gui;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.core.DeviceManager;
import org.core.gui.controllers.MainController;

public class GUIManager {
  Stage primaryStage;
  MainController mainController;
  DeviceManager deviceManager;

  public GUIManager(Stage primaryStage, DeviceManager deviceManager) {
    this.primaryStage = primaryStage;
    this.deviceManager = deviceManager;
  }

  public void init() throws IOException {
    URL location = getClass().getResource("/fxml/main.fxml");

    if (location == null) {
      throw new RuntimeException("FXML file not found");
    }

    FXMLLoader loader = new FXMLLoader(location);
    Parent root = loader.load();

    mainController = loader.getController();
    mainController.setDeviceManager(deviceManager);

    Scene scene = new Scene(root, 1000, 700);

    primaryStage.setTitle("PT Auto Network");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();

    primaryStage.setOnCloseRequest(_ -> System.exit(0));
  }
}
