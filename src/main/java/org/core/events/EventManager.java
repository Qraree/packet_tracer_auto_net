package org.core.events;

import com.cisco.pt.ipc.events.*;
import com.cisco.pt.ipc.ui.IPC;
import com.cisco.pt.ptmp.PacketTracerSession;
import java.io.IOException;
import org.core.events.listeners.AppWindowEventListener;
import org.core.events.listeners.LogicalWorkspaceEventListener;

public class EventManager {

  IPCEventManager ipcEventManager;

  LogicalWorkspaceEventListener logicalWorkspaceEventListener;
  AppWindowEventListener appWindowEventListener;

  public EventManager(PacketTracerSession session) {
    this.ipcEventManager = session.getEventManager();

    logicalWorkspaceEventListener = new LogicalWorkspaceEventListener();
    appWindowEventListener = new AppWindowEventListener();
  }

  public void registerListeners(IPC ipc) throws IOException {
    logicalWorkspaceEventListener.register(ipcEventManager, ipc);
    appWindowEventListener.register(ipcEventManager, ipc);
  }

  public void unregisterListeners() throws IOException {
    logicalWorkspaceEventListener.unregister(ipcEventManager);
    appWindowEventListener.unregister(ipcEventManager);
  }
}
