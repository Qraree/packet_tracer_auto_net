package org.core.gui.cells;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import org.core.gui.controllers.DeviceCardController;
import org.core.models.NetworkNode;

public class DeviceCardCell extends ListCell<NetworkNode> {
  private Parent root;
  private DeviceCardController controller;

  public DeviceCardCell() {

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/device_card.fxml"));
      root = loader.load();
      controller = loader.getController();
    } catch (IOException exc) {
      throw new RuntimeException(exc);
    }
  }

  @Override
  protected void updateItem(NetworkNode device, boolean empty) {
    super.updateItem(device, empty);
    if (empty || device == null) {
      setGraphic(null);
    } else {
      controller.setDevice(device);
      setGraphic(root);
    }
  }
}
