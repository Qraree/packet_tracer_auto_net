package org.core.models;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.sim.Port;
import java.util.ArrayList;

public class NetworkNode {
  private final String name;
  private final String model;
  private final String imagePath;
  private final Device device;
  private final DeviceType type;
  private final ArrayList<NetworkNodeConnection> connections = new ArrayList<>();

  public NetworkNode(
      String name, String model, String imagePath, Device device, DeviceType deviceType) {
    this.name = name;
    this.model = model;
    this.imagePath = imagePath;
    this.device = device;
    this.type = deviceType;
  }

  public String getName() {
    return name;
  }

  public String getModel() {
    return model;
  }

  public String getImagePath() {
    return imagePath;
  }

  public Device getDevice() {
    return device;
  }

  public DeviceType getType() {
    return type;
  }

  public String getStringType() {
    return type.toString();
  }

  public ArrayList<NetworkNodeConnection> getConnections() {
    return connections;
  }

  public void setConnection(NetworkNode node, Port port, Port otherPort) {
    NetworkNodeConnection newConnection = new NetworkNodeConnection(node, port, otherPort);
    this.connections.add(newConnection);
  }

  public boolean checkForExistingConnection(NetworkNode node) {

    for (NetworkNodeConnection connection : connections) {
      Device currentNode = connection.connectedNode.device;
      if (currentNode.getName().equals(node.getName())) {
        return true;
      }
    }

    return false;
  }
}
