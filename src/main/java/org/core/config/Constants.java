package org.core.config;

import com.cisco.pt.ipc.events.IPCEventConstants;
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
}
