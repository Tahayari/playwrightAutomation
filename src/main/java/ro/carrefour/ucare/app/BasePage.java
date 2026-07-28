package ro.carrefour.ucare.app;

import com.microsoft.playwright.Page;

public class BasePage {

  protected Page page;

  public String homeFooterMenu = "#home-footer-menu";
  public String stockFooterMenu = "#stock-footer-menu";
  public String meFooterMenu = "#me-footer-menu";
  public String priceFooterMenu = "#price-footer-menu";
  public String notificationsFooterMenu = "#notifications-footer-menu";
  public String searchInputID = "#product-search-input";

  public BasePage(Page page) {this.page = page;}

}
