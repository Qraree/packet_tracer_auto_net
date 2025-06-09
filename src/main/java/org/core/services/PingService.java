package org.core.services;

import com.cisco.pt.impl.IPAddressImpl;
import com.cisco.pt.ipcutil.PingUtil;
import org.core.PacketTracerConnector;

public class PingService {

  public void ping() {
    long start = System.nanoTime();
    PingUtil pingUtil =
        new PingUtil(
            PacketTracerConnector.ipcInstance.getFactory(),
            PacketTracerConnector.ipcInstance.getPacketTracerSession().getEventManager());
    boolean result = pingUtil.sendPing("PC0", new IPAddressImpl("192.168.2.3"), 10, 10);

    long end = System.nanoTime();
    System.out.println("Execution time: " + (end - start) / 1_000_000 + " ms");
    System.out.println("Result" + result);
  }
}
