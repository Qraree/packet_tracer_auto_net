package org.core.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.core.PacketTracerConnector;

public class MainWrapperController implements Initializable {
  public String mainPagePath = "/fxml/main.fxml";
  public String connectionPagePath = "/fxml/pt_connection.fxml";

  @FXML private StackPane stackWrapperPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    boolean isConnectionActive = PacketTracerConnector.isPacketSessionRunning();
    String pagePath = isConnectionActive ? mainPagePath : connectionPagePath;

    try {
      loadStackPage(pagePath);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void mainPage() throws IOException {
    loadStackPage(mainPagePath);
  }

  private FXMLLoader loadStackPage(String location) throws IOException {

    URL locationURL = getClass().getResource(location);

    if (locationURL == null) {
      throw new RuntimeException("Location not found");
    }

    FXMLLoader loader = new FXMLLoader(locationURL);
    Parent root = loader.load();

    if (location.equals(connectionPagePath)) {
      PTConnectionPageController ptConnectionPageController = loader.getController();
      ptConnectionPageController.setMainWrapper(this);
    }

    stackWrapperPane.getChildren().removeAll();
    stackWrapperPane.getChildren().setAll(root);

    return loader;
  }
}
