package com.vwo.app;

import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardTests extends BaseTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Test
    public void dashboardTest() {
        loginPage = new LoginPage(page);
        dashboardPage = loginPage.login("ascent.beetle5106@eagereverest.com", "pLan-kiTh-Wides");
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
    }

    @Test
    public void logout_success() {
        loginPage = new LoginPage(page);
        dashboardPage = loginPage.login("ascent.beetle5106@eagereverest.com", "pLan-kiTh-Wides");
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
        dashboardPage.logout();
    }
}
