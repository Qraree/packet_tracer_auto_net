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
          IPCEventConstants.LOGICAL_WORKSPACE_LINK_CREATED);

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

  public static final String VLAN_MANAGER_PROCESS = "VlanManager";
}
