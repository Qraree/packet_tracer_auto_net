package org.core.events;

import com.cisco.pt.ipc.events.IPCEventConstants;
import com.cisco.pt.ipc.events.IPCEventManager;
import com.cisco.pt.ipc.events.LogicalWorkspaceEventRegistry;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import com.cisco.pt.ptmp.PacketTracerSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

  IPCEventManager ipcEventManager;
  PacketTracerSession session;

  LogicalWorkspaceEventRegistry LWRegistry;

  public EventManager(PacketTracerSession session) {
    this.session = session;
    this.ipcEventManager = session.getEventManager();

    this.LWRegistry = ipcEventManager.getLogicalWorkspaceEvents();
  }

  public void registerLogicalWorkspaceListener(LogicalWorkspace logicalWorkspace)
      throws IOException {
    List<String> LWCallbackList = new ArrayList<>();
    LWCallbackList.add(IPCEventConstants.LOGICAL_WORKSPACE_DEVICE_ADDED);
    LWCallbackList.add(IPCEventConstants.LOGICAL_WORKSPACE_DEVICE_REMOVED);

    LogicalWorkspaceEventListener LWListener = new LogicalWorkspaceEventListener();
    LWRegistry.addSpecificListenerFiltered(
        LWListener::handleEvent, logicalWorkspace, LWCallbackList);
  }
}
