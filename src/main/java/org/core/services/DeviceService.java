package org.core.services;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.sim.Network;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import org.core.PacketTracerConnector;
import org.core.config.DeviceModelEnum;
import org.core.operations.OperationState;

public class DeviceService {
  public static LogicalWorkspace logicalWorkspace =
      PacketTracerConnector.ipcInstance.appWindow().getActiveWorkspace().getLogicalWorkspace();
  public static Network network = PacketTracerConnector.ipcInstance.network();

  public static void getAllDevicesInit() {
    int deviceCount = network.getDeviceCount();

    for (int i = 1; i < deviceCount; i++) {
      Device device = network.getDeviceAt(i);
      OperationState.getInstance().pushGUIDevice(device);
    }
  }

  public Device getDeviceAt(int index) {
    try {
      return network.getDeviceAt(index);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public static ArrayList<Device> getAllDevices() {
    ArrayList<Device> allDevices = new ArrayList<>();
    int deviceCount = network.getDeviceCount();

    for (int i = 1; i < deviceCount; i++) {
      Device device = network.getDeviceAt(i);
      allDevices.add(device);
    }

    return allDevices;
  }

  public static Device getDeviceByName(String name) {
    return network.getDevice(name);
  }

  public static void addDevice(DeviceType deviceType, String model, int x, int y) {
    logicalWorkspace.addDevice(deviceType, model, x, y);
  }

  public static void addRandomDevice(Integer xBoundary, Integer yBoundary) {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    if (xBoundary == null) xBoundary = 1000;
    if (yBoundary == null) yBoundary = 500;

    Object[][] DevicesArray = {
      {DeviceModelEnum.LAPTOP.getDeviceType(), DeviceModelEnum.LAPTOP.getModel()},
      {DeviceModelEnum.PC.getDeviceType(), DeviceModelEnum.PC.getModel()}
    };
    Random random = new Random();

    int randomDeviceIndex = random.nextInt(DevicesArray.length);

    Function<Double, Double> roundedValue = (value) -> Math.round(value * 100.0) / 100.0;
    double randomXCoordinate = roundedValue.apply(random.nextDouble() * xBoundary);
    double randomYCoordinate = roundedValue.apply(random.nextDouble() * yBoundary);

    logicalWorkspace.addDevice(
        (DeviceType) DevicesArray[randomDeviceIndex][0],
        (String) DevicesArray[randomDeviceIndex][1],
        randomXCoordinate,
        randomYCoordinate);
  }

  public static void addDeviceGroup(Integer count, Integer xCoord, Integer yCoord, Integer step) {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    if (step == null) step = 60;
    if (xCoord == null) xCoord = 300;
    if (yCoord == null) yCoord = 300;
    if (count == null) count = 10;

    int rows = (int) Math.sqrt(count);
    int cols = (count + rows - 1) / rows;

    int deviceCount = 0;
    for (int currentXCoord = xCoord;
        currentXCoord < rows * step + xCoord;
        currentXCoord = currentXCoord + step) {

      for (int currentYCoord = yCoord;
          currentYCoord < cols * step + yCoord;
          currentYCoord = currentYCoord + step) {

        if (deviceCount < count) {
          logicalWorkspace.addDevice(
              DeviceModelEnum.LAPTOP.getDeviceType(),
              DeviceModelEnum.LAPTOP.getModel(),
              currentXCoord,
              currentYCoord);

          deviceCount++;
        }
      }
    }
  }
}
