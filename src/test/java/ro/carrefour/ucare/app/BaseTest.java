package ro.carrefour.ucare.app;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import ro.carrefour.ucare.utilities.ConfigManager;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected ConfigManager configManager;
    protected HomePage homePage;
    protected Item360Page item360Page;

    private final String AUTH_STATE_PATH = "./src/main/resources/storageSession.json";
    private final double GLOBAL_TIMEOUT_MS = 20000;
    private final String MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; Android 12; DT50_5G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Mobile Safari/537.36";
    private final int MOBILE_WIDTH = 360;
    private final int MOBILE_HEIGHT = 720;
    private final double MOBILE_SCALE_FACTOR = 2.0;

    @BeforeSuite
    public void beforeSuite() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        PlaywrightAssertions.setDefaultAssertionTimeout(GLOBAL_TIMEOUT_MS);
    }

    @BeforeTest
    public void beforeTest() {
        BrowserContext tempContext = browser.newContext(new Browser.NewContextOptions()
                .setUserAgent(MOBILE_USER_AGENT)
                .setViewportSize(MOBILE_WIDTH, MOBILE_HEIGHT)
                .setDeviceScaleFactor(MOBILE_SCALE_FACTOR)
                .setIsMobile(true)
                .setHasTouch(true));
        tempContext.setDefaultTimeout(GLOBAL_TIMEOUT_MS);

        page = tempContext.newPage();
        configManager = ConfigManager.getInstance();
        page.navigate(configManager.getProperty("app.url"));

        login();
        tempContext.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(AUTH_STATE_PATH)));

        page.close();
        tempContext.close();
    }

    @BeforeMethod
    public void beforeMethod() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setStorageStatePath(Paths.get(AUTH_STATE_PATH))
                .setUserAgent(MOBILE_USER_AGENT)
                .setViewportSize(MOBILE_WIDTH, MOBILE_HEIGHT)
                .setDeviceScaleFactor(MOBILE_SCALE_FACTOR)
                .setIsMobile(true)
                .setHasTouch(true));
        context.setDefaultTimeout(GLOBAL_TIMEOUT_MS);

        page = context.newPage();
        page.navigate(configManager.getProperty("app.url"));
    }

    @AfterMethod
    public void afterMethod() {
        page.close();
    }

    @AfterTest
    public void afterTest() {
        context.close();
    }

    @AfterSuite
    public void afterSuite() {
        browser.close();
        playwright.close();
        clearAuthStateFile();
    }

    //============================

    private void login() {
        LoginPage loginPage = new LoginPage(page);
        HomePage homePage = loginPage.login(configManager.getProperty("app.username"),
                configManager.getProperty("app.password"));
        assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
        assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
    }

    private void clearAuthStateFile() {
        try {
            java.nio.file.Files.write(
                    Paths.get(AUTH_STATE_PATH),
                    "{}".getBytes()
            );
        } catch (IOException e) {
            System.err.println("Failed to clear storage state file: " + e.getMessage());
        }
    }

    protected void verifyHomePage() {
        homePage = new HomePage(page);
        assertThat(page.locator(homePage.homeFooterMenu)).isVisible();
        assertThat(page.locator(homePage.stockFooterMenu)).isVisible();
        assertThat(page.locator(homePage.meFooterMenu)).isVisible();
        assertThat(page.locator(homePage.priceFooterMenu)).isVisible();
        assertThat(page.locator(homePage.notificationsFooterMenu)).isVisible();
    }

    protected void searchProduct(String id){
        assertThat(page.locator(homePage.searchInputID)).isVisible();

        page.locator(homePage.searchInputID).fill(id);
        page.locator(homePage.searchInputID).press("Enter");

        item360Page = new Item360Page(page);
        assertThat(page.locator(item360Page.productImageID)).isVisible();
        assertThat(page.locator(item360Page.productBrandID)).isVisible();
        assertThat(page.locator(item360Page.productName)).isVisible();

    }
}
