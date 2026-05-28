package ro.carrefour.ucare.app;

import ro.carrefour.ucare.utilities.ConfigManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageTests extends BaseTest {

    LoginPage loginPage;

    @BeforeTest
    public void beforeTest() {

    }

    @BeforeMethod
    public void beforeMethod() {
        context = browser.newContext();
        page = context.newPage();
        configManager = ConfigManager.getInstance();
        page.navigate(configManager.getProperty("app.url"));
    }

    @Test()
    public void loginPage_verifyLoginButton() {
        loginPage = new LoginPage(page);
        page.waitForURL("**ppd.np.idp.carrefour.com**");
        assertThat(page.locator(loginPage.signInButton)).isVisible();
    }

}
