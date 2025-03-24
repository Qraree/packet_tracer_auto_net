package org.core.gui.mappers;

import com.cisco.pt.ipc.sim.Device;
import java.util.ArrayList;
import java.util.List;
import org.core.config.DeviceModelEnum;
import org.core.models.NetworkNode;

public class DeviceMapper {

  public DeviceMapper() {}

  public static ArrayList<NetworkNode> mapManyToNetworkNodes(List<Device> devices) {
    ArrayList<NetworkNode> networkNodes = new ArrayList<>();
    for (Device device : devices) {
      String name = device.getName();
      String model = device.getModel();
      NetworkNode GUIDevice =
          new NetworkNode(name, model, DeviceModelEnum.getIconPathByModel(model), device);
      networkNodes.add(GUIDevice);
    }

    return networkNodes;
  }

  public static NetworkNode mapOneToNetworkNode(Device device) {

    String name = device.getName();
    String model = device.getModel();

    return new NetworkNode(name, model, DeviceModelEnum.getIconPathByModel(model), device);
  }
}
