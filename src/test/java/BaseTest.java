import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.core.PacketTracerConnector;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

  @BeforeAll
  static void setupConnectionToPacketTracer() {
    System.out.println("Setting up connection to packet tracer");
    try {
      PacketTracerConnector.setupPacketTracer();
    } catch (IOException e) {
      fail("Could not connect packet tracer");
    }
  }
}
