package org.core.models;

import com.cisco.pt.ipc.sim.Port;

public class NetworkNodeConnection {
  public NetworkNode connectedNode;
  public Port connectedPort;

  public NetworkNodeConnection(NetworkNode connectedNode, Port connectedPort) {
    this.connectedNode = connectedNode;
    this.connectedPort = connectedPort;
  }

  public NetworkNode getConnectedNode() {
    return connectedNode;
  }

  public Port getConnectedPort() {
    return connectedPort;
  }
}
