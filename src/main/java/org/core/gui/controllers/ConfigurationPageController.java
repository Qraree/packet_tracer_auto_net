package org.core.gui.controllers;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.core.config.DeviceModelEnum;
import org.core.gui.GUIValidator;
import org.core.models.GlobalNetwork;
import org.core.models.NetworkNode;
import org.core.services.DeviceService;
import org.core.services.NetworkConfigurationService;

public class ConfigurationPageController implements Initializable {
  private static final Logger logger =
      Logger.getLogger(ConfigurationPageController.class.getName());

  public TextField randomCount;
  public TextField subnetDeviceCount;
  public ChoiceBox<String> subnetNetworkDeviceChoice;

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

    if (GUIValidator.validateNumberInput(subnetDeviceCount.getText(), 0, 20)) return;
    int deviceCount = Integer.parseInt(subnetDeviceCount.getText());

    DeviceService.addDeviceGroup(deviceCount, 300, 300, 60);
    DeviceService.addDevice(DeviceType.SWITCH, selectedNetworkDevice, 200, 200);
  }

  public void configureFinalNetwork() {
    logger.log(Level.INFO, "Configuring final network");
    ArrayList<Device> devices = DeviceService.getAllDevices();
    NetworkConfigurationService.configureFinalNetwork(devices);
    logger.log(Level.FINE, "Network configuration finished");
  }

  public void configureFinalNetworkRandomV2() {
    logger.log(Level.INFO, "Configuring final network");
    ObservableList<NetworkNode> networkNodes = GlobalNetwork.getInstance().getNetworkNodes();
    NetworkConfigurationService.configFinalNetworkRandomV2(networkNodes);
    logger.log(Level.FINE, "Network configuration finished");
  }
}
