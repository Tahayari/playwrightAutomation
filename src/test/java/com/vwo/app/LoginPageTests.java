package com.vwo.app;

import com.vwo.utilities.ConfigManager;
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
        assertThat(page.locator(loginPage.loginButton)).isVisible();
    }

    @Test(groups = "no-auth")
    public void loginPage_loginWithWrongCredentials() {
        loginPage = new LoginPage(page);
        page.click(loginPage.loginButton);
        assertThat(page.locator(loginPage.invalidCredentialsMsg)).isVisible();
    }

}
