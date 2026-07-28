package ro.carrefour.ucare.app;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

    String burgerMenuIconText = "burger-menu-icon";
    String changeLanguageID = "#change-language";
    String changeStoreID = "#change-store";
    String faqID = "#faq";
    String logoutID = "#logout";

    public HomePage(Page page) {
        super(page);
    }
}
