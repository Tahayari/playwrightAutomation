package ro.carrefour.ucare;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    private static final String BURGER_MENU_ICON = "burger-menu-icon";
    private static final String CHANGE_LANGUAGE_ID = "#change-language";
    private static final String FAQ_ID = "#faq";
    private static final String LOGOUT_ID = "#logout";

    public HomePage(Page page) {
        super(page);
    }

    public void openSideMenu() {
        page.getByAltText(BURGER_MENU_ICON).click();

        assertThat(page.locator(CHANGE_LANGUAGE_ID)).isVisible();
        assertThat(page.locator(FAQ_ID)).isVisible();
        assertThat(page.locator(LOGOUT_ID)).isVisible();
    }

    public void logoutFromSideMenu() {
        page.locator(LOGOUT_ID).click();
        page.waitForURL("**ppd.np.idp.carrefour.com**");
    }
}
