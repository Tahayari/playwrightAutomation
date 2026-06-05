package ro.carrefour.ucare.app;

import com.microsoft.playwright.Page;

public class Item360Page extends BasePage {

  private final Page page;

  public String productImageID = "#product-image";
  public String productBrandID = "#product-brand";
  public String productName = "#product-name";

  public Item360Page(Page page) {
    this.page = page;
  }
}
