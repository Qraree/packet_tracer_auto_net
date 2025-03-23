package org.core.services;

import com.cisco.pt.IPAddress;
import com.cisco.pt.impl.IPAddressImpl;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.sim.*;
import com.cisco.pt.ipc.ui.IPC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkConfigurationService {

  public static void configureFinalNetwork(ArrayList<Device> devices) {

    ArrayList<Device> networkDevices =
        devices.stream()
            .filter(device -> device.getType() == DeviceType.SWITCH)
            .collect(Collectors.toCollection(ArrayList::new));

    int subnetIndex = 2;
    for (Device netDevice : networkDevices) {
      System.out.println(netDevice.getName());
      VLANManager vlanManager = (VLANManager) netDevice.getProcess("VlanManager");
      vlanManager.addVlan(subnetIndex, "subnet_" + subnetIndex);

      List<String> ports = netDevice.getPorts();

      int pcIndex = 2;

      for (String portName : ports) {
        Port port = netDevice.getPort(portName);
        Cable link = (Cable) port.getLink();

        if (link != null) {

          if (port instanceof SwitchPort switchPort) {
            switchPort.setAccessVlan(subnetIndex);
          }

          // dto router fix
          Port otherPort = link.getPort1();
          Port netDevicePort = link.getPort2();
          Device otherDevice = otherPort.getOwnerDevice();
          Device netDevicce = netDevicePort.getOwnerDevice();
          String PortName = netDevicce.getName();

          System.out.println(netDevicce.getName());

          boolean isPersonalDevice =
              Arrays.asList(DeviceType.PC, DeviceType.LAPTOP).contains(otherDevice.getType());

          if (isPersonalDevice) {
            IPAddress defaultGatewayAddress =
                new IPAddressImpl("192.168." + (subnetIndex - 1) + ".1");
            IPAddress subnetMask = new IPAddressImpl("255.255.255.0");
            IPAddress subnetAddress =
                new IPAddressImpl("192.168." + (subnetIndex - 1) + "." + pcIndex);

            HostPort hostPort = (HostPort) otherDevice.getPort("FastEthernet0");
            hostPort.setIpSubnetMask(subnetAddress, subnetMask);
            hostPort.setDefaultGateway(defaultGatewayAddress);
            pcIndex++;
          }

          boolean isRouterDevice = otherDevice.getType() == DeviceType.ROUTER;

          if (isRouterDevice
              && otherDevice instanceof Router router
              && otherPort instanceof RouterPort routerPort) {

            System.out.println("Router " + router.getName() + " " + routerPort.getName());

            if (port instanceof SwitchPort switchPort) {
              switchPort.setAccessPort(false);
            }

            routerPort.setPower(true);
            router.addSubInt(PortName, subnetIndex);
            router.changePortEncapsulation(PortName, "dot1Q");

            IPAddress subnetMask = new IPAddressImpl("255.255.255.0");
            IPAddress subnetAddress = new IPAddressImpl("192.168." + (subnetIndex - 1) + ".1");
            routerPort.setIpSubnetMask(subnetAddress, subnetMask);
          }
        }
      }
      subnetIndex++;
    }
  }
}
