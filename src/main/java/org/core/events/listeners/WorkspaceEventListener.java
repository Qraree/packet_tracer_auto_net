package org.core.events.listeners;

import com.cisco.pt.ipc.events.WorkspaceEvent;
import org.core.events.EventListener;

public class WorkspaceEventListener implements EventListener<WorkspaceEvent> {
  public WorkspaceEventListener() {}

  @Override
  public void handleEvent(WorkspaceEvent event) {
    switch (event.type) {
      case DEVICE_TYPE_SELECTED:
        System.out.println("Device type selected");
        break;
      case DEVICE_TYPE_DESELECTED:
        System.out.println("Device type deselected");
        break;
    }
  }
}
