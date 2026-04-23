package com.vwo.app;

import com.microsoft.playwright.Page;

public class DashboardPage {

    private final Page page;

    String needHelpBtn = "[data-qa='csm-support-widget-container']";
    String userMenuBtn = "[data-qa='btn-user-menu']";
    String logoutBtn = "[data-qa='logout-btn']";

    public DashboardPage(Page page) {
        this.page = page;
    }

    public void logout() {
        page.locator(userMenuBtn).hover();
        page.locator(logoutBtn).click();
        page.waitForURL("**/login");
    }

}
