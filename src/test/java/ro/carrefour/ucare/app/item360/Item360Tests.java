package ro.carrefour.ucare.app.item360;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.testng.annotations.Test;
import ro.carrefour.ucare.app.BaseTest;

public class Item360Tests extends BaseTest {

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
