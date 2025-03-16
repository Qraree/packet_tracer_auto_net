package org.core.gui.controllers;

import org.core.DeviceManager;

public class PageController {
  public static DeviceManager deviceManager;

  public static void setDeviceManager(DeviceManager deviceManager) {
    PageController.deviceManager = deviceManager;
  }
}
