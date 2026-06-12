package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@Feature("Dashboard")
public class HomePageTests extends BaseTest {

  @Description(
      "Verifies that the home page is displayed correctly and all main elements are visible")
  @Test
  public void navigateTo_homePage_test() {
    verifyHomePage();
  }

  @Ignore("ignore this for now")
  @Test
  public void logout_test() {
    verifyHomePage();
    page.getByAltText(homePage.burgerMenuIconText).click();

    assertThat(page.locator(homePage.changeLanguageID)).isVisible();
    assertThat(page.locator(homePage.changeStoreID)).isVisible();
    assertThat(page.locator(homePage.faqID)).isVisible();
    assertThat(page.locator(homePage.logoutID)).isVisible();

    page.locator(homePage.logoutID).click();
    page.waitForURL("**ppd.np.idp.carrefour.com**");
  }
}
