package org.core.controllers;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
  public Button myButton;
  public LogicalWorkspace logicalWorkspace;

  @FXML
  private void handleButtonClick(ActionEvent event) {
    if (logicalWorkspace == null) {
      System.out.println("Workspace not initialized yet!");
      return;
    }

    logicalWorkspace.addDevice(DeviceType.LAPTOP, "Laptop-PT", 150, 480);
  }

  public void setLogicalWorkspace(LogicalWorkspace logicalWorkspace) {
    this.logicalWorkspace = logicalWorkspace;
  }
}
