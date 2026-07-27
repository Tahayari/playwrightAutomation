package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Login")
public class LoginPageTests extends BaseTest {

  LoginPage loginPage;

  @Description(
      "Verifies that the login page is displayed correctly and the login button is visible")
  @Test()
  public void loginPage_verifyLoginButton() {
    loginPage = new LoginPage(page);
    page.waitForURL("**ppd.np.idp.carrefour.com**");
    assertThat(page.locator(loginPage.signInButton)).isVisible();
  }
}
