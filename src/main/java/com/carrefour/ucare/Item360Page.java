package com.carrefour.ucare;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;

public class Item360Page extends BasePage {

    private static final String PRODUCT_IMAGE_ID = "#product-image";
    private static final String PRODUCT_BRAND_ID = "#product-brand";
    private static final String PRODUCT_NAME_ID = "#product-name";
    private static final String PRODUCT_GTIN_ID = "#product-gtin";

    private static final String PRODUCT_INTERNAL_CODE_ID = "#product-internal-code";
    private static final String PRODUCT_STATUS_ID = "#product-status";
    private static final String ITEM_STOCK_ID = "#stock-badge";
    private static final String PRESENTATION_FACING_ID = "#presentationFacing";
    private static final String SHELF_CAPACITY_ID = "#capacity";

    private static final String PRICE_TAB_ID = "#tab-0-price-tab";
    private static final String STOCK_TAB_ID = "#tab-1-stock-tab";
    private static final String DETAILS_TAB_ID = "#tab-2-detail-stock-tab";
    private static final String MERCH_TAB_ID = "#tab-3-merchandising-tab";

    private static final String PERMANENT_PRICE_ID = "#primary-permanent-0";
    private static final String ITEM_MARGIN_RATE_ID = "#primary-margin-1";
    private static final String RECOMMENDED_PRICE_ID = "#primary-recommended-1";

    private static final String GO_TO_PRICE_AUDIT_ID = "//button[@label='Go to Price audit']";
    private static final String GO_TO_LABEL_MGMT_ID = "//button[@label='Go to Label mgmt']";

    public Item360Page(Page page) {
        super(page);
    }

    // ── Methods ───────────────────────────────────────────────────────────

    // ── Assertions Shared ─────────────────────────────────────────────────

    // ── Assertions ROMANIA-ONLY ───────────────────────────────────────────

    public void assertItem360PageIsDisplayed() {
        page.waitForURL("**/product-information/**/price");
        assertThat(page.locator(PRODUCT_IMAGE_ID)).isVisible();
        assertThat(page.locator(PRODUCT_NAME_ID)).isVisible();
    }

    public void assertItem360PageElements_RO() {
        assertThat(page.locator(PRODUCT_BRAND_ID)).isVisible();
        assertThat(page.locator(PRODUCT_GTIN_ID)).isVisible();
        assertThat(page.locator(PRODUCT_INTERNAL_CODE_ID)).isVisible();
        assertThat(page.locator(PRODUCT_STATUS_ID)).isVisible();
        assertThat(page.locator(ITEM_STOCK_ID)).isVisible();
        assertThat(page.locator(PRESENTATION_FACING_ID)).isVisible();
        assertThat(page.locator(SHELF_CAPACITY_ID)).isVisible();
        assertThat(page.locator(PRICE_TAB_ID)).isVisible();
        assertThat(page.locator(STOCK_TAB_ID)).isVisible();
        assertThat(page.locator(DETAILS_TAB_ID)).isVisible();
        assertThat(page.locator(MERCH_TAB_ID)).isVisible();
        assertThat(page.locator(PERMANENT_PRICE_ID)).isVisible();
        assertThat(page.locator(ITEM_MARGIN_RATE_ID)).isVisible();
        assertThat(page.locator(GO_TO_PRICE_AUDIT_ID)).isVisible();
        assertThat(page.locator(GO_TO_LABEL_MGMT_ID)).isVisible();
    }

    // ── Assertions FRANCE-ONLY ───────────────────────────────────────────

    public void assertItem360PageElements_FR() {
        assertThat(page.locator(PRODUCT_BRAND_ID)).isVisible();
        assertThat(page.locator(PRODUCT_GTIN_ID)).isVisible();
        assertThat(page.locator(PRODUCT_INTERNAL_CODE_ID)).isVisible();
        assertThat(page.locator(PRODUCT_STATUS_ID)).isVisible();
        assertThat(page.locator(ITEM_STOCK_ID)).isVisible();
        assertThat(page.locator(PRICE_TAB_ID)).isVisible();
        assertThat(page.locator(STOCK_TAB_ID)).isVisible();
        assertThat(page.locator(DETAILS_TAB_ID)).isVisible();
        assertThat(page.locator(MERCH_TAB_ID)).isVisible();
        assertThat(page.locator(RECOMMENDED_PRICE_ID)).isVisible();
    }
}
