package orangehrmlive.specs;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;
import java.util.ArrayList;
import java.util.Map;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class EmployeeLeave {
    public static ChainBuilder navigateToLeavePage(Map<CharSequence,String> headers_27) {
        return exec(
                http("Navigate to View Leave Module")
                        .get("/web/index.php/leave/viewLeaveModule")
                        .headers(headers_27)
                        .check(css(".oxd-text.oxd-text--h6.oxd-topbar-header-breadcrumb-module").is("Leave"))
                        .resources(
                                getEmployeeActiveLeavePeriod(headers_27)
                                        .check(css(".oxd-text oxd-text--h5.oxd-table-filter-title").is("My Leave List")),
                                selectDaysofHoliday(headers_27, "2023-01-01", "2023-12-31"),
                                getEmployeeLeaveType(headers_27),
                                viewMyWeekDaynOffDay(headers_27)
                        )
        );
    }
    public static ChainBuilder getAllEmployeesLeaveRequest(Map<CharSequence,String> headers_14) {
        return exec(
                http("Employee's leave request")
                        .get("/web/index.php/api/v2/leave/employees/leave-requests?limit=50&offset=0&includeEmployees=onlyCurrent")
                        .headers(headers_14)
                        .check(bodyString().saveAs("employeeLeaveListObj"))
        ).exec(session -> {
            String employeeLeaveList = session.get("employeeLeaveListObj").toString();
            Map<String, Object> employeeLeaveListMap = TestUtils.convertJsonStringToMap(employeeLeaveList);
            ArrayList<Map<String, Object>> employeeList = (ArrayList<Map<String, Object>>) TestUtils.readJsonFileToMap("employeeList.json").get("data");
            ArrayList<Map<String, Object>> employeeLeaveData = (ArrayList<Map<String, Object>>) employeeLeaveListMap.get("data");
            boolean anyMatch = false;
            for(int x = 0; employeeList.size() > x && !anyMatch; x++) {
                for (Map<String, Object> data : employeeLeaveData) {
                    Map<String, Object> employee = (Map<String, Object>) data.get("employee");
                    if (employeeList.get(x).get("empNumber").equals(employee.get("empNumber"))) {
                        anyMatch = true;
                        break;
                    }
                }
            }
            if (!anyMatch) {
                session.markAsFailed();
            }
            return session;
        });
    }
    public static HttpRequestActionBuilder getEmployeeActiveLeavePeriod(Map<CharSequence,String> headers_19) {
        return http("Active leave periods")
                .get("/web/index.php/api/v2/leave/leave-periods")
                .headers(headers_19);
    }
    public static HttpRequestActionBuilder selectDaysofHoliday(Map<CharSequence,String> headers_19, String startDate, String endDate) {
        return http("Holidays in the leave period")
                .get("/web/index.php/api/v2/leave/holidays?fromDate="+startDate+"&toDate="+endDate)
                .headers(headers_19);
    }
    public static HttpRequestActionBuilder getEmployeeLeaveType(Map<CharSequence,String> headers_14) {
        return http("Employee's leave type")
                .get("/web/index.php/api/v2/leave/leave-types?limit=0")
                .headers(headers_14);
    }
    public static HttpRequestActionBuilder viewMyWeekDaynOffDay(Map<CharSequence,String> headers_19) {
        return http("Week day and off week day")
                .get("/web/index.php/api/v2/leave/workweek?model=indexed")
                .headers(headers_19);
    }
    public static ChainBuilder applyForLeave(Map<CharSequence,String> headers_27, String startDate, String endDate) {
        return exec(
                http("Apply for leave")
                        .get("/web/index.php/leave/applyLeave")
                        .headers(headers_27)
                        .resources(
                                employeeLeaveTypeEligibility(headers_27),
                                viewMyWeekDaynOffDay(headers_27),
                                selectDaysofHoliday(headers_27, startDate, endDate)
                        )
        );
    }
    public static HttpRequestActionBuilder employeeLeaveTypeEligibility(Map<CharSequence,String> headers_19) {
        return http("Employee leave type eligibility")
                .get("/web/index.php/api/v2/leave/leave-types/eligible")
                .headers(headers_19);
    }
    public static ChainBuilder viewMyLeaveList(Map<CharSequence,String> headers_27) {
        return exec(
                http("View my leave list")
                        .get("/web/index.php/leave/viewMyLeaveList")
                        .headers(headers_27)
        );
    }
    public static ChainBuilder employeeLeaveEntitlement(Map<CharSequence,String> headers_27) {
        return exec(
                http("View Leave Entitlements")
                        .get("/web/index.php/leave/viewLeaveEntitlements")
                        .headers(headers_27)
        );
    }
    public static ChainBuilder employeePersonalDetails(Map<CharSequence,String> headers_93) {
        return exec(
                http("Personal details")
                        .put("/web/index.php/api/v2/pim/employees/7/personal-details")
                        .headers(headers_93)
                        .body(RawFileBody("orangehrmlive/orangehrmrequest/0093_request.json"))
        );
    }
    public static ChainBuilder customFields(Map<CharSequence,String> headers_93) {
        return exec(
                http("Custom fields")
                        .put("/web/index.php/api/v2/pim/employees/7/custom-fields")
                        .headers(headers_93)
                        .body(RawFileBody("orangehrmlive/orangehrmrequest/0093_request.json"))
        );
    }
}
