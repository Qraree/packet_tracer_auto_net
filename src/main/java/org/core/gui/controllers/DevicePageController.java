package org.core.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.core.gui.cells.DeviceCardCell;
import org.core.models.GlobalNetwork;
import org.core.models.NetworkNode;

public class DevicePageController implements Initializable {
  public ListView<NetworkNode> listview;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    ObservableList<NetworkNode> devices = GlobalNetwork.getInstance().getNetworkNodes();
    listview.setItems(devices);
    listview.setCellFactory(p -> new DeviceCardCell());
  }
}
