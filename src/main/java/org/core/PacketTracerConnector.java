package org.core;

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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import org.core.events.EventManager;
import org.core.gui.GUIManager;

public class PacketTracerConnector extends Application {
  private static final Logger logger = Logger.getLogger(PacketTracerConnector.class.getName());

  @Override
  public void start(Stage primaryStage) throws Exception {

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
    eventManager.registerAppWindowListener(appWindow);

    System.out.println(network.getDeviceCount());
    System.out.println("Connection to Packet Tracer Successful!");

    GUIManager guiManager = new GUIManager(primaryStage, logicalWorkspace);
    guiManager.init();
  }

  public static void main(String[] args) {
    try {
      launch();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occurred: ", e);
      System.out.println("Error connecting to Packet Tracer.");
    }
  }

  public static PacketTracerSession setupConnection() throws IOException {
    PacketTracerSessionFactory sessionFactory = PacketTracerSessionFactoryImpl.getInstance();
    ConnectionNegotiationProperties cnp = OptionsManager.getInstance().getConnectOpts();

    Map<String, String> env = System.getenv();

    cnp.setAuthenticationSecret(env.get("SECRET"));
    cnp.setAuthenticationApplication(env.get("AUTH_APPLICATION"));

    return sessionFactory.openSession(env.get("URL"), Integer.parseInt(env.get("PORT")), cnp);
  }
}
