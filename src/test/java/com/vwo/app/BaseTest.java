package com.vwo.app;

import com.microsoft.playwright.*;
import com.vwo.utilities.ConfigManager;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    private final String AUTH_STATE_PATH = "./src/main/resources/storageSession.json";
    protected ConfigManager configManager;

    @BeforeSuite
    public void beforeSuite() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeTest
    public void beforeTest() {
        BrowserContext tempContext = browser.newContext();
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
        context = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get(AUTH_STATE_PATH)));
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

    private void login() {
        LoginPage loginPage = new LoginPage(page);
        DashboardPage dashboardPage = loginPage.login(configManager.getProperty("app.username"),
                configManager.getProperty("app.password"));
        assertThat(page.locator(dashboardPage.needHelpBtn)).isVisible();
        assertThat(page.locator(dashboardPage.userMenuBtn)).isVisible();
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

}
