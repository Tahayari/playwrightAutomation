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

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test(groups = {"INT_ADM"})
  public void navigateTo_item360Page_2() {
    verifyHomePage();

    searchProduct("10005001");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_3() {
    verifyHomePage();

    searchProduct("10005003");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_4() {
    verifyHomePage();

    searchProduct("10005004");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_5() {
    verifyHomePage();

    searchProduct("10005004");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_6() {
    verifyHomePage();

    searchProduct("10005004");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_7() {
    verifyHomePage();

    searchProduct("10005004");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_8() {
    verifyHomePage();

    searchProduct("10005004");
  }

  @Feature("Item360")
  @Description("Verifies that searching by a valid product ID returns correct product details")
  @Issue("TCH-8526")
  @Test
  public void navigateTo_item360Page_9() {
    verifyHomePage();

    searchProduct("10005004");
  }
}
