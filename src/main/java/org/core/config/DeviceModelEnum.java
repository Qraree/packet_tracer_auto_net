package org.core.config;

import com.cisco.pt.ipc.enums.DeviceType;

public enum DeviceModelEnum {
  LAPTOP("Laptop-PT", DeviceType.LAPTOP, "/images/laptop.png"),
  PC("PC-PT", DeviceType.PC, "/images/pc.jpg"),
  SWITCH_2960_24TT("2960-24TT", DeviceType.SWITCH, "/images/switch.jpg"),
  SWITCH_3560_24PS("3560-24PS", DeviceType.MULTI_LAYER_SWITCH, "/images/switch_l3.png"),
  SWITCH_3650_24PS("3650-24PS", DeviceType.MULTI_LAYER_SWITCH, "/images/switch_l3.png"),
  SWITCH_2950_24("2950-24", DeviceType.SWITCH, "/images/switch.jpg"),
  ROUTER1841("1841", DeviceType.ROUTER, "/images/router.png"),
  PDD("Power Distribution Device", DeviceType.POWER_DISTRIBUTION_DEVICE, "/images/cisco.png");

  private final String model;
  private final DeviceType deviceType;
  private final String iconPath;

  DeviceModelEnum(String model, DeviceType deviceType, final String path) {
    this.model = model;
    this.deviceType = deviceType;
    this.iconPath = path;
  }

  public String getModel() {
    return model;
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public String getIconPath() {
    return iconPath;
  }

  public static DeviceModelEnum getDeviceModelEnumByModel(String model) {
    for (DeviceModelEnum device : values()) {
      if (device.getModel().equalsIgnoreCase(model)) {
        return device;
      }
    }

    return null;
  }

  public static String getIconPathByModel(String model) {
    for (DeviceModelEnum device : values()) {
      if (device.getModel().equalsIgnoreCase(model)) {
        return device.getIconPath();
      }
    }
    return "/images/cisco.png";
  }
}
