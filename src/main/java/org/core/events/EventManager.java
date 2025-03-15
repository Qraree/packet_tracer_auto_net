package org.core.events;

import com.cisco.pt.ipc.events.IPCEventManager;
import com.cisco.pt.ipc.events.LogicalWorkspaceEventRegistry;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import com.cisco.pt.ptmp.PacketTracerSession;
import java.io.IOException;
import java.util.List;
import org.core.config.Constants;

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
    List<String> LWCallbackList = Constants.LWCallbackList;

    LogicalWorkspaceEventListener LWListener = new LogicalWorkspaceEventListener();
    LWRegistry.addSpecificListenerFiltered(
        LWListener::handleEvent, logicalWorkspace, LWCallbackList);
  }
}
