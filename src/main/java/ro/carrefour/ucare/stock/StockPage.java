package ro.carrefour.ucare.stock;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import ro.carrefour.ucare.BasePage;

public class StockPage extends BasePage {

    private static final String STOCK_PAGE_TITLE = "#stock-title";
    private static final String OOS_CARD = "//div[@data-testid='stock-carousel']/div[1]";
    private static final String NEGATIVE_STOCK_CARD = "#negative-stock-card";
    private static final String REGULAR_ORDER_OPTION = "#regular-order";
    private static final String ORDER_VALIDATION_OPTION = "#order-validation";
    private static final String EXPIRING_PRODUCTS_OPTION = "#expiry";
    private static final String OUT_OF_SHELF_OPTION = "#out-of-shelf";
    private static final String STOCK_MOVEMENTS_OPTION = "#stock-movement";
    private static final String STOCK_TRANSFER_OPTION = "#stock-transfer";
    private static final String PALLETS_OPTION = "#pallet";
    private static final String GENERAL_INVENTORY_OPTION = "#inventory";
    private static final String PARTIAL_INVENTORY_OPTION = "#partial-inventory";

    public StockPage(Page page) {
        super(page);
    }

    // ── Methods ───────────────────────────────────────────────────────────

    // ── Assertions Shared ─────────────────────────────────────────────────
    public void assertPageTitleIsDisplayed() {
        assertThat(page.locator(STOCK_PAGE_TITLE)).isVisible();
    }

    // ── Assertions ROMANIA-ONLY ───────────────────────────────────────────
    public void assertStockPageMainElementsAreDisplayed_RO() {
        assertThat(page.locator(OOS_CARD)).isVisible();
        //        assertThat(page.locator(negativeStockCard)).isVisible();
        assertThat(page.locator(REGULAR_ORDER_OPTION)).isVisible();
        assertThat(page.locator(ORDER_VALIDATION_OPTION)).isVisible();
        assertThat(page.locator(EXPIRING_PRODUCTS_OPTION)).isVisible();
        assertThat(page.locator(OUT_OF_SHELF_OPTION)).isVisible();
        assertThat(page.locator(STOCK_MOVEMENTS_OPTION)).isVisible();
        assertThat(page.locator(STOCK_TRANSFER_OPTION)).isVisible();
        assertThat(page.locator(PALLETS_OPTION)).isVisible();
        assertThat(page.locator(GENERAL_INVENTORY_OPTION)).isVisible();
        assertThat(page.locator(PARTIAL_INVENTORY_OPTION)).isVisible();
    }
}
