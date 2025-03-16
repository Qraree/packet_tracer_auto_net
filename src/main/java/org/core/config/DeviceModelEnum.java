package org.core.config;

public enum DeviceModelEnum {
  // todo model и device type
  LAPTOP("Laptop-PT"),
  PC("PC-PT"),
  SWITCH_2960_24TT("2960-24TT"),
  SWITCH_3560_24PS("3560-24PS");

  private final String model;

  DeviceModelEnum(String value) {
    this.model = value;
  }

  public String getModel() {
    return model;
  }
}
