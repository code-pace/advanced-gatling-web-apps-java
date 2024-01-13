import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import orangehrmlive.specs.TestUtils;

public class Engine {

  public static void main(String[] args) {
    TestUtils.deleteReportBeforeStart();
    GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
      .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString())
      .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
      .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString());

    Gatling.fromMap(props.build());
  }
}
