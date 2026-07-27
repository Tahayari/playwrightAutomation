package ro.carrefour.ucare.app.me;

import io.qameta.allure.Description;
import org.testng.annotations.Test;
import ro.carrefour.ucare.app.BaseTest;

public class MePageTests extends BaseTest {

  @Description("Verify that the Me page loads correctly and displays expected elements")
  @Test
  public void navigateToMePage_success() {
    verifyHomePage();
    navigateToMePage();
  }
}
