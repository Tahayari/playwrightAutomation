package ro.carrefour.ucare;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {

    protected Page page;

    private static final String HOME_FOOTER_MENU = "#home-footer-menu";
    private static final String STOCK_FOOTER_MENU = "#stock-footer-menu";
    private static final String ME_FOOTER_MENU = "#me-footer-menu";
    private static final String PRICE_FOOTER_MENU = "#price-footer-menu";
    private static final String NOTIFICATIONS_FOOTER_MENU = "#notifications-footer-menu";
    private static final String PRODUCT_SEARCH_INPUT = "#product-search-input";

    public BasePage(Page page) {
        this.page = page;
    }

    // ── Methods ───────────────────────────────────────────────────────────
    public void insertTextToSearchInput(String text) {
        page.locator(PRODUCT_SEARCH_INPUT).fill(text);
        page.locator(PRODUCT_SEARCH_INPUT).press("Enter");
    }

    public void navigateToHomePage() {
        page.locator(HOME_FOOTER_MENU).click();
    }

    public void navigateToStockPage() {
        page.locator(STOCK_FOOTER_MENU).click();
    }

    public  void navigateToMePage() {
        page.locator(ME_FOOTER_MENU).click();
    }

    // ── Assertions Shared ─────────────────────────────────────────────────
    public void assertHomePageIsDisplayed() {
        assertThat(page.locator(HOME_FOOTER_MENU)).isVisible();
        assertThat(page.locator(STOCK_FOOTER_MENU)).isVisible();
    }

    public void assertSearchInputIsDisplayed() {
        assertThat(page.locator(PRODUCT_SEARCH_INPUT)).isVisible();
    }

    // ── Assertions ROMANIA-ONLY ───────────────────────────────────────────
    public void assertAllTabsAreDisplayed_RO() {
        assertThat(page.locator(HOME_FOOTER_MENU)).isVisible();
        assertThat(page.locator(STOCK_FOOTER_MENU)).isVisible();
        assertThat(page.locator(ME_FOOTER_MENU)).isVisible();
        assertThat(page.locator(PRICE_FOOTER_MENU)).isVisible();
        assertThat(page.locator(NOTIFICATIONS_FOOTER_MENU)).isVisible();
        assertThat(page.locator(PRODUCT_SEARCH_INPUT)).isVisible();
    }

}
