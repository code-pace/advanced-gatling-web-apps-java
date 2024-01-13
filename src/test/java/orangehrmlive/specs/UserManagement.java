package orangehrmlive.specs;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class UserManagement {
    public static ChainBuilder navigateToUserManagement() {
        return exec(
                http("Navigate to user management module as admin")
                        .get("/web/index.php/admin/viewSystemUsers")
                        .check(css(".oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module").is("User Management"))
                        .check(xpath("//div/span[contains(@class,'oxd-text')]").saveAs("numberOfRecord"))
        ).exec(session -> {
            System.out.println(session.get("#numberOfRecord").toString());
            return session;
        });
    }
}
