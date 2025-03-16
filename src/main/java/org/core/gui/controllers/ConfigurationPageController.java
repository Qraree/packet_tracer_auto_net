package org.core.gui.controllers;

import com.cisco.pt.UUID;
import com.cisco.pt.ipc.enums.DeviceType;
import com.cisco.pt.ipc.ui.LogicalWorkspace;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.core.config.DeviceModelEnum;
import org.core.gui.GUIValidator;
import org.core.operations.OperationState;

public class ConfigurationPageController implements Initializable {
  public TextField randomCount;
  public TextField subnetDeviceCount;
  public ChoiceBox<String> subnetNetworkDeviceChoice;
  private LogicalWorkspace logicalWorkspace;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    subnetNetworkDeviceChoice
        .getItems()
        .addAll(
            DeviceModelEnum.SWITCH_2960_24TT.getModel(),
            DeviceModelEnum.SWITCH_3560_24PS.getModel());

    subnetNetworkDeviceChoice.setValue(DeviceModelEnum.SWITCH_2960_24TT.getModel());
  }

  // todo move device methods to DeviceManager class
  public void handleAddRandomDeviceAction() {
    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    Object[][] DevicesArray = {
      {DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getModel()},
      {DeviceType.PC, DeviceModelEnum.PC.getModel()}
    };
    Random random = new Random();

    int randomDeviceIndex = random.nextInt(DevicesArray.length);

    Function<Double, Double> roundedValue = (value) -> Math.round(value * 100.0) / 100.0;
    double randomXCoordinate = roundedValue.apply(random.nextDouble() * 1000);
    double randomYCoordinate = roundedValue.apply(random.nextDouble() * 500);

    logicalWorkspace.addDevice(
        (DeviceType) DevicesArray[randomDeviceIndex][0],
        (String) DevicesArray[randomDeviceIndex][1],
        randomXCoordinate,
        randomYCoordinate);

    List<UUID> itemsIds = logicalWorkspace.getCanvasItemIds();
    System.out.println(itemsIds);
  }

  public void handleAddDevicesGroupAction() {

    if (logicalWorkspace == null) {
      throw new RuntimeException("No logical workspace available");
    }

    int startXCoord = 300;
    int startYCoord = 300;
    int step = 60;

    if (!GUIValidator.validateNumberInput(randomCount.getText(), 0, 20)) return;
    int devicesCount = Integer.parseInt(randomCount.getText());

    addDeviceGroup(devicesCount, startXCoord, startYCoord, step);
  }

  public void handleAddSubnetAction(ActionEvent actionEvent) {
    String selectedNetworkDevice = subnetNetworkDeviceChoice.getValue();

    if (!GUIValidator.validateNumberInput(subnetDeviceCount.getText(), 0, 20)) return;
    int deviceCount = Integer.parseInt(subnetDeviceCount.getText());

    java.util.UUID operationUUID = java.util.UUID.randomUUID();
    OperationState.getInstance().setCurrentOperation(operationUUID);

    addDeviceGroup(deviceCount, 300, 300, 60);
    logicalWorkspace.addDevice(DeviceType.SWITCH, selectedNetworkDevice, 200, 200);
  }

  private void addDeviceGroup(Integer count, Integer xCoord, Integer yCoord, Integer step) {

    if (step == null) step = 60;
    if (xCoord == null) xCoord = 300;
    if (yCoord == null) yCoord = 300;
    if (count == null) count = 10;

    int rows = (int) Math.sqrt(count);
    int cols = (count + rows - 1) / rows;

    int deviceCount = 0;
    for (int currentXCoord = xCoord;
        currentXCoord < rows * step + xCoord;
        currentXCoord = currentXCoord + step) {

      for (int currentYCoord = yCoord;
          currentYCoord < cols * step + yCoord;
          currentYCoord = currentYCoord + step) {

        if (deviceCount < count) {
          logicalWorkspace.addDevice(
              DeviceType.LAPTOP, DeviceModelEnum.LAPTOP.getModel(), currentXCoord, currentYCoord);

          deviceCount++;
        }
      }
    }
  }

  public void setLogicalWorkspace(LogicalWorkspace logicalWorkspace) {
    this.logicalWorkspace = logicalWorkspace;
  }
}
