package com.vwo.app;

import com.microsoft.playwright.Page;
import org.testng.Assert;

public class LoginPage {

    private final Page page;

    String loginInput = "#login-username";
    String passwordInput = "#login-password";
    String loginButton = "#js-login-btn";
    String invalidCredentialsMsg = "[data-qa='rixawilomi']";

    public LoginPage(Page page) {
        this.page = page;
    }

    public DashboardPage login(String username, String password) {
        page.fill(loginInput, username);
        page.fill(passwordInput, password);
        page.click(loginButton);
        page.waitForURL("**accountId=**");
        Assert.assertTrue(page.url().contains("accountId="));

        return new DashboardPage(page);
    }

}