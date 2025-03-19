package org.core.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.core.gui.models.DeviceGUIModel;

public class DeviceCardController {

  public ImageView deviceImage;
  public Label deviceName;
  public Label deviceDescription;

  @FXML private AnchorPane root;

  public void setDevice(DeviceGUIModel device) {
    deviceImage.setImage(new Image(device.getImagePath()));
    deviceName.setText(device.getName());
    deviceDescription.setText(device.getDescription());
  }
}
