import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cisco.pt.util.Pair;
import org.core.config.UtilCommon;
import org.junit.jupiter.api.Test;

public class UtilsTest {
  @Test
  void testGetRandomCoordinatesWithBoundaries() {
    Integer xBoundary = 100;
    Integer yBoundary = 100;
    Pair<Double, Double> randomCoords =
        UtilCommon.getRandomCoordinatesWithBoundaries(xBoundary, yBoundary);

    Double xCoord = randomCoords.getFirst();
    Double yCoord = randomCoords.getSecond();
    assertTrue(xCoord <= xBoundary && xCoord > 0);
    assertTrue(yCoord <= yBoundary && yCoord > 0);
  }
}
