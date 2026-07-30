package ro.carrefour.ucare;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Item360Page extends BasePage {

    private static final String PRODUCT_IMAGE_ID = "#product-image";
    private static final String PRODUCT_BRAND_ID = "#product-brand";
    private static final String PRODUCT_NAME_ID = "#product-name";

    public Item360Page(Page page) {
        super(page);
    }


    // ── Methods ───────────────────────────────────────────────────────────


    // ── Assertions Shared ─────────────────────────────────────────────────


    // ── Assertions ROMANIA-ONLY ───────────────────────────────────────────

    public void assertItem360PageIsDisplayed() {
        assertThat(page.locator(PRODUCT_IMAGE_ID)).isVisible();
        assertThat(page.locator(PRODUCT_BRAND_ID)).isVisible();
        assertThat(page.locator(PRODUCT_NAME_ID)).isVisible();
    }

}
