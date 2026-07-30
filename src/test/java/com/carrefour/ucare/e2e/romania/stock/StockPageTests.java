package com.carrefour.ucare.e2e.romania.stock;

import com.carrefour.ucare.e2e.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Stock")
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
