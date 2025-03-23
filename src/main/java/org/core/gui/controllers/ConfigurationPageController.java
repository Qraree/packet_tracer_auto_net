package org.core.gui.controllers;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.core.config.DeviceModelEnum;
import org.core.gui.GUIValidator;
import org.core.operations.OperationState;
import org.core.services.DeviceService;
import org.core.services.NetworkConfigurationService;

public class ConfigurationPageController implements Initializable {
  public TextField randomCount;
  public TextField subnetDeviceCount;
  public ChoiceBox<String> subnetNetworkDeviceChoice;
  public DeviceService deviceService;

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
    deviceService.addRandomDevice(1000, 500);
  }

  public void handleAddDevicesGroupAction() {

    if (deviceService == null) {
      throw new RuntimeException("No device manager available");
    }

    int startXCoord = 300;
    int startYCoord = 300;
    int step = 60;

    if (GUIValidator.validateNumberInput(randomCount.getText(), 0, 20)) return;
    int devicesCount = Integer.parseInt(randomCount.getText());

    deviceService.addDeviceGroup(devicesCount, startXCoord, startYCoord, step);
  }

  public void handleAddSubnetAction() {
    String selectedNetworkDevice = subnetNetworkDeviceChoice.getValue();

    if (GUIValidator.validateNumberInput(subnetDeviceCount.getText(), 0, 20)) return;
    int deviceCount = Integer.parseInt(subnetDeviceCount.getText());

    java.util.UUID operationUUID = java.util.UUID.randomUUID();
    OperationState.getInstance().setCurrentOperation(operationUUID);

    deviceService.addDeviceGroup(deviceCount, 300, 300, 60);
    deviceService.addDevice(DeviceType.SWITCH, selectedNetworkDevice, 200, 200);
  }

  public void configureFinalNetwork() {
    ArrayList<Device> devices = deviceService.getAllDevices();
    NetworkConfigurationService.configureFinalNetwork(devices);
  }

  public void setDeviceService(DeviceService deviceService) {
    this.deviceService = deviceService;
  }
}
