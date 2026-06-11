package ro.carrefour.ucare.app.item360;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import ro.carrefour.ucare.app.BaseTest;

@Feature("Item360")
public class Item360Tests extends BaseTest {

  @Severity(SeverityLevel.CRITICAL)
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Test
  public void navigateTo_item360Page() {
    verifyHomePage();
    assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
    assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
    assertThat(page.locator(homePage.meFooterMenu)).isVisible();
    assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
    assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();

    searchProduct("10005000");
  }

  @Severity(SeverityLevel.CRITICAL)
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Test
  public void navigateTo_item360Page_2() {
    verifyHomePage();
    assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
    assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
    assertThat(page.locator(homePage.meFooterMenu)).isVisible();
    assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
    assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();

    searchProduct("10005001");
  }

  @Severity(SeverityLevel.CRITICAL)
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Test
  public void navigateTo_item360Page_3() {
    verifyHomePage();
    assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
    assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
    assertThat(page.locator(homePage.meFooterMenu)).isVisible();
    assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
    assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();

    searchProduct("10005003");
  }
}
