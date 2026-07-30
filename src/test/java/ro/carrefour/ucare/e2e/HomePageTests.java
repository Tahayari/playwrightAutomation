package ro.carrefour.ucare.e2e;

import static io.qameta.allure.Allure.step;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Dashboard")
public class HomePageTests extends BaseTest {

    @Description(
            "Verifies that the home page is displayed correctly and all main elements are visible")
    @Test
    public void navigateTo_homePage_test() {
        verifyHomePage();
    }

    @Description("Logout from the side menu")
    @Test()
    public void logout_test() {
        verifyHomePage();

        step("Open the side menu");
        homePage.openSideMenu();

        step("Tap on the logout option from the side menu");
        homePage.logoutFromSideMenu();

        step("User is successfully logged out");
    }
}
