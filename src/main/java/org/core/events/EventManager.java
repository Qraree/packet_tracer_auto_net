package org.core.events;

import com.cisco.pt.ipc.events.*;
import com.cisco.pt.ipc.ui.AppWindow;
import com.cisco.pt.ipc.ui.IPC;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import com.cisco.pt.ipc.ui.Workspace;
import com.cisco.pt.ptmp.PacketTracerSession;
import java.io.IOException;
import java.util.List;
import org.core.config.Constants;
import org.core.events.listeners.AppWindowEventListener;
import org.core.events.listeners.LogicalWorkspaceEventListener;

public class EventManager {

  IPCEventManager ipcEventManager;
  PacketTracerSession session;

  LogicalWorkspaceEventRegistry LWRegistry;
  AppWindowEventRegistry AWRegistry;
  WorkspaceEventRegistry WorkspaceRegistry;

  public EventManager(PacketTracerSession session) throws IOException {
    this.session = session;
    this.ipcEventManager = session.getEventManager();

    this.LWRegistry = ipcEventManager.getLogicalWorkspaceEvents();
    this.AWRegistry = ipcEventManager.getAppWindowEvents();
    this.WorkspaceRegistry = ipcEventManager.getWorkspaceEvents();
  }

  public void registerListeners(IPC ipc) throws IOException {
    AppWindow appWindow = ipc.appWindow();
    Workspace workspace = appWindow.getActiveWorkspace();
    LogicalWorkspace logicalWorkspace = workspace.getLogicalWorkspace();

    registerLogicalWorkspaceListener(logicalWorkspace);
    registerAppWindowListener(appWindow);
  }

  public void registerLogicalWorkspaceListener(LogicalWorkspace logicalWorkspace)
      throws IOException {
    List<String> LWCallbackList = Constants.LWCallbackList;

    LogicalWorkspaceEventListener LWListener = new LogicalWorkspaceEventListener();
    LWRegistry.addSpecificListenerFiltered(
        LWListener::handleEvent, logicalWorkspace, LWCallbackList);
  }

  public void registerAppWindowListener(AppWindow appWindow) throws IOException {
    List<String> AWCallbackList = Constants.AWCallbackList;

    AppWindowEventListener AWListener = new AppWindowEventListener();
    AWRegistry.addSpecificListenerFiltered(AWListener::handleEvent, appWindow, AWCallbackList);
  }
}
