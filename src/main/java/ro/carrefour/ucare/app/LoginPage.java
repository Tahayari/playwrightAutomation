package ro.carrefour.ucare.app;

import static org.testng.Assert.assertTrue;

import com.microsoft.playwright.Page;

public class LoginPage {

  private final Page page;

  String loginInput = "#idToken3";
  String passwordInput = "#idToken4";
  String signInButton = "#loginButton_0";

  public LoginPage(Page page) {
    this.page = page;
  }

  public HomePage login(String username, String password) {
    page.fill(loginInput, username);
    page.fill(passwordInput, password);
    page.click(signInButton);
    page.waitForURL("**ucare-uat.tc.carrefour.ro**");
    assertTrue(page.url().contains("tc.carrefour.ro"));

    return new HomePage(page);
  }
}
