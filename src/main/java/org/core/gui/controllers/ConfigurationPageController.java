package org.core.gui.controllers;

import com.cisco.pt.ipc.enums.DeviceType;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.core.DeviceManager;
import org.core.config.DeviceModelEnum;
import org.core.gui.GUIValidator;
import org.core.operations.OperationState;

public class ConfigurationPageController implements Initializable {
  public TextField randomCount;
  public TextField subnetDeviceCount;
  public ChoiceBox<String> subnetNetworkDeviceChoice;
  public DeviceManager deviceManager;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    subnetNetworkDeviceChoice
        .getItems()
        .addAll(
            DeviceModelEnum.SWITCH_2960_24TT.getModel(),
            DeviceModelEnum.SWITCH_3560_24PS.getModel());

    subnetNetworkDeviceChoice.setValue(DeviceModelEnum.SWITCH_2960_24TT.getModel());
  }

  public void handleAddRandomDeviceAction() {
    deviceManager.addRandomDevice(1000, 500);
  }

  public void handleAddDevicesGroupAction() {

    if (deviceManager == null) {
      throw new RuntimeException("No device manager available");
    }

    int startXCoord = 300;
    int startYCoord = 300;
    int step = 60;

    if (GUIValidator.validateNumberInput(randomCount.getText(), 0, 20)) return;
    int devicesCount = Integer.parseInt(randomCount.getText());

    deviceManager.addDeviceGroup(devicesCount, startXCoord, startYCoord, step);
  }

  public void handleAddSubnetAction(ActionEvent actionEvent) {
    String selectedNetworkDevice = subnetNetworkDeviceChoice.getValue();

    if (GUIValidator.validateNumberInput(subnetDeviceCount.getText(), 0, 20)) return;
    int deviceCount = Integer.parseInt(subnetDeviceCount.getText());

    java.util.UUID operationUUID = java.util.UUID.randomUUID();
    OperationState.getInstance().setCurrentOperation(operationUUID);

    deviceManager.addDeviceGroup(deviceCount, 300, 300, 60);
    deviceManager.addDevice(DeviceType.SWITCH, selectedNetworkDevice, 200, 200);
  }

  public void setDeviceManager(DeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }
}
