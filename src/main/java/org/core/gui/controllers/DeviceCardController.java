package org.core.gui.controllers;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.core.gui.models.DeviceGUIModel;

public class DeviceCardController {

  public ImageView deviceImage;
  public Label deviceName;
  public Label deviceDescription;

  public void setDevice(DeviceGUIModel device) {
    deviceImage.setImage(new Image(device.getImagePath()));
    deviceName.setText(device.getName());
    deviceDescription.setText(device.getDescription());
  }
}
