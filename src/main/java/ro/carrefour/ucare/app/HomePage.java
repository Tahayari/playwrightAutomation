package ro.carrefour.ucare.app;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {

  private final Page page;

  String burgerMenuIconText = "burger-menu-icon";
  String changeLanguageID = "#change-language";
  String changeStoreID = "#change-store";
  String faqID = "#faq";
  String logoutID = "#logout";

  public HomePage(Page page) {
    this.page = page;
  }
}
