import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.core.PacketTracerConnector;
import org.core.services.DeviceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

  @BeforeAll
  static void setupConnectionToPacketTracer() {
    try {
      PacketTracerConnector.setupPacketTracer();
      DeviceService.deleteAllDevices();
    } catch (IOException e) {
      fail("Could not connect packet tracer");
    }
  }

  @AfterEach
  void deleteAllDevices() {
    DeviceService.deleteAllDevices();
  }
}
