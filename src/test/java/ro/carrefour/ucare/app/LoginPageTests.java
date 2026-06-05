package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.testng.annotations.Test;

public class LoginPageTests extends BaseTest {

  LoginPage loginPage;

  @Test()
  public void loginPage_verifyLoginButton() {
    loginPage = new LoginPage(page);
    page.waitForURL("**ppd.np.idp.carrefour.com**");
    assertThat(page.locator(loginPage.signInButton)).isVisible();
  }
}
