package org.core.config;

public class CommandBuilder {
  public static String buildIpAddressDefaultSubnetCommand(
      Integer subnetNumber, Number deviceNumber, String addressPrefix) {

    if (addressPrefix == null) addressPrefix = "192.168.";

    return "ip address "
        + addressPrefix
        + subnetNumber
        + "."
        + deviceNumber
        + " "
        + Constants.DEFAULT_SUBNET_MASK;
  }

  public static String buildIpAddress(int first, int second, int third, int forth) {
    return first + "." + second + "." + third + "." + forth;
  }

  public static String buildOspfSubnetCommand(
      Integer subnetNumber, Number deviceNumber, String addressPrefix) {
    if (addressPrefix == null) addressPrefix = "192.168.";
    return "network "
        + addressPrefix
        + subnetNumber
        + "."
        + deviceNumber
        + " "
        + Constants.DEFAULT_SUBNET_MASK
        + " area 0";
  }
}
