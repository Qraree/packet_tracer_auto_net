package org.core;

import com.cisco.pt.impl.OptionsManager;
import com.cisco.pt.ipc.IPCFactory;
import com.cisco.pt.ipc.enums.BufferFullAction;
import com.cisco.pt.ipc.system.NetworkFile;
import com.cisco.pt.ipc.system.Options;
import com.cisco.pt.ipc.ui.AppWindow;
import com.cisco.pt.ipc.ui.IPC;
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
import org.core.services.DeviceService;

public class PacketTracerConnector extends Application {
  private static final Logger logger = Logger.getLogger(PacketTracerConnector.class.getName());

  private static final String ENV_SECRET = "SECRET";
  private static final String ENV_AUTH_APP = "AUTH_APPLICATION";
  private static final String ENV_URL = "URL";
  private static final String ENV_PORT = "PORT";

  public static IPC ipcInstance;

  @Override
  public void start(Stage primaryStage) throws Exception {
    setupPacketTracer();
    setupGUI(primaryStage);
  }

  public static void main(String[] args) {
    try {
      launch();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "An error occurred: ", e);
    }
  }

  public static void setupPacketTracer() throws IOException {
    try {
      PacketTracerSession session = setupConnection();
      ipcInstance = setupIPC(session);
      setupNetwork();
      registerListeners(session);
    } catch (Error e) {
      logger.log(Level.SEVERE, "Packet Tracer is not running!");
    }
  }

  private static PacketTracerSession setupConnection() throws IOException {

    PacketTracerSessionFactory sessionFactory = PacketTracerSessionFactoryImpl.getInstance();
    ConnectionNegotiationProperties cnp = OptionsManager.getInstance().getConnectOpts();

    Map<String, String> env = System.getenv();

    cnp.setAuthenticationSecret(env.get(ENV_SECRET));
    cnp.setAuthenticationApplication(env.get(ENV_AUTH_APP));

    return sessionFactory.openSession(env.get(ENV_URL), Integer.parseInt(env.get(ENV_PORT)), cnp);
  }

  private static IPC setupIPC(PacketTracerSession session) {
    IPCFactory ipcFactory = new IPCFactory(session);
    return ipcFactory.getIPC();
  }

  private static void setupNetwork() {
    AppWindow appWindow = ipcInstance.appWindow();
    NetworkFile networkFile = appWindow.getActiveFile();
    Options options = networkFile.getOptions();
    options.setBufferFullAction(BufferFullAction.AUTO_CLEAR_EVENT_LIST);

    DeviceService.setupNetworkNodes();
  }

  private static void registerListeners(PacketTracerSession session) throws IOException {
    EventManager eventManager = new EventManager(session);
    eventManager.registerListeners(ipcInstance);
    logger.info("Connection to Packet Tracer Successful!");
  }

  private void setupGUI(Stage primaryStage) throws IOException {
    GUIManager guiManager = new GUIManager(primaryStage);
    guiManager.init();
  }

  public static boolean isPacketSessionRunning() {
    return ipcInstance != null;
  }
}
