package org.core.services;

import com.cisco.pt.ipc.ui.LogicalWorkspace;
import org.core.PacketTracerConnector;

public class CanvasService {

  private static final LogicalWorkspace logicalWorkspace =
      PacketTracerConnector.ipcInstance.appWindow().getActiveWorkspace().getLogicalWorkspace();

  public static void clearLayer(double layerNumber) {
    logicalWorkspace.clearLayer(layerNumber);
  }
}
