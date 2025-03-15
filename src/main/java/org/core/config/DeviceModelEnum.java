package org.core.config;

public enum DeviceModelEnum {
  LAPTOP("Laptop-PT"),
  PC("PC-PT");

  private final String value;

  DeviceModelEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
