package org.core.events;

import com.cisco.pt.ipc.events.LogicalWorkspaceEvent;

public class LogicalWorkspaceEventListener implements EventListener<LogicalWorkspaceEvent> {
  @Override
  public void handleEvent(LogicalWorkspaceEvent event) {
    switch (event.type) {
      case DEVICE_ADDED:
        LogicalWorkspaceEvent.DeviceAdded LWEvent = (LogicalWorkspaceEvent.DeviceAdded) event;
        System.out.println("Device type: " + LWEvent.type);
        System.out.println("Device model: " + LWEvent.model);
    }
  }
}
