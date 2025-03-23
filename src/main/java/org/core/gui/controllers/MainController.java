package org.core.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.core.services.DeviceService;

public class MainController implements Initializable {
  private final String devicePagePath = "/fxml/device_page.fxml";
  private final String configPagePath = "/fxml/configuration_page.fxml";

  public DevicePageController deviceController;
  public ConfigurationPageController configurationController;
  public DeviceService deviceService;

  @FXML private StackPane stackPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      loadStackPage(devicePagePath);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void devicePage() throws IOException {
    FXMLLoader loader = loadStackPage(devicePagePath);

    deviceController = loader.getController();
  }

  public void configurationPage() throws IOException {
    FXMLLoader loader = loadStackPage(configPagePath);

    configurationController = loader.getController();
    configurationController.setDeviceService(deviceService);
  }

  private FXMLLoader loadStackPage(String location) throws IOException {
    URL locationURL = getClass().getResource(location);

    if (locationURL == null) {
      throw new RuntimeException("Location not found");
    }

    FXMLLoader loader = new FXMLLoader(locationURL);
    Parent root = loader.load();

    stackPane.getChildren().removeAll();
    stackPane.getChildren().setAll(root);

    return loader;
  }

  public void setDeviceManager(DeviceService deviceService) {
    this.deviceService = deviceService;
  }
}
