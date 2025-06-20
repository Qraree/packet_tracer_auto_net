package org.core.config;

import com.cisco.pt.ipc.sim.VLAN;
import com.cisco.pt.util.Pair;
import java.util.Random;
import java.util.function.Function;
import org.core.models.NetworkNode;

public class UtilCommon {
  private static String getEnvVar(String key) {
    String value = System.getenv(key);
    if (value == null || value.isEmpty()) {
      throw new IllegalStateException("Missing required environment variable: " + key);
    }
    return value;
  }

  public static boolean isValidVlanNumber(VLAN vlan) {
    return vlan.getVlanNumber() > Constants.VLAN_DEFAULT_NUMBER
        && vlan.getVlanNumber() < Constants.VLAN_SERVICE_NUMBER;
  }

  public static Pair<Integer, Integer> getDeviceNoteCoordinates(
      NetworkNode networkDeviceNode, NetworkNode endDeviceNode) {
    double xCoodinate = networkDeviceNode.getDevice().getXCoordinate();
    double yCoodinate = networkDeviceNode.getDevice().getYCoordinate();

    double xCoordinateEndDevice = endDeviceNode.getDevice().getXCoordinate();
    double yCoordinateEndDevice = endDeviceNode.getDevice().getYCoordinate();

    double xcoord = xCoodinate - xCoordinateEndDevice;
    double ycoord = yCoordinateEndDevice - yCoodinate;

    double theta = Math.atan2(ycoord, xcoord);

    double noteTheta = theta + Math.PI;
    double nodeR = 60;

    int noteX = (int) (xCoordinateEndDevice + nodeR * Math.cos(noteTheta));
    int noteY = (int) (yCoordinateEndDevice - nodeR * Math.sin(noteTheta));

    return new Pair<>(noteX, noteY);
  }

  public static Pair<Double, Double> getRandomCoordinatesWithBoundaries(
      Integer xBoundary, Integer yBoundary) {

    Random random = new Random();

    Function<Double, Double> roundedValue = (value) -> Math.round(value * 100.0) / 100.0;
    double randomXCoordinate = roundedValue.apply(random.nextDouble() * xBoundary);
    double randomYCoordinate = roundedValue.apply(random.nextDouble() * yBoundary);

    return new Pair<>(randomXCoordinate, randomYCoordinate);
  }
}
