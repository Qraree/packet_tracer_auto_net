package org.example;

import com.cisco.pt.impl.OptionsManager;
import com.cisco.pt.ipc.IPCFactory;
import com.cisco.pt.ipc.enums.BufferFullAction;
import com.cisco.pt.ipc.sim.Network;
import com.cisco.pt.ipc.system.NetworkFile;
import com.cisco.pt.ipc.system.Options;
import com.cisco.pt.ipc.ui.AppWindow;
import com.cisco.pt.ipc.ui.IPC;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import com.cisco.pt.ptmp.ConnectionNegotiationProperties;
import com.cisco.pt.ptmp.PacketTracerSession;
import com.cisco.pt.ptmp.PacketTracerSessionFactory;
import com.cisco.pt.ptmp.impl.PacketTracerSessionFactoryImpl;
import java.io.IOException;
import org.example.events.EventManager;

public class PacketTracerConnector {
  public static void main(String[] args) {
    try {

      PacketTracerSession session = setupConnection();

      IPCFactory ipcFactory = new IPCFactory(session);
      IPC ipc = ipcFactory.getIPC();

      AppWindow appWindow = ipc.appWindow();
      Network network = ipc.network();

      appWindow.getRSSwitch().showSimulationMode();
      NetworkFile networkFile = appWindow.getActiveFile();

      Options options = networkFile.getOptions();
      options.setBufferFullAction(BufferFullAction.AUTO_CLEAR_EVENT_LIST);

      EventManager eventManager = new EventManager(session);

      LogicalWorkspace logicalWorkspace = appWindow.getActiveWorkspace().getLogicalWorkspace();
      eventManager.registerLogicalWorkspaceListener(logicalWorkspace);

      System.out.println(network.getDeviceCount());
      System.out.println("Подключение к Packet Tracer установлено!");

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Ошибка при подключении к Packet Tracer.");
    }
  }

  public static PacketTracerSession setupConnection() throws IOException {
    PacketTracerSessionFactory sessionFactory = PacketTracerSessionFactoryImpl.getInstance();
    ConnectionNegotiationProperties cnp = OptionsManager.getInstance().getConnectOpts();

    cnp.setAuthenticationSecret("cisco");
    cnp.setAuthenticationApplication("com.example.myapp");

    return sessionFactory.openSession("localhost", 39000, cnp);
  }
}
