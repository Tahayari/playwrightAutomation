package com.carrefour.ucare.e2e.romania.item360;

import com.carrefour.ucare.e2e.BaseTest;
import com.carrefour.ucare.utilities.TestDataManager;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Item360")
public class Item360Tests extends BaseTest {

    @Description("Verifies that searching by a valid product ID returns correct product details")
    @Test(groups = {"INT_ADM"})
    public void navigateTo_item360Page_test() {
        verifyHomePage();

        String testData = TestDataManager.get("item360.internalCode.id_2");
        searchProduct(testData);
        item360Page.assertItem360PageElements_RO();
    }
}
