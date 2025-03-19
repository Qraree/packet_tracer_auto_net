package org.core.gui.mappers;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import java.util.List;
import org.core.gui.models.DeviceGUIModel;

public class DeviceMapper {

  public DeviceMapper() {}

  public static ArrayList<DeviceGUIModel> mapManyToGUIModel(List<Device> devices) {
    ArrayList<DeviceGUIModel> deviceGUIModels = new ArrayList<>();
    for (Device device : devices) {
      String name = device.getName();
      String model = device.getModel();
      DeviceGUIModel GUIDevice = new DeviceGUIModel(name, model, "/images/router.png");
      deviceGUIModels.add(GUIDevice);
    }

    return deviceGUIModels;
  }

  public static DeviceGUIModel mapOneToGUIModel(Device device) {

    String name = device.getName();
    String model = device.getModel();

    return new DeviceGUIModel(name, model, "/images/router.png");
  }
}
