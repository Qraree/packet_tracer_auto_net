package org.core.events.listeners;

import com.cisco.pt.ipc.events.LogicalWorkspaceEvent;
import com.cisco.pt.ipc.sim.Device;
import javafx.application.Platform;
import org.core.events.EventListener;
import org.core.models.GlobalNetwork;
import org.core.services.DeviceService;

public class LogicalWorkspaceEventListener implements EventListener<LogicalWorkspaceEvent> {

  public LogicalWorkspaceEventListener() {}

  @Override
  public void handleEvent(LogicalWorkspaceEvent event) {
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
              GlobalNetwork.getInstance()
                  .createNodesConnection(
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
        Platform.runLater(() -> GlobalNetwork.getInstance().createNetworkNode(device));

        break;
      case DEVICE_REMOVED:
        assert event instanceof LogicalWorkspaceEvent.DeviceRemoved;
        LogicalWorkspaceEvent.DeviceRemoved devRemovedEvent =
            (LogicalWorkspaceEvent.DeviceRemoved) event;

        Platform.runLater(
            () -> GlobalNetwork.getInstance().deleteNetworkNode(devRemovedEvent.name));

        break;

      case LINK_DELETED:
        assert event instanceof LogicalWorkspaceEvent.LinkDeleted;
        System.out.println("Link deleted");
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
