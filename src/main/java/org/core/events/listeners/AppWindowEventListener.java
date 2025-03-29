package org.core.events.listeners;

import com.cisco.pt.ipc.events.AppWindowEvent;
import com.cisco.pt.ipc.events.AppWindowEventRegistry;
import com.cisco.pt.ipc.events.IPCEventManager;
import com.cisco.pt.ipc.ui.IPC;
import java.io.IOException;
import org.core.config.Constants;
import org.core.events.EventListener;

public class AppWindowEventListener
    implements EventListener<AppWindowEvent>, com.cisco.pt.ipc.events.AppWindowEventListener {

  public AppWindowEventListener() {}

  public void register(IPCEventManager ipcEventManager, IPC ipc) throws IOException {
    AppWindowEventRegistry AWRegistry = ipcEventManager.getAppWindowEvents();

    AWRegistry.addSpecificListenerFiltered(this, ipc.appWindow(), Constants.AWCallbackList);
  }

  public void unregister(IPCEventManager ipcEventManager) throws IOException {
    AppWindowEventRegistry AWRegistry = ipcEventManager.getAppWindowEvents();
    AWRegistry.removeListener(this);
  }

  public void handleEvent(AppWindowEvent event) {
    switch (event.type) {
      case APP_EXIT, FILE_CLOSING, FILE_NEWED:
        // todo Потом сделать, чтобы устанавливалась новая сессия
        System.exit(0);
        break;
    }
  }
}
