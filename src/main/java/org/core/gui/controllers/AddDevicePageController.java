package org.core.gui.controllers;

import com.cisco.pt.ipc.sim.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.core.config.DeviceModelEnum;
import org.core.gui.GUIValidator;
import org.core.services.DeviceService;

public class AddDevicePageController implements Initializable {
  private static final Logger logger = Logger.getLogger(AddDevicePageController.class.getName());

  public TextField randomCount;
  public TextField subnetDeviceCount;
  public ChoiceBox<String> subnetNetworkDeviceChoice;
  public TextField addRandomNetworkNetDeviceCount;
  public TextField addRandomNetworkEndDeviceCount;

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
    DeviceService.addRandomDevice(1000, 500);
  }

  public void handleAddDevicesGroupAction() {

    int startXCoord = 300;
    int startYCoord = 300;
    int step = 60;

    if (GUIValidator.validateNumberInput(randomCount.getText(), 0, 20)) return;
    int devicesCount = Integer.parseInt(randomCount.getText());

    DeviceService.addDeviceGroup(devicesCount, startXCoord, startYCoord, step);
  }

  public void handleAddSubnetAction() {
    String selectedNetworkDevice = subnetNetworkDeviceChoice.getValue();
    DeviceModelEnum deviceObjectEnum =
        DeviceModelEnum.getDeviceModelEnumByModel(selectedNetworkDevice);

    if (deviceObjectEnum == null) {
      throw new RuntimeException("Invalid device model: " + selectedNetworkDevice);
    }

    if (GUIValidator.validateNumberInput(subnetDeviceCount.getText(), 0, 20)) return;
    int deviceCount = Integer.parseInt(subnetDeviceCount.getText());

    ArrayList<Device> devices = DeviceService.addDeviceGroup(deviceCount, 300, 300, 60);
    Device networkDevice =
        DeviceService.addDevice(
            deviceObjectEnum.getDeviceType(), deviceObjectEnum.getModel(), 200, 200);
    DeviceService.linkNetworkDeviceToEndDevices(networkDevice, devices);
  }

  public void AddRandomNetwork() throws InterruptedException {
    if (GUIValidator.validateNumberInput(addRandomNetworkNetDeviceCount.getText(), 0, 30)) return;
    if (GUIValidator.validateNumberInput(addRandomNetworkEndDeviceCount.getText(), 0, 30)) return;

    DeviceService.deleteAllDevices();

    int netDeviceCount = Integer.parseInt(addRandomNetworkNetDeviceCount.getText());
    int endDeviceCount = Integer.parseInt(addRandomNetworkEndDeviceCount.getText());

    DeviceService.addRandomNetwork(netDeviceCount, endDeviceCount);
  }

  public void deleteAllDevices() {
    DeviceService.deleteAllDevices();
  }
}
