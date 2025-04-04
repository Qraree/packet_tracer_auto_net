package org.core.events.listeners;

import com.cisco.pt.ipc.events.IPCEventManager;
import com.cisco.pt.ipc.events.LogicalWorkspaceEvent;
import com.cisco.pt.ipc.events.LogicalWorkspaceEventRegistry;
import com.cisco.pt.ipc.sim.Device;
import com.cisco.pt.ipc.ui.IPC;
import java.io.IOException;
import javafx.application.Platform;
import org.core.config.Constants;
import org.core.events.EventListener;
import org.core.models.GlobalNetwork;
import org.core.services.DeviceService;

public class LogicalWorkspaceEventListener
    implements EventListener<LogicalWorkspaceEvent>,
        com.cisco.pt.ipc.events.LogicalWorkspaceEventListener {

  public LogicalWorkspaceEventListener() {}

  public void register(IPCEventManager ipcEventManager, IPC ipc) throws IOException {
    LogicalWorkspaceEventRegistry LWRegistry = ipcEventManager.getLogicalWorkspaceEvents();

    LWRegistry.addSpecificListenerFiltered(
        this, ipc.appWindow().getActiveWorkspace().getLogicalWorkspace(), Constants.LWCallbackList);
  }

  public void unregister(IPCEventManager ipcEventManager) throws IOException {
    LogicalWorkspaceEventRegistry AWRegistry = ipcEventManager.getLogicalWorkspaceEvents();
    AWRegistry.removeListener(this);
  }

  public void handleEvent(LogicalWorkspaceEvent event) {
    GlobalNetwork globalNetwork = GlobalNetwork.getInstance();
    System.out.println(event.type);
    switch (event.type) {
      case LINK_CREATED:
        System.out.println("Link created");
        assert event instanceof LogicalWorkspaceEvent.LinkCreated;
        LogicalWorkspaceEvent.LinkCreated linkCreateEvent =
            (LogicalWorkspaceEvent.LinkCreated) event;

        System.out.println("LINK CREATED");

        Platform.runLater(
            () -> {
              globalNetwork.createNodesConnection(
                  linkCreateEvent.deviceName1,
                  linkCreateEvent.deviceName2,
                  linkCreateEvent.portName1,
                  linkCreateEvent.portName2);
            });

        System.out.println("First device: " + linkCreateEvent.deviceName1);
        System.out.println("First device port: " + linkCreateEvent.portName1);

        System.out.println("Second device: " + linkCreateEvent.deviceName2);
        System.out.println("Second device port: " + linkCreateEvent.portName2);

        break;
      case DEVICE_ADDED:
        LogicalWorkspaceEvent.DeviceAdded LWEvent = (LogicalWorkspaceEvent.DeviceAdded) event;
        System.out.println("Device added " + LWEvent.model);

        Device device = DeviceService.getDeviceByName(LWEvent.name);
        Platform.runLater(() -> globalNetwork.createNetworkNode(device));

        break;
      case DEVICE_REMOVED:
        assert event instanceof LogicalWorkspaceEvent.DeviceRemoved;
        LogicalWorkspaceEvent.DeviceRemoved devRemovedEvent =
            (LogicalWorkspaceEvent.DeviceRemoved) event;

        Platform.runLater(
            () -> {
              globalNetwork.deleteAllNodeConnections(devRemovedEvent.name);
              globalNetwork.deleteNetworkNode(devRemovedEvent.name);
            });
        break;
      case LINK_DELETED:
        assert event instanceof LogicalWorkspaceEvent.LinkDeleted;
        LogicalWorkspaceEvent.LinkDeleted linkDeletedEvent =
            (LogicalWorkspaceEvent.LinkDeleted) event;
        System.out.println("Link deleted");
        Platform.runLater(
            () ->
                globalNetwork.deleteNodeConnections(
                    linkDeletedEvent.deviceName1,
                    linkDeletedEvent.deviceName2,
                    linkDeletedEvent.portName1,
                    linkDeletedEvent.portName2));

        break;
      case LINK_STARTED:
        assert event instanceof LogicalWorkspaceEvent.LinkStarted;
        System.out.println("Link started");
        break;
      default:
        System.out.println("Unknown event type: " + event.type);
    }
  }
}
