import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import org.core.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceTest extends BaseTest {

  @BeforeEach
  void deleteAllBeforeTest() {
    DeviceService.deleteAllDevices();
  }

  @Test
  void deleteAllDevices() {
    DeviceService.addDeviceGroup(15, null, null, null);
    ArrayList<Device> allDevices = DeviceService.getAllDevices();
    assertEquals(15, allDevices.size());

    DeviceService.deleteAllDevices();
    ArrayList<Device> devicesAfterDelete = DeviceService.getAllDevices();
    assertEquals(0, devicesAfterDelete.size());
  }

  @Test
  void addSubnetDevicesCountTest() {
    DeviceService.deleteAllDevices();

    System.out.println("Adding subnet devices");
    Integer PowerDistroDeviceCount = 1;
    Integer subnetRouterCount = 1;
    Integer addedDeviceCount = 3;
    DeviceService.addSubnet(addedDeviceCount);
    ArrayList<Device> devices = DeviceService.getAllDevices();
    for (Device device : devices) {
      String deviceName = device.getName();
      System.out.println(deviceName);
      System.out.println(device.getModel());
    }
    assertEquals(subnetRouterCount + addedDeviceCount + PowerDistroDeviceCount, devices.size());
  }

  @Test
  void addRandomDeviceCountTest() {
    System.out.println(DeviceService.getAllDevices().size());
    System.out.println("Adding random devices");
    int xBoundary = 1000;
    int yBoundary = 400;
    Device addedDevice = DeviceService.addRandomDevice(xBoundary, yBoundary);

    assertTrue(addedDevice.getXCoordinate() < xBoundary);
    assertTrue(addedDevice.getYCoordinate() < yBoundary);

    ArrayList<Device> devices = DeviceService.getAllDevices();

    for (Device device : devices) {
      System.out.println(device.getName());
    }
    assertEquals(1, devices.size());
  }
}
