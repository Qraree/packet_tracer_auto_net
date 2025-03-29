package org.core.events;

import com.cisco.pt.ipc.events.IPCEventManager;
import com.cisco.pt.ipc.ui.IPC;
import java.io.IOException;

public interface EventListener<T> {
  void handleEvent(T event);

  void register(IPCEventManager ipcEventManager, IPC ipc) throws IOException;

  void unregister(IPCEventManager ipcEventManager) throws IOException;
}
