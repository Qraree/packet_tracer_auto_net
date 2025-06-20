package org.core.models;

import com.cisco.pt.ipc.sim.Cable;
import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.sim.Link;
import com.cisco.pt.ipc.sim.Port;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.core.config.DeviceModelEnum;
import org.core.gui.mappers.DeviceMapper;

public class GlobalNetwork {
  private static final Logger logger = Logger.getLogger(GlobalNetwork.class.getName());
  private static GlobalNetwork instance;
  ObservableList<NetworkNode> networkNodes = FXCollections.observableArrayList();

  private GlobalNetwork() {}

  public static synchronized GlobalNetwork getInstance() {
    if (GlobalNetwork.instance == null) {
      GlobalNetwork.instance = new GlobalNetwork();
    }

    return GlobalNetwork.instance;
  }

  public void setupNetworkNodes(ArrayList<Device> devices) {
    devices.forEach(this::createNetworkNode);
  }

  public void createNodesConnection(
      String deviceName1, String deviceName2, String portName1, String portName2) {
    NetworkNode firstNode = getNodeByName(deviceName1);
    NetworkNode secondNode = getNodeByName(deviceName2);

    Port firstPort = firstNode.getDevice().getPort(portName1);
    Port secondPort = secondNode.getDevice().getPort(portName2);

    firstNode.setConnection(secondNode, firstPort, secondPort);
    secondNode.setConnection(firstNode, secondPort, firstPort);
  }

  public void deleteNodeConnections(
      String deviceName1, String deviceName2, String portName1, String portName2) {
    NetworkNode firstNode = getNodeByName(deviceName1);
    NetworkNode secondNode = getNodeByName(deviceName2);

    firstNode.deleteConnection(secondNode);
    secondNode.deleteConnection(firstNode);
  }

  public void deleteAllNodeConnections(String deviceName) {
    NetworkNode node = getNodeByName(deviceName);
    node.deleteAllConnections();
  }

  public void setNodesConnections() {
    for (NetworkNode node : networkNodes) {
      Device nodeDevice = node.getDevice();
      List<String> ports = nodeDevice.getPorts();

      for (String portName : ports) {
        Port port = nodeDevice.getPort(portName);
        Cable link = (Cable) port.getLink();

        if (link != null) {

          Port otherPort = getOtherPort(link, port);
          Device otherDevice = otherPort.getOwnerDevice();
          NetworkNode otherNode = getNodeByName(otherDevice.getName());

          if (otherNode.getName().equals(node.getName())) continue;

          boolean isConnectionAlreadyExists = node.checkForExistingConnection(otherNode);
          if (!isConnectionAlreadyExists) {
            node.setConnection(otherNode, port, otherPort);
            otherNode.setConnection(node, otherPort, port);
          }
        }
      }
    }
  }

  private Port getOtherPort(Link link, Port port) {
    assert link instanceof Cable;

    Port firstPort = ((Cable) link).getPort1();

    if (firstPort.getObjectUUID() == port.getObjectUUID()) {
      return firstPort;
    }

    return ((Cable) link).getPort2();
  }

  public ObservableList<NetworkNode> getNetworkNodes() {
    return networkNodes;
  }

  public void createNetworkNode(Device device) {
    if (device.getModel().equals(DeviceModelEnum.PDD.getModel())) return;

    logger.info("Creating network node " + device.getName());
    networkNodes.add(DeviceMapper.mapOneToNetworkNode(device));
  }

  public void deleteNetworkNode(String deviceName) {
    networkNodes.removeIf(device -> device.getName().equals(deviceName));
  }

  public NetworkNode getNodeByName(String name) {

    for (NetworkNode node : networkNodes) {
      if (node.getName().equals(name)) {
        return node;
      }
    }

    return null;
  }
}
