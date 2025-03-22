package org.core.config;

public class UtilCommon {
  private static String getEnvVar(String key) {
    String value = System.getenv(key);
    if (value == null || value.isEmpty()) {
      throw new IllegalStateException("Missing required environment variable: " + key);
    }
    return value;
  }
}
