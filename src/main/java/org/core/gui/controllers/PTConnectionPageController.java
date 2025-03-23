package org.core.gui.controllers;

import java.io.IOException;
import javafx.application.Platform;
import org.core.PacketTracerConnector;

public class PTConnectionPageController {

  MainWrapperController mainWrapperController;

  public void reloadConnection() {
    Platform.runLater(
        () -> {
          try {
            PacketTracerConnector.setupPacketTracer();

            boolean isConnectionActive = PacketTracerConnector.isPacketSessionRunning();

            if (isConnectionActive) {
              mainWrapperController.mainPage();
            }
          } catch (IOException e) {
          }
        });
  }

  public void setMainWrapper(MainWrapperController mainWrapperController) {
    this.mainWrapperController = mainWrapperController;
  }
}
