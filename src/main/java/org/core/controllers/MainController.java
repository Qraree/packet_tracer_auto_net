package org.core.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
  @FXML private Button button;

  @FXML
  private void handleButtonClick(ActionEvent event) {
    System.out.println("Button clicked!");
    button.setText("Clicked!");
  }
}
