package org.core.events.listeners;

import com.cisco.pt.ipc.events.AppWindowEvent;
import org.core.events.EventListener;

public class AppWindowEventListener
    implements EventListener<AppWindowEvent>, com.cisco.pt.ipc.events.AppWindowEventListener {

  @Override
  public void handleEvent(AppWindowEvent event) {
    switch (event.type) {
      case APP_EXIT, FILE_CLOSING, FILE_NEWED:
        // todo Потом сделать, чтобы устанавливалась новая сессия
        System.exit(0);
        break;
    }
  }
}
