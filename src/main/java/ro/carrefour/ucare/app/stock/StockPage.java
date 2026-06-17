package ro.carrefour.ucare.app.stock;

import com.microsoft.playwright.Page;
import ro.carrefour.ucare.app.BasePage;

public class StockPage extends BasePage {

  public String stockPageTitle = "#stock-title";
  public String oosCard = "//div[@data-testid='stock-carousel']/div[1]";
  public String negativeStockCard = "#negative-stock-card";
  public String regularOrderOption = "#regular-order";
  public String orderValidationOption = "#order-validation";
  public String expiringProductsOption = "#expiry";
  public String outOfShelfOption = "#out-of-shelf";
  public String stockMovementsOption = "#stock-movement";
  public String stockTransferOption = "#stock-transfer";
  public String palletsOption = "#pallet";
  public String generalInventoryOption = "#inventory";
  public String partialInventoryOption = "#partial-inventory";

  public StockPage(Page page) {}
}
