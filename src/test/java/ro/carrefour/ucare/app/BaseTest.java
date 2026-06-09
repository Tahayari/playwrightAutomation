package ro.carrefour.ucare.app;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ro.carrefour.ucare.utilities.ConfigManager;
import ro.carrefour.ucare.utilities.PlaywrightFactory;

public class BaseTest {
  protected BrowserContext context;
  protected Page page;
  protected ConfigManager configManager;
  protected HomePage homePage;
  protected Item360Page item360Page;

  private static final String AUTH_STATE_PATH = "./src/main/resources/storageSession.json";
  private static final double GLOBAL_TIMEOUT_MS = 20000;

  @BeforeSuite
  public void beforeSuite() {
    PlaywrightFactory.initBrowser();
    PlaywrightAssertions.setDefaultAssertionTimeout(GLOBAL_TIMEOUT_MS);
  }

  @BeforeTest
  public void beforeTest() {}

  @BeforeMethod
  public void beforeMethod() {

    configManager = ConfigManager.getInstance();
    boolean sessionExists = hasValidAuthState();

    context =
        PlaywrightFactory.createMobileContext(
            sessionExists ? AUTH_STATE_PATH : null, GLOBAL_TIMEOUT_MS);

    context
        .tracing()
        .start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));

    page = context.newPage();
    page.navigate(configManager.getProperty("app.url"));

    if (!sessionExists) {
      performLoginAndSaveState();
    }

    homePage = new HomePage(page);
    item360Page = new Item360Page(page);
  }

  @AfterMethod
  public void afterMethod(ITestResult result) {
    String testId =
        result.getTestClass().getRealClass().getSimpleName()
            + "_"
            + result.getMethod().getMethodName()
            + "_"
            + System.currentTimeMillis();

    if (result.getStatus() == ITestResult.FAILURE) {
      captureScreenshot(testId);
      saveTrace(testId); // also stops tracing with save
    } else {
      discardTrace(); // stops tracing, no file written
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

  @AfterSuite
  public void afterSuite() {
    PlaywrightFactory.closeBrowser();
    clearAuthStateFile();
  }

  // ==========================================
  // PRIVATE HELPERS
  // ==========================================

  /**
   * Returns true only when the auth-state file exists and contains a real session (i.e. is not
   * missing, empty, or the blank-slate "{}" written by clearAuthStateFile).
   */
  private boolean hasValidAuthState() {
    java.nio.file.Path path = Paths.get(AUTH_STATE_PATH);
    if (!Files.exists(path)) return false;
    try {
      String content = new String(Files.readAllBytes(path)).trim();
      return !content.isEmpty() && !content.equals("{}");
    } catch (IOException e) {
      System.err.println("Warning: Could not read auth state file: " + e.getMessage());
      return false;
    }
  }

  /**
   * Executes the login sequence on the current live page, verifies the result, then serialises the
   * browser session to disk for all subsequent tests. Throws RuntimeException on save failure —
   * auth loss is a suite-breaking event.
   */
  private void performLoginAndSaveState() {

    login();

    try {
      Files.createDirectories(Paths.get(AUTH_STATE_PATH).getParent());
      context.storageState(
          new BrowserContext.StorageStateOptions().setPath(Paths.get(AUTH_STATE_PATH)));
    } catch (IOException e) {
      throw new RuntimeException("Critical: failed to save authentication session.", e);
    }
  }

  /**
   * Resets the auth-state file to a blank JSON object so that the next suite run always starts with
   * a clean login, preventing stale/expired sessions.
   */
  private void clearAuthStateFile() {
    try {
      Files.write(Paths.get(AUTH_STATE_PATH), "{}".getBytes());
    } catch (IOException e) {
      System.err.println("Warning: failed to clear auth state file: " + e.getMessage());
    }
  }

  private void captureScreenshot(String testId) {
    if (page == null) return;
    try {
      Path dir = Paths.get("target/evidence/screenshots");
      Files.createDirectories(dir);
      page.screenshot(
          new Page.ScreenshotOptions().setFullPage(true).setPath(dir.resolve(testId + ".png")));
    } catch (Exception e) {
      System.err.println("Warning: screenshot capture failed — " + e.getMessage());
    }
  }

  private void saveTrace(String testId) {
    if (context == null) return;
    try {
      Path dir = Paths.get("target/evidence/traces");
      Files.createDirectories(dir);
      context.tracing().stop(new Tracing.StopOptions().setPath(dir.resolve(testId + ".zip")));
    } catch (Exception e) {
      System.err.println("Warning: trace save failed — " + e.getMessage());
    }
  }

  private void discardTrace() {
    if (context == null) return;
    try {
      context.tracing().stop(); // no path = no file written
    } catch (Exception ignored) {
    }
  }

  // ==========================================
  // PROTECTED HELPERS (for subclasses)
  // ==========================================

  private void login() {
    LoginPage loginPage = new LoginPage(page);
    HomePage homePage =
        loginPage.login(
            configManager.getProperty("app.username"), configManager.getProperty("app.password"));
    assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
    assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
  }

  protected void verifyHomePage() {
    homePage = new HomePage(page);
    assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
    assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
    assertThat(page.locator(homePage.meFooterMenu)).isVisible();
    assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
    assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();
  }

  protected void searchProduct(String id) {
    assertThat(page.locator(homePage.searchInputID)).isVisible();
    page.locator(homePage.searchInputID).fill(id);
    page.locator(homePage.searchInputID).press("Enter");

    item360Page = new Item360Page(page);
    assertThat(page.locator(item360Page.productImageID)).isVisible();
    assertThat(page.locator(item360Page.productBrandID)).isVisible();
    assertThat(page.locator(item360Page.productName)).isVisible();
  }
}
