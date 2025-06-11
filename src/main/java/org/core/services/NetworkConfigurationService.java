package org.core.services;

import com.cisco.pt.IPAddress;
import com.cisco.pt.impl.IPAddressImpl;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import com.cisco.pt.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import org.core.config.CommandBuilder;
import org.core.config.Constants;
import org.core.config.UtilCommon;
import org.core.models.NetworkNode;
import org.core.models.NetworkNodeConnection;

public class NetworkConfigurationService {

  // extract to class
  public static void configFinalNetworkRandomV2(ObservableList<NetworkNode> networkNodes) {
    CanvasService.clearLayer(Constants.CANVAS_LAYER);
    int subnetIndex = 2;

    ArrayList<NetworkNode> networkDevices =
        networkNodes.stream()
            .filter(node -> Constants.NETWORK_DEVICE_TYPES.contains(node.getStringType()))
            .filter(node -> !node.getConnections().isEmpty())
            .collect(Collectors.toCollection(ArrayList::new));

    String privateSubnetPrefix = "192.168.";
    String subnetNamePrefix = "subnet_";

    for (NetworkNode networkDeviceNode : networkDevices) {
      setupConfigureTerminalMode(networkDeviceNode);
      VLANManager vlanManager =
          (VLANManager) networkDeviceNode.getDevice().getProcess(Constants.VLAN_MANAGER_PROCESS);
      vlanManager.addVlan(subnetIndex, subnetNamePrefix + subnetIndex);

      ArrayList<NetworkNodeConnection> nodeConnections = networkDeviceNode.getConnections();

      int endDeviceIndex = 2;
      boolean endDeviceFound = false;

      for (NetworkNodeConnection nodeConnection : nodeConnections) {
        NetworkNode connectedNode = nodeConnection.getConnectedNode();
        boolean isConnectedToEndDevice =
            Constants.END_DEVICE_TYPES.contains(connectedNode.getStringType());

        if (isConnectedToEndDevice) {
          endDeviceFound = true;
          if (nodeConnection.getConnectedPort() instanceof SwitchPort switchPort) {
            switchPort.setAccessPort(true);
            switchPort.setAccessVlan(subnetIndex);
          }

          IPAddress defaultGatewayAddress =
              new IPAddressImpl(privateSubnetPrefix + subnetIndex + ".1");
          IPAddress subnetMask = new IPAddressImpl(Constants.DEFAULT_SUBNET_MASK);
          IPAddress subnetAddress =
              new IPAddressImpl(privateSubnetPrefix + subnetIndex + "." + endDeviceIndex);

          HostPort hostPort = (HostPort) nodeConnection.getOtherPort();
          hostPort.setIpSubnetMask(subnetAddress, subnetMask);
          hostPort.setDefaultGateway(defaultGatewayAddress);

          Pair<Integer, Integer> noteCoordinates =
              UtilCommon.getDeviceNoteCoordinates(networkDeviceNode, connectedNode);

          CanvasService.addDeviceNote(
              noteCoordinates.getFirst(), noteCoordinates.getSecond(), subnetAddress.toString());

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
      if (endDeviceFound) subnetIndex++;
    }

    NetworkLayerConfiguration(networkNodes, subnetIndex);
  }

  private static void NetworkLayerConfiguration(
      ObservableList<NetworkNode> networkNodes, int subnetIndex) {
    ArrayList<NetworkNode> multilayerSwitches =
        networkNodes.stream()
            .filter(node -> Constants.MULTILAYER_SWITCH_TYPES.contains(node.getStringType()))
            .filter(node -> !node.getConnections().isEmpty())
            .collect(Collectors.toCollection(ArrayList::new));

    ArrayList<NetworkNode> pastSwitches = new ArrayList<>();

    for (NetworkNode switchL3Node : multilayerSwitches) {

      setupConfigureTerminalMode(switchL3Node);
      setL3SwitchAccessVlanPorts(switchL3Node);

      ArrayList<NetworkNodeConnection> nodeConnections = switchL3Node.getConnections();
      for (NetworkNodeConnection nodeConnection : nodeConnections) {
        NetworkNode connectedNode = nodeConnection.getConnectedNode();

        boolean isConnectedToSwitch = connectedNode.getType() == DeviceType.SWITCH;
        if (isConnectedToSwitch) {
          configureMultiLayerVlan(connectedNode, switchL3Node);
        }

        boolean isConnectedToSwitchL3 =
            Arrays.asList(DeviceType.MULTI_LAYER_SWITCH, DeviceType.SWITCH3650)
                .contains(connectedNode.getType());

        if (isConnectedToSwitchL3) {
          if (pastSwitches.contains(connectedNode)) continue;

          TerminalLine terminalLine = switchL3Node.getDevice().getCommandLine();

          // port.getIpAddress IPC error <IPC call "getIpAddress" not found> bruh.
          // so will get it through terminal
          terminalLine.enterCommand("interface " + nodeConnection.getConnectedPort().getName());
          terminalLine.enterCommand(Constants.TERMINAL_NO_SWITCHPORT_COMMAND);
          String switchIpCommand =
              CommandBuilder.buildIpAddressDefaultSubnetCommand(subnetIndex, 1, null);
          terminalLine.enterCommand(switchIpCommand);

          terminalLine.enterCommand(Constants.TERMINAL_ROUTER_OSPF_COMMAND);
          String ospfNetworkCommand = CommandBuilder.buildOspfSubnetCommand(subnetIndex, 0, null);
          terminalLine.enterCommand(ospfNetworkCommand);

          TerminalLine otherTerminalLine = connectedNode.getDevice().getCommandLine();

          otherTerminalLine.enterCommand("interface " + nodeConnection.getOtherPort().getName());
          otherTerminalLine.enterCommand(Constants.TERMINAL_NO_SWITCHPORT_COMMAND);
          String otherIpCommand =
              CommandBuilder.buildIpAddressDefaultSubnetCommand(subnetIndex, 254, null);
          otherTerminalLine.enterCommand(otherIpCommand);

          otherTerminalLine.enterCommand(Constants.TERMINAL_ROUTER_OSPF_COMMAND);
          otherTerminalLine.enterCommand(ospfNetworkCommand);

          subnetIndex++;
        }
      }

      pastSwitches.add(switchL3Node);
      setOSPFOnSwitchL3Ports(switchL3Node);
    }
  }

  private static void setL3SwitchAccessVlanPorts(NetworkNode switchL3Node) {
    VLANManager vlanManager = getVlanManager(switchL3Node);
    int vlanCount = vlanManager.getVlanCount();

    for (int index = 0; index < vlanCount; index++) {
      VLAN vlan = vlanManager.getVlanAt(index);
      if (UtilCommon.isValidVlanNumber(vlan)) {
        configureVlanOnSwitch(vlanManager, vlan);
        configureAccessPorts(switchL3Node, vlan);
      }
    }
  }

  private static void configureMultiLayerVlan(NetworkNode nodeForVlan, NetworkNode multiLayerNode) {
    VLANManager sourceVlanManager = getVlanManager(nodeForVlan);
    VLANManager targetVlanManager = getVlanManager(multiLayerNode);
    int vlanCount = sourceVlanManager.getVlanCount();

    for (int index = 0; index < vlanCount; index++) {
      VLAN vlan = sourceVlanManager.getVlanAt(index);
      if (UtilCommon.isValidVlanNumber(vlan)) {
        configureVlanOnSwitch(targetVlanManager, vlan);
      }
    }
  }

  private static void setOSPFOnSwitchL3Ports(NetworkNode switchL3Node) {
    VLANManager vlanManager = getVlanManager(switchL3Node);
    int vlanCount = vlanManager.getVlanCount();

    TerminalLine terminalLine = switchL3Node.getDevice().getCommandLine();
    terminalLine.enterCommand(Constants.TERMINAL_ROUTER_OSPF_COMMAND);
    for (int index = 0; index < vlanCount; index++) {
      VLAN vlan = vlanManager.getVlanAt(index);
      if (UtilCommon.isValidVlanNumber(vlan)) {

        String ospfNetworkCommand =
            CommandBuilder.buildOspfSubnetCommand(vlan.getVlanNumber(), 0, null);
        terminalLine.enterCommand(ospfNetworkCommand);
      }
    }
  }

  private static VLANManager getVlanManager(NetworkNode node) {
    return (VLANManager) node.getDevice().getProcess(Constants.VLAN_MANAGER_PROCESS);
  }

  private static void configureVlanOnSwitch(VLANManager vlanManager, VLAN vlan) {
    vlanManager.addVlan(vlan.getVlanNumber(), vlan.getName());
    vlanManager.addVlanInt(vlan.getVlanNumber());
    setVlanInterfaceIp(vlanManager, vlan);
  }

  private static void setVlanInterfaceIp(VLANManager vlanManager, VLAN vlan) {
    RouterPort vlanInterface = vlanManager.getVlanInt(vlan.getVlanNumber());
    IPAddress subnetMask = new IPAddressImpl(Constants.DEFAULT_SUBNET_MASK);
    IPAddress subnetAddress = new IPAddressImpl("192.168." + vlan.getVlanNumber() + ".1");
    vlanInterface.setIpSubnetMask(subnetAddress, subnetMask);
  }

  private static void configureAccessPorts(NetworkNode switchNode, VLAN vlan) {
    TerminalLine terminalLine = switchNode.getDevice().getCommandLine();
    for (NetworkNodeConnection connection : switchNode.getConnections()) {
      NetworkNode connectedNode = connection.getConnectedNode();
      if (Constants.END_DEVICE_TYPES.contains(connectedNode.getStringType())) {
        Port connectedPort = connection.getConnectedPort();
        terminalLine.enterCommand("interface " + connectedPort.getName());
        terminalLine.enterCommand("switchport access vlan " + vlan.getVlanNumber());
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
}
