package org.core.config;

import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.events.IPCEventConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
  public static final List<String> LWCallbackList =
      Arrays.asList(
          IPCEventConstants.LOGICAL_WORKSPACE_DEVICE_ADDED,
          IPCEventConstants.LOGICAL_WORKSPACE_DEVICE_REMOVED,
          IPCEventConstants.LOGICAL_WORKSPACE_LINK_CREATED,
          IPCEventConstants.LOGICAL_WORKSPACE_LINK_DELETED);

  public static final List<String> AWCallbackList =
      Arrays.asList(
          IPCEventConstants.APP_WINDOW_APP_EXIT,
          IPCEventConstants.APP_WINDOW_FILE_CLOSING,
          IPCEventConstants.APP_WINDOW_FILE_NEWED);

  public static final ArrayList<String> NETWORK_DEVICE_TYPES =
      new ArrayList<>(
          Arrays.asList(
              DeviceType.SWITCH.toString(),
              DeviceType.MULTI_LAYER_SWITCH.toString(),
              DeviceType.SWITCH3650.toString()));

  public static final ArrayList<String> MULTILAYER_SWITCH_TYPES =
      new ArrayList<>(
          Arrays.asList(
              DeviceType.MULTI_LAYER_SWITCH.toString(), DeviceType.SWITCH3650.toString()));

  public static final ArrayList<String> END_DEVICE_TYPES =
      new ArrayList<>(Arrays.asList(DeviceType.PC.toString(), DeviceType.LAPTOP.toString()));

  public static final String DEFAULT_SUBNET_MASK = "255.255.255.0";
  public static final String DEFAULT_PC_INTERFACE = "FastEthernet0";

  public static final Integer CANVAS_LAYER = 10;

  // VLAN_NUMBER
  public static final Integer VLAN_DEFAULT_NUMBER = 1;
  public static final Integer VLAN_SERVICE_NUMBER = 1002;

  // PROCESS
  public static final String VLAN_MANAGER_PROCESS = "VlanManager";
  public static final String OSPF_MAIN_PROCESS = "OspfMainProcess";

  // CLI COMMANDS
  public static final String TERMINAL_NO_RESPONSE = "no";
  public static final String TERMINAL_USER_MODE = "user";
  public static final String TERMINAL_ENABLE_MODE = "enable";
  public static final String TERMINAL_ENABLE_RESPONSE = "enable";
  public static final String TERMINAL_CONFIG_RESPONSE = "configure terminal";
  public static final String TERMINAL_IP_ROUTING_COMMAND = "ip routing";
  public static final String TERMINAL_SWITCH_TO_TRUNK_COMMAND = "switchport mode trunk";
  public static final String TERMINAL_NO_SWITCHPORT_COMMAND = "no switchport";
  public static final String TERMINAL_ROUTER_OSPF_COMMAND = "router ospf 1";
}
