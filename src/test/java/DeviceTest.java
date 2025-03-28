import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import org.core.models.GlobalNetwork;
import org.core.models.NetworkNode;
import org.core.services.DeviceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DeviceTest extends BaseTest {

  // todo fix device clean

  //  @BeforeEach
  //  void deleteAllBeforeTest() {
  //    System.out.println("Deleting all before test");
  //    DeviceService.deleteAllDevices();
  //    ArrayList<Device> devices = DeviceService.getAllDevices();
  //    System.out.println("Remaining " + devices.size() + " devices");
  //  }
  //

  @Test
  void addSubnetDevicesCountTest() {
    DeviceService.deleteAllDevices();
    System.out.println("Adding subnet devices");
    Integer subnetRouterCount = 1;
    Integer addedDeviceCount = 3;
    DeviceService.addSubnet(addedDeviceCount);
    ArrayList<Device> devices = DeviceService.getAllDevices();
    assertEquals(subnetRouterCount + addedDeviceCount, devices.size());

    ObservableList<NetworkNode> nodes = GlobalNetwork.getInstance().getNetworkNodes();
    for (NetworkNode node : nodes) {
      System.out.println("NODE - " + node.getName());
    }
    assertEquals(subnetRouterCount + addedDeviceCount, nodes.size());
  }

  @Test
  void addRandomDeviceCountTest() {
    DeviceService.deleteAllDevices();
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

  @AfterEach
  void deleteAllAfterTest() {
    DeviceService.deleteAllDevices();
  }
}
