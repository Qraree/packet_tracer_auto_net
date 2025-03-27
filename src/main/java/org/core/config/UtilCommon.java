package org.core.config;

import com.cisco.pt.ipc.sim.VLAN;

public class UtilCommon {
  private static String getEnvVar(String key) {
    String value = System.getenv(key);
    if (value == null || value.isEmpty()) {
      throw new IllegalStateException("Missing required environment variable: " + key);
    }
    return value;
  }

  public static boolean isValidVlanNumber(VLAN vlan) {
    return vlan.getVlanNumber() > Constants.VLAN_DEFAULT_NUMBER
        && vlan.getVlanNumber() < Constants.VLAN_SERVICE_NUMBER;
  }
}
