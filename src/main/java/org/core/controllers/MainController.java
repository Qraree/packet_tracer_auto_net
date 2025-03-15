package org.core.controllers;

import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {
  public LogicalWorkspace logicalWorkspace;
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
    loadStackPage("/fxml/device_page.fxml");
  }

  public void configurationPage(ActionEvent actionEvent) throws IOException {
    loadStackPage("/fxml/configuration_page.fxml");
  }

  private void loadStackPage(String location) throws IOException {
    URL locationURL = getClass().getResource(location);

    if (locationURL == null) {
      throw new RuntimeException("Location not found");
    }

    Parent root = FXMLLoader.load(locationURL);

    stackPane.getChildren().removeAll();
    stackPane.getChildren().setAll(root);
  }

  public void setLogicalWorkspace(LogicalWorkspace logicalWorkspace) {
    this.logicalWorkspace = logicalWorkspace;
  }
}
