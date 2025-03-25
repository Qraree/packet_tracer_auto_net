package org.core.models;

import com.cisco.pt.ipc.sim.Port;

public class NetworkNodeConnection {
  public NetworkNode connectedNode;
  public Port connectedPort;
  public Port otherPort;

  public NetworkNodeConnection(NetworkNode connectedNode, Port connectedPort, Port otherPort) {
    this.connectedNode = connectedNode;
    this.connectedPort = connectedPort;
    this.otherPort = otherPort;
  }

  public NetworkNode getConnectedNode() {
    return connectedNode;
  }

  public Port getConnectedPort() {
    return connectedPort;
  }

  public Port getOtherPort() {
    return otherPort;
  }
}
