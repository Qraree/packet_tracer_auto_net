package org.core.gui.controllers;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.core.models.NetworkNode;

public class DeviceCardController {

  public ImageView deviceImage;
  public Label deviceName;
  public Label deviceDescription;

  public void setDevice(NetworkNode device) {
    deviceImage.setImage(new Image(device.getImagePath()));
    deviceName.setText(device.getName());
    deviceDescription.setText(device.getModel());
  }
}
