package org.core.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.core.gui.cells.DeviceCardCell;
import org.core.gui.models.DeviceGUIModel;
import org.core.operations.OperationState;

public class DevicePageController implements Initializable {
  public ListView<DeviceGUIModel> listview;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    ObservableList<DeviceGUIModel> devices = OperationState.getInstance().getGUIDevices();
    listview.setItems(devices);
    listview.setCellFactory(p -> new DeviceCardCell());
  }
}
