package orangehrmlive;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;
import orangehrmlive.specs.EmployeeLeave;
import orangehrmlive.specs.Login;
import orangehrmlive.specs.UserManagement;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class OrangeHrmRequest extends Simulation {

  private Map<CharSequence, String> headers_0 = Map.ofEntries(
          Map.entry("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
          Map.entry("Accept-Encoding", "gzip, deflate, br"),
          Map.entry("Accept-Language", "en-US,en;q=0.9"),
          Map.entry("Cache-Control", "max-age=0"),
          Map.entry("Sec-Fetch-Dest", "document"),
          Map.entry("Sec-Fetch-Mode", "navigate"),
          Map.entry("Sec-Fetch-Site", "cross-site"),
          Map.entry("Sec-Fetch-User", "?1"),
          Map.entry("Upgrade-Insecure-Requests", "1"),
          Map.entry("sec-ch-ua", "Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120"),
          Map.entry("sec-ch-ua-mobile", "?0"),
          Map.entry("sec-ch-ua-platform", "Windows")
  );
  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://opensource-demo.orangehrmlive.com")
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*detectportal\\.firefox\\.com.*", ".*\\.ico\\?v=\\d+", ".*\\.css\\?v=\\d+", ".*\\.js\\?v=\\d+"))
          .headers(headers_0);


  private ScenarioBuilder scn = scenario("OrangeHrmRequest")
          .exec(
                  Login.gotoLoginPage(headers_0).pause(2),
                  Login.enterLoginCredentials(headers_0,"3e9fa59f35039957e63f.czoLSkQjplEKy2M21qZPzxlrW7dXMXL_z3o_RQYqZwE.FGw5ISxanwhvuAJioNEbqy8xFtZhRzePnBgIH1IZMEcLV3o8IFftNkixVw", "Admin", "admin123").pause(2),
                  EmployeeLeave.navigateToLeavePage(headers_0),
                  EmployeeLeave.getAllEmployeesLeaveRequest(headers_0),
                  EmployeeLeave.applyForLeave(headers_0, "2023-11-01", "2023-11-31").pause(2),
                  EmployeeLeave.viewMyLeaveList(headers_0).pause(2),
                  EmployeeLeave.employeeLeaveEntitlement(headers_0),pause(2),
                  EmployeeLeave.employeePersonalDetails(headers_0),
                  EmployeeLeave.customFields(headers_0),
                  UserManagement.navigateToUserManagement()
          );

  {
	  setUp(scn.injectOpen(
              rampUsersPerSec(10).to(30).during(2)
              )).protocols(httpProtocol);
  }
}
