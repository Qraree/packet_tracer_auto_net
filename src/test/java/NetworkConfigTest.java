import javafx.collections.ObservableList;
import org.core.models.GlobalNetwork;
import org.core.models.NetworkNode;
import org.core.services.NetworkConfigurationService;
import org.junit.jupiter.api.Test;

public class NetworkConfigTest extends BaseTest {

  @Test
  void testNetworkConfig() {
    ObservableList<NetworkNode> nodes = GlobalNetwork.getInstance().getNetworkNodes();
    NetworkConfigurationService.configFinalNetworkRandomV2(nodes);
  }
}
