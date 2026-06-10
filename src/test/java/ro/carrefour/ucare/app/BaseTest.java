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
    private static final Object AUTH_LOCK = new Object();

    @BeforeSuite
    public void beforeSuite() {

        PlaywrightAssertions.setDefaultAssertionTimeout(GLOBAL_TIMEOUT_MS);
    }

    @BeforeTest
    public void beforeTest() {
        PlaywrightFactory.initBrowser();
    }

    @BeforeMethod
    public void beforeMethod() {
        configManager = ConfigManager.getInstance();

        // Guarantee auth state exists on disk before we create the test context.
        // The first thread to arrive logs in; every other thread waits, then skips login.
        ensureAuthState();

        // Auth state is guaranteed from this point — all contexts load a valid session.
        context = PlaywrightFactory.createMobileContext(AUTH_STATE_PATH, GLOBAL_TIMEOUT_MS);
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.navigate(configManager.getProperty("app.url"));

        homePage = new HomePage(page);
        item360Page = new Item360Page(page);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testId = result.getTestClass().getRealClass().getSimpleName()
                + "_" + result.getMethod().getMethodName()
                + "_" + System.currentTimeMillis();

        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(testId);
            saveTrace(testId);
        } else {
            discardTrace();
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
        clearAuthStateFile();
    }

    @AfterTest
    public void afterTest() {
        PlaywrightFactory.closeBrowser();
    }

    // ==========================================
    // AUTH STATE MANAGEMENT
    // ==========================================

    /**
     * Double-checked locking pattern — exactly one login fires per suite run.
     * <p>
     * Fast path: auth state already exists → returns immediately, no locking.
     * Slow path: first thread acquires AUTH_LOCK, logs in via a short-lived
     * temporary context, writes the session to disk, then releases the lock.
     * Every thread that was waiting then sees a valid file and skips login.
     */
    private void ensureAuthState() {
        if (hasValidAuthState()) return; // Fast path — no locking needed

        synchronized (AUTH_LOCK) {
            if (hasValidAuthState()) return; // Another thread logged in while we waited

            BrowserContext loginCtx = PlaywrightFactory.createMobileContext(null, GLOBAL_TIMEOUT_MS);
            Page loginPage = loginCtx.newPage();
            try {
                loginPage.navigate(configManager.getProperty("app.url"));

                LoginPage lp = new LoginPage(loginPage);
                HomePage hp = lp.login(
                        configManager.getProperty("app.username"),
                        configManager.getProperty("app.password")
                );

                assertThat(loginPage.locator(hp.homeFooterMenu)).isVisible();
                assertThat(loginPage.locator(hp.stockFooterMenu)).isVisible();

                Files.createDirectories(Paths.get(AUTH_STATE_PATH).getParent());
                loginCtx.storageState(
                        new BrowserContext.StorageStateOptions()
                                .setPath(Paths.get(AUTH_STATE_PATH))
                );
            } catch (IOException e) {
                throw new RuntimeException("Critical: failed to save authentication session.", e);
            } finally {
                loginPage.close();
                loginCtx.close();
            }
        }
    }

    /**
     * Returns {@code true} only when the auth-state file exists and holds a real session
     * (not missing, empty, or the reset sentinel "{}").
     */
    private boolean hasValidAuthState() {
        Path path = Paths.get(AUTH_STATE_PATH);
        if (!Files.exists(path)) return false;
        try {
            String content = new String(Files.readAllBytes(path)).trim();
            return !content.isEmpty() && !content.equals("{}");
        } catch (IOException e) {
            System.err.println("Warning: could not read auth state file — " + e.getMessage());
            return false;
        }
    }

    /**
     * Resets the auth-state file to an empty JSON object so the next suite
     * run always performs a fresh login (prevents stale / expired sessions).
     */
    private void clearAuthStateFile() {
        try {
            Files.write(Paths.get(AUTH_STATE_PATH), "{}".getBytes());
        } catch (IOException e) {
            System.err.println("Warning: failed to clear auth state file — " + e.getMessage());
        }
    }

    // ==========================================
    // EVIDENCE CAPTURE
    // ==========================================

    private void captureScreenshot(String testId) {
        if (page == null) return;
        try {
            Path dir = Paths.get("target/evidence/screenshots");
            Files.createDirectories(dir);
            page.screenshot(new Page.ScreenshotOptions()
                    .setFullPage(true)
                    .setPath(dir.resolve(testId + ".png")));
        } catch (Exception e) {
            System.err.println("Warning: screenshot capture failed — " + e.getMessage());
        }
    }

    private void saveTrace(String testId) {
        if (context == null) return;
        try {
            Path dir = Paths.get("target/evidence/traces");
            Files.createDirectories(dir);
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(dir.resolve(testId + ".zip")));
        } catch (Exception e) {
            System.err.println("Warning: trace save failed — " + e.getMessage());
        }
    }

    private void discardTrace() {
        if (context == null) return;
        try {
            context.tracing().stop();
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
