package org.core.gui.controllers;

import com.cisco.pt.IPAddress;
import com.cisco.pt.impl.IPAddressImpl;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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

  public void configureReadyNetwork(ActionEvent actionEvent) {
    ArrayList<Device> devices = deviceManager.getAllDevices();

    ArrayList<Device> networkDevices =
        devices.stream()
            .filter(device -> device.getType() == DeviceType.SWITCH)
            .collect(Collectors.toCollection(ArrayList::new));

    int subnetIndex = 2;
    for (Device netDevice : networkDevices) {
      System.out.println(netDevice.getName());
      VLANManager vlanManager = (VLANManager) netDevice.getProcess("VlanManager");
      vlanManager.addVlan(subnetIndex, "subnet_" + subnetIndex);

      List<String> ports = netDevice.getPorts();

      int pcIndex = 2;

      for (String portName : ports) {
        Port port = netDevice.getPort(portName);
        Cable link = (Cable) port.getLink();

        if (link != null) {

          if (port instanceof SwitchPort switchPort) {
            switchPort.setAccessVlan(subnetIndex);
          }

          // dto router fix
          Port otherPort = link.getPort1();
          Port netDevicePort = link.getPort2();
          Device otherDevice = otherPort.getOwnerDevice();
          Device netDevicce = netDevicePort.getOwnerDevice();
          String PortName = netDevicce.getName();

          System.out.println(netDevicce.getName());

          boolean isPersonalDevice =
              Arrays.asList(DeviceType.PC, DeviceType.LAPTOP).contains(otherDevice.getType());

          if (isPersonalDevice) {
            IPAddress defaultGatewayAddress =
                new IPAddressImpl("192.168." + (subnetIndex - 1) + ".1");
            IPAddress subnetMask = new IPAddressImpl("255.255.255.0");
            IPAddress subnetAddress =
                new IPAddressImpl("192.168." + (subnetIndex - 1) + "." + pcIndex);

            HostPort hostPort = (HostPort) otherDevice.getPort("FastEthernet0");
            hostPort.setIpSubnetMask(subnetAddress, subnetMask);
            hostPort.setDefaultGateway(defaultGatewayAddress);
            pcIndex++;
          }

          boolean isRouterDevice = otherDevice.getType() == DeviceType.ROUTER;

          if (isRouterDevice
              && otherDevice instanceof Router router
              && otherPort instanceof RouterPort routerPort) {

            System.out.println("Router " + router.getName() + " " + routerPort.getName());

            if (port instanceof SwitchPort switchPort) {
              switchPort.setAccessPort(false);
            }

            routerPort.setPower(true);
            router.addSubInt(PortName, subnetIndex);
            router.changePortEncapsulation(PortName, "dot1Q");

            IPAddress subnetMask = new IPAddressImpl("255.255.255.0");
            IPAddress subnetAddress = new IPAddressImpl("192.168." + (subnetIndex - 1) + ".1");
            routerPort.setIpSubnetMask(subnetAddress, subnetMask);
          }
        }
      }
      subnetIndex++;
    }
  }

  public void setDeviceManager(DeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }
}
