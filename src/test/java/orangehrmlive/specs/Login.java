package orangehrmlive.specs;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Login {
    public static ChainBuilder gotoLoginPage(Map<CharSequence,String> headers_0) {
        return exec(
                http("Navigate to OrangeHrm login page")
                        .get("/web/index.php/auth/login")
                        .headers(headers_0)
                        .check(bodyString().saveAs("responseBody"))
                        .check(regex(":token=.*").exists().saveAs("testValue"))
        ).pause(2).exec(session -> {
            String token = session.get("testValue").toString();
            List<String> extractToken = List.of(token.split("&quot;"));
            Session newSession = session.set("_token", extractToken.get(1));
            return newSession;
        });
    }

    public static ChainBuilder enterLoginCredentials(Map<CharSequence,String> headers_7, String _token, String username, String password) {
        return exec(
                http("Validate login credentials")
                        .post("/web/index.php/auth/validate")
                        .headers(headers_7)
                        .formParam("_token", "#{_token}")
                        .formParam("username", username)
                        .formParam("password", password)
        );
    }
}
