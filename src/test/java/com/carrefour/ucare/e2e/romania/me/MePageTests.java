package com.carrefour.ucare.e2e.romania.me;

import com.carrefour.ucare.e2e.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Me")
public class MePageTests extends BaseTest {

    @Description("Verify that the Me page loads correctly and displays expected elements")
    @Test
    public void navigateToMePage_success_test() {
        verifyHomePage();
        navigateToMePage();
    }
}
