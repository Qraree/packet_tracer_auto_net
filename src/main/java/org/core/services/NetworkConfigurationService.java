package org.core.services;

import com.cisco.pt.IPAddress;
import com.cisco.pt.impl.IPAddressImpl;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import org.core.config.Constants;
import org.core.models.NetworkNode;
import org.core.models.NetworkNodeConnection;

public class NetworkConfigurationService {

  public static void configureFinalNetwork(ArrayList<Device> devices) {

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

          Port otherPort;
          Port netDevicePort;

          if (port.getObjectUUID() == link.getPort1().getObjectUUID()) {
            otherPort = link.getPort2();
            netDevicePort = link.getPort1();
          } else {
            otherPort = link.getPort1();
            netDevicePort = link.getPort2();
          }

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

  public static void configFinalNetworkRandomV2(ObservableList<NetworkNode> networkNodes) {

    ArrayList<NetworkNode> networkDevices =
        networkNodes.stream()
            .filter(node -> Constants.NETWORK_DEVICE_TYPES.contains(node.getStringType()))
            .filter(node -> !node.getConnections().isEmpty())
            .collect(Collectors.toCollection(ArrayList::new));

    int subnetIndex = 2;
    String privateSubnetPrefix = "192.168.";
    String subnetNamePrefix = "subnet_";

    for (NetworkNode networkDeviceNode : networkDevices) {
      setupConfigureTerminalMode(networkDeviceNode);
      VLANManager vlanManager =
          (VLANManager) networkDeviceNode.getDevice().getProcess(Constants.VLAN_MANAGER_PROCESS);
      vlanManager.addVlan(subnetIndex, subnetNamePrefix + subnetIndex);

      ArrayList<NetworkNodeConnection> nodeConnections = networkDeviceNode.getConnections();

      int endDeviceIndex = 2;

      for (NetworkNodeConnection nodeConnection : nodeConnections) {
        NetworkNode connectedNode = nodeConnection.getConnectedNode();
        boolean isConnectedToEndDevice =
            Constants.END_DEVICE_TYPES.contains(connectedNode.getStringType());

        if (isConnectedToEndDevice) {
          if (nodeConnection.connectedPort instanceof SwitchPort switchPort) {
            switchPort.setAccessPort(true);
            switchPort.setAccessVlan(subnetIndex);
          }

          IPAddress defaultGatewayAddress =
              new IPAddressImpl(privateSubnetPrefix + (subnetIndex - 1) + ".1");
          IPAddress subnetMask = new IPAddressImpl(Constants.DEFAULT_SUBNET_MASK);
          IPAddress subnetAddress =
              new IPAddressImpl(privateSubnetPrefix + (subnetIndex - 1) + "." + endDeviceIndex);

          HostPort hostPort = (HostPort) nodeConnection.getOtherPort();
          hostPort.setIpSubnetMask(subnetAddress, subnetMask);
          hostPort.setDefaultGateway(defaultGatewayAddress);
          endDeviceIndex++;
        }

        boolean isConnectedToNetworkDevice =
            Constants.NETWORK_DEVICE_TYPES.contains(connectedNode.getStringType());
        if (isConnectedToNetworkDevice) {
          setupConfigureTerminalMode(networkDeviceNode);
          TerminalLine terminalLine = networkDeviceNode.getDevice().getCommandLine();
          terminalLine.enterCommand("interface " + nodeConnection.getConnectedPort().getName());
          terminalLine.enterCommand(Constants.TERMINAL_SWITCH_TO_TRUNK_COMMAND);
        }
      }
      subnetIndex++;
    }

    // network Layer Configuration
    ArrayList<NetworkNode> multilayerSwitches =
        networkNodes.stream()
            .filter(node -> Constants.MULTILAYER_SWITCH_TYPES.contains(node.getStringType()))
            .filter(node -> !node.getConnections().isEmpty())
            .collect(Collectors.toCollection(ArrayList::new));

    for (NetworkNode switchNode : multilayerSwitches) {
      setupConfigureTerminalMode(switchNode);
      configureMultiLayerVlan(switchNode, switchNode);

      ArrayList<NetworkNodeConnection> nodeConnections = switchNode.getConnections();
      for (NetworkNodeConnection nodeConnection : nodeConnections) {
        NetworkNode connectedNode = nodeConnection.getConnectedNode();

        boolean isConnectedToSwitch = connectedNode.getType() == DeviceType.SWITCH;
        if (isConnectedToSwitch) {
          configureMultiLayerVlan(connectedNode, switchNode);
        }
      }
    }
  }

  private static void setupConfigureTerminalMode(NetworkNode switchNode) {
    assert Constants.NETWORK_DEVICE_TYPES.contains(switchNode.getStringType());

    TerminalLine terminalLine = switchNode.getDevice().getCommandLine();

    String mode = terminalLine.getMode();

    if (mode.isEmpty()) {
      terminalLine.enterCommand(Constants.TERMINAL_NO_RESPONSE);
    }

    mode = terminalLine.getMode();

    if (mode.equals(Constants.TERMINAL_USER_MODE)) {
      terminalLine.enterCommand(Constants.TERMINAL_ENABLE_RESPONSE);
    }

    mode = terminalLine.getMode();
    if (mode.equals(Constants.TERMINAL_ENABLE_MODE)) {
      terminalLine.enterCommand(Constants.TERMINAL_CONFIG_RESPONSE);
    }

    terminalLine.enterCommand(Constants.TERMINAL_IP_ROUTING_COMMAND);
  }

  private static void configureMultiLayerVlan(NetworkNode nodeForVlan, NetworkNode multiLayerNode) {
    int defaultVlanNumber = 1;
    int serviceVlanNumber = 1002;

    VLANManager connectedNodeVlanManager =
        (VLANManager) nodeForVlan.getDevice().getProcess(Constants.VLAN_MANAGER_PROCESS);

    VLANManager multiLayerSwitchVlanManager =
        (VLANManager) multiLayerNode.getDevice().getProcess(Constants.VLAN_MANAGER_PROCESS);

    int vlanCount = connectedNodeVlanManager.getVlanCount();
    for (int index = 0; index < vlanCount; index++) {
      VLAN currentVlan = connectedNodeVlanManager.getVlanAt(index);

      boolean isAcceptedVlanNumber =
          currentVlan.getVlanNumber() > defaultVlanNumber
              && currentVlan.getVlanNumber() < serviceVlanNumber;

      if (isAcceptedVlanNumber) {
        multiLayerSwitchVlanManager.addVlan(currentVlan.getVlanNumber(), currentVlan.getName());

        multiLayerSwitchVlanManager.addVlanInt(currentVlan.getVlanNumber());
        RouterPort vlanInterface =
            multiLayerSwitchVlanManager.getVlanInt(currentVlan.getVlanNumber());

        IPAddress subnetMask = new IPAddressImpl(Constants.DEFAULT_SUBNET_MASK);
        IPAddress subnetAddress =
            new IPAddressImpl("192.168." + (currentVlan.getVlanNumber() - 1) + ".1");

        vlanInterface.setIpSubnetMask(subnetAddress, subnetMask);

        if (nodeForVlan.getName().equals(multiLayerNode.getName())) {
          TerminalLine terminalLine = multiLayerNode.getDevice().getCommandLine();
          for (NetworkNodeConnection nodeConnection : multiLayerNode.getConnections()) {
            NetworkNode connectedNode = nodeConnection.getConnectedNode();

            boolean isEndDevice =
                Constants.END_DEVICE_TYPES.contains(connectedNode.getStringType());
            if (isEndDevice) {
              Port connectedPort = nodeConnection.getConnectedPort();
              terminalLine.enterCommand("interface " + connectedPort.getName());
              terminalLine.enterCommand("switchport access vlan " + currentVlan.getVlanNumber());
            }
          }
        }
      }
    }
  }
}
