package org.core.services;

import com.cisco.pt.ipc.enums.ConnectType;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.sim.Network;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import com.cisco.pt.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.core.PacketTracerConnector;
import org.core.config.Constants;
import org.core.config.DeviceModelEnum;
import org.core.config.UtilCommon;
import org.core.models.GlobalNetwork;

public class DeviceService {
  private static final Logger logger = Logger.getLogger(DeviceService.class.getName());

  public static LogicalWorkspace logicalWorkspace =
      PacketTracerConnector.ipcInstance.appWindow().getActiveWorkspace().getLogicalWorkspace();
  public static Network network = PacketTracerConnector.ipcInstance.network();

  public static void setupNetworkNodes() {
    int deviceCount = network.getDeviceCount();
    ArrayList<Device> devices = new ArrayList<>();

    for (int i = 0; i < deviceCount; i++) {
      Device device = network.getDeviceAt(i);
      devices.add(device);
    }

    GlobalNetwork.getInstance().setupNetworkNodes(devices);
    GlobalNetwork.getInstance().setNodesConnections();
    logger.log(Level.FINE, "Setup network nodes");
  }

  public static ArrayList<Device> getAllDevices() {
    ArrayList<Device> allDevices = new ArrayList<>();
    int deviceCount = network.getDeviceCount();

    for (int i = 0; i < deviceCount; i++) {
      Device device = network.getDeviceAt(i);
      allDevices.add(device);
    }

    return allDevices;
  }

  public static Device getDeviceByName(String name) {
    return network.getDevice(name);
  }

  public static Device addDevice(DeviceType deviceType, String model, int x, int y) {
    String deviceName = logicalWorkspace.addDevice(deviceType, model, x, y);
    return getDeviceByName(deviceName);
  }

  public static void linkNetworkDeviceToEndDevices(
      Device networkDevice, ArrayList<Device> endDevices) {
    List<String> networkDevicePorts = networkDevice.getPorts();
    List<String> filteredPorts =
        new ArrayList<>(
            networkDevicePorts.stream().filter(port -> !port.contains("Vlan")).toList());

    System.out.println("PORT" + filteredPorts);
    for (Device endDevice : endDevices) {
      logicalWorkspace.createLink(
          endDevice.getName(),
          Constants.DEFAULT_PC_INTERFACE,
          networkDevice.getName(),
          filteredPorts.getFirst(),
          ConnectType.ETHERNET_STRAIGHT);

      if (!filteredPorts.isEmpty()) {
        filteredPorts.removeFirst();
      }
    }
  }

  public static void linkTwoDevicesRandomPorts(Device device1, Device device2) {
    List<String> filteredPorts1 =
        new ArrayList<>(
            device1.getPorts().stream().filter(port -> !port.contains("Vlan")).toList());

    List<String> filteredPorts2 =
        new ArrayList<>(
            device1.getPorts().stream().filter(port -> !port.contains("Vlan")).toList());

    logicalWorkspace.createLink(
        device1.getName(),
        filteredPorts1.getLast(),
        device2.getName(),
        filteredPorts2.getLast(),
        ConnectType.ETHERNET_STRAIGHT);
  }

  public static void deleteAllDevices() {
    List<String> deviceNames = getAllDevices().stream().map(Device::getName).toList();

    for (String name : deviceNames) {
      logicalWorkspace.removeDevice(name);
    }

    ArrayList<Device> devices = DeviceService.getAllDevices();
    System.out.println("Devices size after delete " + devices.size());
  }

  public static void addSubnetV1(Integer deviceCount) {
    ArrayList<Device> devices = addDeviceGroup(deviceCount, 300, 300, 60);
    Device networkDevice =
        addDevice(
            DeviceModelEnum.SWITCH_2950_24.getDeviceType(),
            DeviceModelEnum.SWITCH_2950_24.getModel(),
            200,
            200);

    DeviceService.linkNetworkDeviceToEndDevices(networkDevice, devices);
  }

  public static void addSubnet(
      Integer deviceCount,
      Pair<Integer, Integer> endDevicesCoords,
      DeviceModelEnum deviceObjectEnum,
      Pair<Integer, Integer> networkDeviceCoords) {

    Integer endDeviceXCoordinate = endDevicesCoords != null ? endDevicesCoords.getFirst() : 300;
    Integer endDeviceYCoordinate = endDevicesCoords != null ? endDevicesCoords.getSecond() : 300;

    ArrayList<Device> devices =
        DeviceService.addDeviceGroup(deviceCount, endDeviceXCoordinate, endDeviceYCoordinate, 60);

    int networkDeviceXCoordinate =
        networkDeviceCoords != null ? networkDeviceCoords.getFirst() : 200;
    int networkDeviceYCoordinate =
        networkDeviceCoords != null ? networkDeviceCoords.getSecond() : 200;

    Device networkDevice =
        DeviceService.addDevice(
            deviceObjectEnum.getDeviceType(),
            deviceObjectEnum.getModel(),
            networkDeviceXCoordinate,
            networkDeviceYCoordinate);

    DeviceService.linkNetworkDeviceToEndDevices(networkDevice, devices);
  }

