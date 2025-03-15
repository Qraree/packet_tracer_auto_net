package org.core.gui.controllers;

import com.cisco.pt.UUID;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import org.core.config.DeviceModelEnum;

public class ConfigurationPageController {
  private LogicalWorkspace logicalWorkspace;

  // todo move device methods to DeviceManager class
  public void handleAddRandomDeviceAction() {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    Object[][] DevicesArray = {
      {DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getValue()},
      {DeviceType.PC, DeviceModelEnum.PC.getValue()}
    };
    Random random = new Random();

    int randomDeviceIndex = random.nextInt(DevicesArray.length);

    Function<Double, Double> roundedValue = (value) -> Math.round(value * 100.0) / 100.0;
    double randomXCoordinate = roundedValue.apply(random.nextDouble() * 1000);
    double randomYCoordinate = roundedValue.apply(random.nextDouble() * 500);

    logicalWorkspace.addDevice(
        (DeviceType) DevicesArray[randomDeviceIndex][0],
        (String) DevicesArray[randomDeviceIndex][1],
        randomXCoordinate,
        randomYCoordinate);

    List<UUID> itemsIds = logicalWorkspace.getCanvasItemIds();
    System.out.println(itemsIds);
  }

  public void handleAddDevicesGroupAction() {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    int startXCoord = 300;
    int startYCoord = 300;
    int step = 60;

    int devicesCount = 10;

    int rows = (int) Math.sqrt(devicesCount);
    int cols = (devicesCount + rows - 1) / rows;

    int deviceCount = 0;
    for (int currentXCoord = startXCoord;
        currentXCoord < rows * step + startXCoord;
        currentXCoord = currentXCoord + step) {

      for (int currentYCoord = startYCoord;
          currentYCoord < cols * step + startYCoord;
          currentYCoord = currentYCoord + step) {

        if (deviceCount < devicesCount) {
          logicalWorkspace.addDevice(
              DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getValue(), currentXCoord, currentYCoord);

          deviceCount++;
        }
      }
    }
  }

  public void setLogicalWorkspace(LogicalWorkspace logicalWorkspace) {
    this.logicalWorkspace = logicalWorkspace;
  }
}
