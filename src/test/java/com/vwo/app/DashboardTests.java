package com.vwo.app;

import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardTests extends BaseTest {
    DashboardPage dashboardPage;

    @Test
    public void dashboardTest() {
        dashboardPage = new DashboardPage(page);
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
    }

    @Test
    public void logout() {
        dashboardPage = new DashboardPage(page);
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
        dashboardPage.logout();
    }

}
