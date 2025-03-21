package org.core.operations;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.core.gui.mappers.DeviceMapper;
import org.core.gui.models.DeviceGUIModel;

public class OperationState {
  private static OperationState instance;

  UUID currentOperationUUID = null;

  List<Device> devices = new ArrayList<>();
  ObservableList<DeviceGUIModel> GUIDevices = FXCollections.observableArrayList();
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

  public List<Device> getDevices() {
    return devices;
  }

  public ObservableList<DeviceGUIModel> getGUIDevices() {
    return GUIDevices;
  }

  public void pushDevice(Device device) {
    devices.add(device);
  }

  public void removeFromDevices(String deviceName) {
    for (Device device : devices) {
      if (device.getName() == null) {
        devices.remove(device);
        continue;
      }

      if (device.getName().equals(deviceName)) {
        devices.remove(device);
        break;
      }
    }
  }

  public void pushGUIDevice(Device device) {
    GUIDevices.add(DeviceMapper.mapOneToGUIModel(device));
  }

  public void removeFromGUIDevices(String deviceName) {
    //    removeFromDevices(deviceName);
    GUIDevices.removeIf(device -> device.getName().equals(deviceName));
  }
}
