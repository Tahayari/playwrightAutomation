package com.vwo.app;

import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPageTest extends BaseTest{

    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Test
    public void loginPage_loginWithCorrectCredentials() {

        loginPage = new LoginPage(page);
        dashboardPage = loginPage.login("ascent.beetle5106@eagereverest.com", "pLan-kiTh-Wides");
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
    }

    @Test
    public void loginPage_loginWithWrongCredentials() {
        loginPage = new LoginPage(page);
        page.fill(loginPage.loginInput, "ascent.beetle5106@eagereverest.com");
        page.fill(loginPage.passwordInput, "123456");
        page.click(loginPage.loginButton);
        assertThat(page.locator(loginPage.invalidCredentialsMsg)).isVisible();
    }

}
