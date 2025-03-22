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

  private static final String ENV_SECRET = "SECRET";
  private static final String ENV_AUTH_APP = "AUTH_APPLICATION";
  private static final String ENV_URL = "URL";
  private static final String ENV_PORT = "PORT";

  @Override
  public void start(Stage primaryStage) throws Exception {

    PacketTracerSession session = setupConnection();
    IPC ipc = setupIPC(session);
    DeviceManager deviceManager = setupNetwork(ipc);
    registerListeners(ipc, session, deviceManager);
    setupGUI(primaryStage, deviceManager);
  }

  public static void main(String[] args) {
    try {
      launch();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occurred: ", e);
      System.out.println("Error connecting to Packet Tracer.");
    }
  }

  private DeviceManager setupNetwork(IPC ipc) {
    AppWindow appWindow = ipc.appWindow();
    Network network = ipc.network();

    configureNetworkOptions(appWindow);

    LogicalWorkspace logicalWorkspace = appWindow.getActiveWorkspace().getLogicalWorkspace();
    return new DeviceManager(logicalWorkspace, network);
  }

  private IPC setupIPC(PacketTracerSession session) {
    IPCFactory ipcFactory = new IPCFactory(session);

    return ipcFactory.getIPC();
  }

  private void registerListeners(IPC ipc, PacketTracerSession session, DeviceManager deviceManager)
      throws IOException {
    AppWindow appWindow = ipc.appWindow();
    LogicalWorkspace logicalWorkspace = appWindow.getActiveWorkspace().getLogicalWorkspace();

    EventManager eventManager = new EventManager(session, deviceManager);
    eventManager.registerLogicalWorkspaceListener(logicalWorkspace);
    eventManager.registerAppWindowListener(appWindow);

    logger.info("Connection to Packet Tracer Successful!");
  }

  private void configureNetworkOptions(AppWindow appWindow) {

    NetworkFile networkFile = appWindow.getActiveFile();
    Options options = networkFile.getOptions();
    options.setBufferFullAction(BufferFullAction.AUTO_CLEAR_EVENT_LIST);
  }

  private PacketTracerSession setupConnection() throws IOException {
    PacketTracerSessionFactory sessionFactory = PacketTracerSessionFactoryImpl.getInstance();
    ConnectionNegotiationProperties cnp = OptionsManager.getInstance().getConnectOpts();

    Map<String, String> env = System.getenv();

    cnp.setAuthenticationSecret(env.get(ENV_SECRET));
    cnp.setAuthenticationApplication(env.get(ENV_AUTH_APP));

    return sessionFactory.openSession(env.get(ENV_URL), Integer.parseInt(env.get(ENV_PORT)), cnp);
  }

  private void setupGUI(Stage primaryStage, DeviceManager deviceManager) throws IOException {
    GUIManager guiManager = new GUIManager(primaryStage, deviceManager);
    guiManager.init();
  }
}
