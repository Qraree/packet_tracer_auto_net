package org.core.events.listeners;

import com.cisco.pt.ipc.events.LogicalWorkspaceEvent;
import java.util.UUID;
import org.core.events.EventListener;
import org.core.operations.OperationState;

public class LogicalWorkspaceEventListener implements EventListener<LogicalWorkspaceEvent> {
  @Override
  public void handleEvent(LogicalWorkspaceEvent event) {
    System.out.println(event.type);
    switch (event.type) {
      case DEVICE_ADDED:
        System.out.println("Device added!!!");
        UUID currentOperationUUID = OperationState.getInstance().getCurrentOperationUUID();
        System.out.println("HEllo?!!!");

        if (currentOperationUUID == null) {
          System.out.println("No operation found");
          return;
        }

        System.out.println("Current operation: " + currentOperationUUID.toString());

        LogicalWorkspaceEvent.DeviceAdded LWEvent = (LogicalWorkspaceEvent.DeviceAdded) event;

        System.out.println("Device type: " + LWEvent.type);
        System.out.println("Device model: " + LWEvent.model);
        break;
      case DEVICE_REMOVED:
        assert event instanceof LogicalWorkspaceEvent.DeviceRemoved;
        LogicalWorkspaceEvent.DeviceRemoved devRemovedEvent =
            (LogicalWorkspaceEvent.DeviceRemoved) event;

        System.out.println("Event type: " + devRemovedEvent.type);
        System.out.println("Device name: " + devRemovedEvent.name);
        break;
      case LINK_CREATED:
        assert event instanceof LogicalWorkspaceEvent.LinkCreated;
        LogicalWorkspaceEvent.LinkCreated linkCreateEvent =
            (LogicalWorkspaceEvent.LinkCreated) event;

        System.out.println("First device: " + linkCreateEvent.deviceName1);
        System.out.println("First device port: " + linkCreateEvent.portName1);

        System.out.println("Second device: " + linkCreateEvent.deviceName2);
        System.out.println("Second device port: " + linkCreateEvent.portName2);

        break;
    }
  }
}
