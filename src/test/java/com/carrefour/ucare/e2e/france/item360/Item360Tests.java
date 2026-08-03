package com.carrefour.ucare.e2e.france.item360;

import com.carrefour.ucare.e2e.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Item360")
public class Item360Tests extends BaseTest {

    @Description("Verifies that searching by a valid product ID returns correct product details")
    @Test(groups = {"INT_ADM"})
    public void navigateTo_item360Page_test() {
        verifyHomePage();

        searchProduct("0022133");

        item360Page.assertItem360PageElements_FR();
    }
}
