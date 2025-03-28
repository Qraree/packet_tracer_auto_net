import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import org.core.services.DeviceService;
import org.junit.jupiter.api.Test;

class DeviceTest extends BaseTest {

  @Test
  void addSubnetDevicesCountTest() {
    Integer deviceInitialCount = 1;
    Integer addedDeviceCount = 3;
    DeviceService.addSubnet(addedDeviceCount);
    ArrayList<Device> devices = DeviceService.getAllDevices();
    assertEquals(deviceInitialCount + addedDeviceCount, devices.size());
  }
}