  public static void addRandomNetworkV2() {
    int minXboundary = 300;
    int minYboundary = 50;

    int xBoundary = 1000;
    int yBoundary = 450;

    int maxSubnetsCount = 3;
    int maxDeviceCount = 6;

    Random random = new Random();
    int subnetCount = random.nextInt(maxSubnetsCount);

    int maxTransitNodes = random.nextInt(maxSubnetsCount);
    int transitNodesCount = random.nextInt(maxTransitNodes);

    List<Device> transitDevices = new ArrayList<>();

    // creating transit nodes
    for (int i = 0; i < transitNodesCount; i++) {
      int randomTransitXCoordinate = minXboundary + random.nextInt(xBoundary - minXboundary);
      int randomTransitYCoordinate = minYboundary + random.nextInt(yBoundary - minYboundary);

      Device device =
          DeviceService.addDevice(
              DeviceModelEnum.SWITCH_3560_24PS.getDeviceType(),
              DeviceModelEnum.SWITCH_3560_24PS.getModel(),
              randomTransitXCoordinate,
              randomTransitYCoordinate);

      for (Device transitDevice : transitDevices) {
        if (random.nextBoolean()) {
          DeviceService.linkTwoDevicesRandomPorts(transitDevice, device);
        }
      }

      transitDevices.add(device);
    }

    for (int i = 0; i < subnetCount; i++) {

      // create subnet
      int randomSubnetXCoordinate = minXboundary + random.nextInt(xBoundary - minXboundary);
      int randomSubnetYCoordinate = minYboundary + random.nextInt(yBoundary - minYboundary);

      int devicesCount = random.nextInt(maxDeviceCount);
      DeviceService.addSubnet(
          devicesCount,
          new Pair<>(),
          DeviceModelEnum.SWITCH_3560_24PS,
          new Pair<>(randomSubnetXCoordinate, randomSubnetYCoordinate));
    }
  }

  public static void addRandomNetwork(Integer netDeviceCount, Integer endDeviceCount) {

    int minXboundary = 300;
    int minYboundary = 50;
    int xBoundary = 1000;
    int yBoundary = 450;

    Integer totalEndDeviceCount = endDeviceCount;
    Random random = new Random();
    int switchL3DevicesCount = random.nextInt(netDeviceCount);

    for (int i = 0; i < switchL3DevicesCount; i++) {
      int randomXCoordinate = minXboundary + random.nextInt(xBoundary - minXboundary);
      int randomYCoordinate = minYboundary + random.nextInt(yBoundary - minYboundary);

      Device networkDevice =
          DeviceService.addDevice(
              DeviceModelEnum.SWITCH_3560_24PS.getDeviceType(),
              DeviceModelEnum.SWITCH_3560_24PS.getModel(),
              randomXCoordinate,
              randomYCoordinate);
    }

    int switchDevicesCount = netDeviceCount - switchL3DevicesCount;

    for (int i = 0; i < switchDevicesCount; i++) {
      int randomXCoordinate = minXboundary + random.nextInt(xBoundary - minXboundary);
      int randomYCoordinate = minYboundary + random.nextInt(yBoundary - minYboundary);

      Device netDevice =
          addDevice(
              DeviceModelEnum.SWITCH_2950_24.getDeviceType(),
              DeviceModelEnum.SWITCH_2950_24.getModel(),
              randomXCoordinate,
              randomYCoordinate);

      if (totalEndDeviceCount <= 0) continue;
      int randomEndDeviceCount = random.nextInt(totalEndDeviceCount);

      ArrayList<Device> devices =
          addDeviceGroup(
              randomEndDeviceCount, randomXCoordinate + 50, randomYCoordinate + 50, null);
      totalEndDeviceCount -= randomEndDeviceCount;

      linkNetworkDeviceToEndDevices(netDevice, devices);
    }
  }

  public static Device addRandomDevice(Integer xBoundary, Integer yBoundary) {
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

    Pair<Double, Double> RandomCoords =
        UtilCommon.getRandomCoordinatesWithBoundaries(xBoundary, yBoundary);

    double randomXCoordinate = RandomCoords.getFirst();
    double randomYCoordinate = RandomCoords.getSecond();

    String deviceName =
        logicalWorkspace.addDevice(
            (DeviceType) DevicesArray[randomDeviceIndex][0],
            (String) DevicesArray[randomDeviceIndex][1],
            randomXCoordinate,
            randomYCoordinate);

    return getDeviceByName(deviceName);
  }

  public static ArrayList<Device> addDeviceGroup(
      Integer count, Integer xCoord, Integer yCoord, Integer step) {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    ArrayList<Device> devices = new ArrayList<>();

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
          String deviceName =
              logicalWorkspace.addDevice(
                  DeviceModelEnum.LAPTOP.getDeviceType(),
                  DeviceModelEnum.LAPTOP.getModel(),
                  currentXCoord,
                  currentYCoord);

          Device createdDevice = getDeviceByName(deviceName);
          devices.add(createdDevice);

          deviceCount++;
          System.out.println("device count " + deviceCount);
          int deviceSize = DeviceService.getAllDevices().size();
          System.out.println("device size " + deviceSize);
        }
      }
    }

    return devices;
  }
}
