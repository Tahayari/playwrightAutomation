package ro.carrefour.ucare.e2e.stock;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ro.carrefour.ucare.e2e.BaseTest;

public class StockPageTests extends BaseTest {

    @Description("Verify that the Stock page loads correctly and displays expected elements")
    @Test
    public void navigateTo_StockPage_success_test() {
        verifyHomePage();
        navigateToStockPage();
    }

    @Description("Verify that you can scan a product from Stock page")
    @Test
    public void navigateTo_Item360fromStockPage_success_test() {
        verifyHomePage();
        navigateToStockPage();
        searchProduct("10005000");
    }
}
