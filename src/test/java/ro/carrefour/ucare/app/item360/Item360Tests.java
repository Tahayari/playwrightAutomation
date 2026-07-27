package ro.carrefour.ucare.app.item360;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;
import ro.carrefour.ucare.app.BaseTest;

@Feature("Item360")
public class Item360Tests extends BaseTest {

    @Feature("Item360")
    @Description("Verifies that searching by a valid product ID returns correct product details")
    @Issue("TCH-8526")
    @Test(groups = {"INT_ADM"})
    public void navigateTo_item360Page() {
        verifyHomePage();

        searchProduct("10005000");
    }


}
