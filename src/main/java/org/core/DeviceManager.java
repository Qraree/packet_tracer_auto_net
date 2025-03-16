package org.core;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.util.Random;
import java.util.function.Function;
import org.core.config.DeviceModelEnum;

public class DeviceManager {
  LogicalWorkspace logicalWorkspace;

  public DeviceManager(LogicalWorkspace logicalWorkspace) {
    this.logicalWorkspace = logicalWorkspace;
  }

  public void addDevice(DeviceType deviceType, String model, int x, int y) {
    logicalWorkspace.addDevice(deviceType, model, x, y);
  }

  public void addRandomDevice(Integer xBoundary, Integer yBoundary) {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    if (xBoundary == null) xBoundary = 1000;
    if (yBoundary == null) yBoundary = 500;

    Object[][] DevicesArray = {
      {DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getModel()},
      {DeviceType.PC, DeviceModelEnum.PC.getModel()}
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

  public void addDeviceGroup(Integer count, Integer xCoord, Integer yCoord, Integer step) {
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
              DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getModel(), currentXCoord, currentYCoord);

          deviceCount++;
        }
      }
    }
  }
}
