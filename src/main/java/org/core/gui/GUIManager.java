package org.core.gui;

import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.core.gui.controllers.MainController;

public class GUIManager {
  Stage primaryStage;
  MainController mainController;
  LogicalWorkspace logicalWorkspace;

  public GUIManager(Stage primaryStage, LogicalWorkspace logicalWorkspace) {
    this.primaryStage = primaryStage;
    this.logicalWorkspace = logicalWorkspace;
  }

  public void init() throws IOException {
    URL location = getClass().getResource("/fxml/main.fxml");

    if (location == null) {
      throw new RuntimeException("FXML file not found");
    }

    FXMLLoader loader = new FXMLLoader(location);
    Parent root = loader.load();

    mainController = loader.getController();
    mainController.setLogicalWorkspace(logicalWorkspace);

    Scene scene = new Scene(root, 1000, 700);

    primaryStage.setTitle("PT Auto Network");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();

    primaryStage.setOnCloseRequest(_ -> System.exit(0));
  }
}
