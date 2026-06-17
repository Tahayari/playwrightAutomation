package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static io.qameta.allure.Allure.step;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ro.carrefour.ucare.app.me.MePage;
import ro.carrefour.ucare.app.stock.StockPage;
import ro.carrefour.ucare.utilities.AuthStateManager;
import ro.carrefour.ucare.utilities.ConfigManager;
import ro.carrefour.ucare.utilities.EvidenceManager;
import ro.carrefour.ucare.utilities.PlaywrightFactory;

public class BaseTest {

  protected BrowserContext context;
  protected Page page;
  protected ConfigManager configManager;
  protected HomePage homePage;
  protected Item360Page item360Page;
  protected StockPage stockPage;
  protected MePage mePage;

  private static final String AUTH_STATE_PATH = "./src/main/resources/storageSession.json";
  private static final double GLOBAL_TIMEOUT_MS = 20000;

  // Shared across all instances and threads — manages the one session file on disk
  private static final AuthStateManager authStateManager = new AuthStateManager(AUTH_STATE_PATH);

  // Per-test-method — wraps the current page and context for failure capture
  protected EvidenceManager evidenceManager;

  // ==========================================
  // LIFECYCLE
  // ==========================================

  @BeforeSuite
  public void beforeSuite() {
    // Static assertion timeout — safe to set once from the main thread
    PlaywrightAssertions.setDefaultAssertionTimeout(GLOBAL_TIMEOUT_MS);
  }

  @BeforeTest
  public void beforeTest() {
    // Runs in the worker thread — each thread must own its own Playwright + Browser
    PlaywrightFactory.initBrowser();
  }

  @BeforeMethod
  public void beforeMethod() {
    configManager = ConfigManager.getInstance();

    // Guarantees a valid session file exists before any test context loads it.
    // First thread logs in via a temporary context; all others wait, then skip.
    authStateManager.ensureAuthState(this::performLogin);

    context = PlaywrightFactory.createMobileContext(AUTH_STATE_PATH, GLOBAL_TIMEOUT_MS);
    context
        .tracing()
        .start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));

    step("Navigate to Dashboard page");
    page = context.newPage();
    page.navigate(configManager.getProperty("app.url"));

    homePage = new HomePage(page);
    item360Page = new Item360Page(page);
    evidenceManager = new EvidenceManager(page, context);
  }

  @AfterMethod
  public void afterMethod(ITestResult result) {
    // Guard against @BeforeMethod failing before evidenceManager was initialized
    if (evidenceManager != null) {
      String testId =
          result.getTestClass().getRealClass().getSimpleName()
              + "_"
              + result.getMethod().getMethodName()
              + "_"
              + System.currentTimeMillis();

      if (result.getStatus() == ITestResult.FAILURE) {
        evidenceManager.captureEvidence(testId);
      } else {
        evidenceManager.discardTrace();
      }
    }

    if (page != null) {
      page.close();
      page = null;
    }
    if (context != null) {
      context.close();
      context = null;
    }
  }

  @AfterTest
  public void afterTest() {
    // Mirrors @BeforeTest: each thread disposes its own browser
    PlaywrightFactory.closeBrowser();
  }

  @AfterSuite
  public void afterSuite() {
    // Wipe the session so the next run always starts with a fresh login
    authStateManager.clearAuthStateFile();
  }

  // ==========================================
  // PRIVATE — login action passed to AuthStateManager
  // ==========================================

  private void performLogin() {
    BrowserContext loginCtx = PlaywrightFactory.createMobileContext(null, GLOBAL_TIMEOUT_MS);
    Page loginPage = loginCtx.newPage();
    try {
      loginPage.navigate(configManager.getProperty("app.url"));

      LoginPage lp = new LoginPage(loginPage);
      HomePage hp =
          lp.login(
              configManager.getProperty("app.username"), configManager.getProperty("app.password"));

      assertThat(loginPage.locator(hp.homeFooterMenu)).isVisible();
      assertThat(loginPage.locator(hp.stockFooterMenu)).isVisible();

      Files.createDirectories(Paths.get(AUTH_STATE_PATH).getParent());
      loginCtx.storageState(
          new BrowserContext.StorageStateOptions().setPath(Paths.get(AUTH_STATE_PATH)));
    } catch (IOException e) {
      throw new RuntimeException("Critical: failed to save authentication session.", e);
    } finally {
      loginPage.close();
      loginCtx.close();
    }
  }

  // ==========================================
  // PROTECTED HELPERS (for subclasses)
  // ==========================================

  @Step("Verify home page is fully loaded")
  protected void verifyHomePage() {
    step(
        "Verify home page main elements are visible",
        () -> {
          assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
          assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
          assertThat(page.locator(homePage.meFooterMenu)).isVisible();
          assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
          assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();
          step("Main tabs are displayed");
        });
  }

  //  @Step("Search for product: {id}")
  protected void searchProduct(String id) {
    step(
        "Search for product with ID: " + id,
        () -> {
          step("Verify if search input is displayed");
          assertThat(page.locator(homePage.searchInputID)).isVisible();

          step("Enter product id {id}");
          page.locator(homePage.searchInputID).fill(id);
          page.locator(homePage.searchInputID).press("Enter");

          step("Verify if item360 page is displayed");
          assertThat(page.locator(item360Page.productImageID)).isVisible();
          assertThat(page.locator(item360Page.productBrandID)).isVisible();
          assertThat(page.locator(item360Page.productName)).isVisible();
          step("Item360 page is displayed");
        });
  }

  protected void navigateToStockPage() {
    stockPage = new StockPage(page);
    step(
        "Navigate to Stock Page",
        () -> {
          page.locator(homePage.stockFooterMenu).click();
          step("Verify if Stock page is displayed");
          assertThat(page.locator(stockPage.stockPageTitle)).isVisible();
          step("Stock page is displayed");

          assertThat(page.locator(stockPage.oosCard)).isVisible();
          assertThat(page.locator(stockPage.negativeStockCard)).isVisible();
          assertThat(page.locator(stockPage.regularOrderOption)).isVisible();
          assertThat(page.locator(stockPage.orderValidationOption)).isVisible();
          assertThat(page.locator(stockPage.expiringProductsOption)).isVisible();
          assertThat(page.locator(stockPage.outOfShelfOption)).isVisible();
          assertThat(page.locator(stockPage.stockMovementsOption)).isVisible();
          assertThat(page.locator(stockPage.stockTransferOption)).isVisible();
          assertThat(page.locator(stockPage.palletsOption)).isVisible();
          assertThat(page.locator(stockPage.generalInventoryOption)).isVisible();
          assertThat(page.locator(stockPage.partialInventoryOption)).isVisible();
          step("Stock page main elements are displayed");
        });
  }

  protected void navigateToMePage() {
    mePage = new MePage(page);
    step(
        "Navigate to me page",
        () -> {
          page.locator(homePage.meFooterMenu).click();
          step("Verify if Me page is displayed");
          assertThat(page.locator(mePage.mePageTitle)).isVisible();
          step("Me page is displayed");

          //            assertThat(page.locator(mePage.daysOffRequestsCard)).isVisible();
          assertThat(page.locator(mePage.planningVisualizationOption)).isVisible();
          assertThat(page.locator(mePage.daysOffOption)).isVisible();
          assertThat(page.locator(mePage.myContactsOption)).isVisible();
          step("Me page elements are displayed");
        });
  }
}
