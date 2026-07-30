package ro.carrefour.ucare.e2e.me;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ro.carrefour.ucare.e2e.BaseTest;

public class MePageTests extends BaseTest {

    @Description("Verify that the Me page loads correctly and displays expected elements")
    @Test
    public void navigateToMePage_success_test() {
        verifyHomePage();
        navigateToMePage();
    }
}
