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

  public void addRandomDevice() {
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
  }
}
