package ro.carrefour.ucare.app.stock;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import ro.carrefour.ucare.app.BaseTest;

public class StockPageTests extends BaseTest {

  @Description("Verify that the Stock page loads correctly and displays expected elements")
  @Test
  public void navigateToStockPage_success() {
    verifyHomePage();
    navigateToStockPage();
  }

  @Issue("IMU-764")
  @Description("Verify that you can scan a product from Stock page")
  @Test
  public void navigateToItem360fromStockPage_success() {
    verifyHomePage();
    navigateToStockPage();
    searchProduct("10005000");
  }
}
