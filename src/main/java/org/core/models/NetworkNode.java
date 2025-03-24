package org.core.models;

import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.sim.Port;
import java.util.ArrayList;

public class NetworkNode {
  private final String name;
  private final String description;
  private final String imagePath;
  private final Device device;
  public ArrayList<NetworkNodeConnection> connections = new ArrayList<>();

  public NetworkNode(String name, String description, String imagePath, Device device) {
    this.name = name;
    this.description = description;
    this.imagePath = imagePath;
    this.device = device;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getImagePath() {
    return imagePath;
  }

  public Device getDevice() {
    return device;
  }

  public ArrayList<NetworkNodeConnection> getConnections() {
    return connections;
  }

  public void setConnection(NetworkNode node, Port port) {
    NetworkNodeConnection newConnection = new NetworkNodeConnection(node, port);
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
