package org.core.gui.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.core.models.GlobalNetwork;
import org.core.models.NetworkNode;
import org.core.services.NetworkConfigurationService;

public class ConfigurationPageController {
  private static final Logger logger =
      Logger.getLogger(ConfigurationPageController.class.getName());

  public void configureFinalNetworkRandomV2() {
    logger.log(Level.INFO, "Configuring final network");
    ObservableList<NetworkNode> networkNodes = GlobalNetwork.getInstance().getNetworkNodes();
    NetworkConfigurationService.configFinalNetworkRandomV2(networkNodes);
    logger.log(Level.FINE, "Network configuration finished");
  }
}
