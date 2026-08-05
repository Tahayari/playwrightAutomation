package com.carrefour.ucare;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    private static final String LOGIN_INPUT = "#idToken3";
    private static final String PASSWORD_INPUT = "#idToken4";
    private static final String SIGN_IN_BUTTON = "#loginButton_0";

    public LoginPage(Page page) {
        this.page = page;
    }

    // ── Methods ───────────────────────────────────────────────────────────

    public HomePage login(String username, String password) {
        String env = System.getProperty("env", "ro").toLowerCase();
        page.fill(LOGIN_INPUT, username);
        page.fill(PASSWORD_INPUT, password);
        page.click(SIGN_IN_BUTTON);
        switch (env) {
            case "ro":
                page.waitForURL("**ucare-uat.tc.carrefour.ro**");
                break;
            case "fr":
                page.waitForURL("**fr-uat.ucare.carrefour.com**");
                break;
            default:
                throw new IllegalArgumentException("Invalid environment: " + env);
        }
        return new HomePage(page);
    }

    // ── Assertions ────────────────────────────────────────────────────────
    public void assertPageIsReady() {
        page.waitForURL("**ppd.np.idp.carrefour.com**");
        assertThat(page.locator(SIGN_IN_BUTTON)).isVisible();
    }
}
