package ro.carrefour.ucare.e2e;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import ro.carrefour.ucare.LoginPage;

@Feature("Login")
public class LoginPageTests extends BaseTest {

    LoginPage loginPage;

    @Description("Verifies that the login page is displayed correctly and the login button is visible")
    @Test()
    public void verifyLoginButton_test() {
        loginPage = new LoginPage(page);
        loginPage.assertPageIsReady();
    }
}
