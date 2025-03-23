package org.core.gui;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUIManager {
  Stage primaryStage;

  public String mainPagePath = "/fxml/main_wrapper.fxml";

  public GUIManager(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void init() throws IOException {

    URL location = getClass().getResource(mainPagePath);

    if (location == null) {
      throw new RuntimeException("FXML file not found");
    }

    FXMLLoader loader = new FXMLLoader(location);
    Parent root = loader.load();

    Scene scene = new Scene(root, 1000, 700);
    primaryStage.setScene(scene);

    configureIcon(primaryStage);
    primaryStage.setTitle("PT Auto Network");
    primaryStage.setResizable(false);
    primaryStage.show();

    primaryStage.setOnCloseRequest(_ -> System.exit(0));
  }

  private void configureIcon(Stage primaryStage) {

    String iconPath = "/images/router.png";

    try {
      primaryStage
          .getIcons()
          .add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));

      if (System.getProperty("os.name").toLowerCase().contains("mac")) {
        Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(
            Toolkit.getDefaultToolkit().getImage(getClass().getResource(iconPath)));
      }
    } catch (Exception e) {
      System.err.println("Failed to load icon: " + iconPath);
    }
  }
}
