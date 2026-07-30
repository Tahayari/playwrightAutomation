package ro.carrefour.ucare.e2e;

import static io.qameta.allure.Allure.step;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import io.qameta.allure.Step;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ro.carrefour.ucare.HomePage;
import ro.carrefour.ucare.Item360Page;
import ro.carrefour.ucare.LoginPage;
import ro.carrefour.ucare.me.MePage;
import ro.carrefour.ucare.stock.StockPage;
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
        context.tracing()
                .start(
                        new Tracing.StartOptions()
                                .setScreenshots(true)
                                .setSnapshots(true)
                                .setSources(true));

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
        PlaywrightFactory.closeBrowser();
    }

    @AfterSuite
    public void afterSuite() {
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
                            configManager.getProperty("app.username"),
                            configManager.getProperty("app.password"));

            hp.assertHomePageIsDisplayed();

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
                    homePage.assertAllTabsAreDisplayed_RO();
                    step("Main tabs are displayed");
                });
    }

    //  @Step("Search for product: {id}")
    protected void searchProduct(String id) {
        step(
                "Search for product with ID: " + id,
                () -> {
                    step("Verify if search input is displayed");
                    homePage.assertSearchInputIsDisplayed();

                    step("Enter product id : " + id);
                    homePage.insertTextToSearchInput(id);

                    step("Verify if item360 page is displayed");
                    item360Page.assertItem360PageIsDisplayed();

                    step("Item360 page is displayed");
                });
    }

    protected void navigateToStockPage() {
        stockPage = new StockPage(page);
        step(
                "Navigate to Stock Page",
                () -> {
                    homePage.navigateToStockPage();

                    step("Verify if Stock page is displayed");
                    stockPage.assertPageTitleIsDisplayed();

                    step("Verify if Stock page main elements are displayed");
                    stockPage.assertStockPageMainElementsAreDisplayed_RO();
                });
    }

    protected void navigateToMePage() {
        mePage = new MePage(page);
        step(
                "Navigate to me page",
                () -> {
                    homePage.navigateToMePage();

                    step("Verify if Me page is displayed");
                    mePage.assertPageTitleIsDisplayed();

                    step("Verify if Me page elements are displayed");
                    mePage.assertMePageMainElementsAreDisplayed_RO();
                });
    }
}
