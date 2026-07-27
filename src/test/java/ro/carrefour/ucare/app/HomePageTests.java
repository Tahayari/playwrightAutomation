package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Dashboard")
public class HomePageTests extends BaseTest {

  @Description(
      "Verifies that the home page is displayed correctly and all main elements are visible")
  @Test
  public void navigateTo_homePage_test() {
    verifyHomePage();
  }

  @Description("Logout from the side menu")
  @Test()
  public void logout_test() {
    verifyHomePage();

    step("Tap on the burger menu from the side menu");
    page.getByAltText(homePage.burgerMenuIconText).click();

    assertThat(page.locator(homePage.changeLanguageID)).isVisible();
    assertThat(page.locator(homePage.changeStoreID)).isVisible();
    assertThat(page.locator(homePage.faqID)).isVisible();
    assertThat(page.locator(homePage.logoutID)).isVisible();
    step("Side menu is opened");

    step("Tap on the logout option from the side menu");
    page.locator(homePage.logoutID).click();
    page.waitForURL("**ppd.np.idp.carrefour.com**");
    step("User is successfully logged out");
  }
}
