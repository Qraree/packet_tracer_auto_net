package org.core.services;

import com.cisco.pt.ipc.ui.LogicalWorkspace;
import org.core.PacketTracerConnector;
import org.core.config.Constants;

public class CanvasService {

  private static final LogicalWorkspace logicalWorkspace =
      PacketTracerConnector.ipcInstance.appWindow().getActiveWorkspace().getLogicalWorkspace();

  public static void clearLayer(double layerNumber) {
    logicalWorkspace.clearLayer(layerNumber);
  }

  public static void addDeviceNote(int xCoordinate, int yCoordinate, String text) {
    logicalWorkspace.addNote(xCoordinate, yCoordinate, Constants.CANVAS_LAYER, text);
  }
}
