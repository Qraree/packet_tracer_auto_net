package org.core.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.core.services.DeviceService;

public class MainController implements Initializable {
  public DevicePageController deviceController;
  public ConfigurationPageController configurationController;
  public DeviceService deviceService;

  @FXML private StackPane stackPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      loadStackPage("/fxml/device_page.fxml");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void devicePage(ActionEvent actionEvent) throws IOException {
    FXMLLoader loader = loadStackPage("/fxml/device_page.fxml");

    deviceController = loader.getController();
    deviceController.setDeviceManager(deviceService);
  }

  public void configurationPage(ActionEvent actionEvent) throws IOException {
    FXMLLoader loader = loadStackPage("/fxml/configuration_page.fxml");

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
