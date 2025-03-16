package org.core.operations;

import com.cisco.pt.ipc.sim.Device;
import java.util.UUID;

public class OperationState {
  private static OperationState instance;

  UUID currentOperationUUID;

  Device[] devices;
  SubnetOperation[] subnetOperations;

  private OperationState() {}

  public static synchronized OperationState getInstance() {
    if (OperationState.instance == null) {
      OperationState.instance = new OperationState();
    }

    return OperationState.instance;
  }

  public UUID getCurrentOperationUUID() {
    return currentOperationUUID;
  }

  public void setCurrentOperation(UUID uuid) {
    currentOperationUUID = uuid;
  }
}
