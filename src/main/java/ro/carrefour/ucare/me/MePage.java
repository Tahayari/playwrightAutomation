package ro.carrefour.ucare.me;

import com.microsoft.playwright.Page;
import ro.carrefour.ucare.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MePage extends BasePage {

    private static final String ME_PAGE_TITLE = "#me-title";
    private static final String DAYS_OFF_REQUESTS_CARD_TITLE = "#days-off-requests-card-title";
    private static final String PLANNING_VISUALIZATION_OPTION = "#planning";
    private static final String DAYS_OFF_OPTION = "#days-off";
    private static final String MY_CONTACTS_OPTION = "#contacts";

    public MePage(Page page) {
        super(page);
    }

    // ── Methods ───────────────────────────────────────────────────────────


    // ── Assertions Shared ─────────────────────────────────────────────────
    public void assertPageTitleIsDisplayed() {
        assertThat(page.locator(ME_PAGE_TITLE)).isVisible();
    }

    // ── Assertions ROMANIA-ONLY ───────────────────────────────────────────
    public void assertMePageMainElementsAreDisplayed_RO() {
        assertThat(page.locator(DAYS_OFF_REQUESTS_CARD_TITLE)).isVisible();
        assertThat(page.locator(PLANNING_VISUALIZATION_OPTION)).isVisible();
        assertThat(page.locator(DAYS_OFF_OPTION)).isVisible();
        assertThat(page.locator(MY_CONTACTS_OPTION)).isVisible();
    }

}
