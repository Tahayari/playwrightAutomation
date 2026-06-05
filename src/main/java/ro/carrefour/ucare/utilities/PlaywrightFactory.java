package ro.carrefour.ucare.utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Geolocation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class PlaywrightFactory {
  private static Playwright playwright;
  private static Browser browser;

  /** Utility class — no instantiation. */
  private PlaywrightFactory() {}

  /**
   * Initializes the core Playwright and Browser instances once per suite. Safe to call multiple
   * times; subsequent calls are no-ops if already initialized.
   */
  public static void initBrowser() {
    if (playwright == null) {
      playwright = Playwright.create();
    }
    if (browser == null || !browser.isConnected()) {
      browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }
  }

  /**
   * Creates a fresh, fully isolated mobile BrowserContext.
   *
   * @param authStatePath Path to the storage-state JSON file; pass {@code null} to start with a
   *     clean session.
   * @param timeout Default navigation / action timeout in milliseconds.
   * @return A configured, ready-to-use BrowserContext.
   */
  public static BrowserContext createMobileContext(String authStatePath, double timeout) {
    if (browser == null || !browser.isConnected()) {
      initBrowser();
    }
    Browser.NewContextOptions options =
        new Browser.NewContextOptions()
            .setPermissions(Collections.singletonList("geolocation"))
            .setGeolocation(new Geolocation(44.439, 26.0963))
            .setUserAgent(
                "Mozilla/5.0 (Linux; Android 12; DT50_5G) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/105.0.0.0 Mobile Safari/537.36")
            .setViewportSize(360, 720)
            .setDeviceScaleFactor(2.0)
            .setIsMobile(true)
            .setHasTouch(true);

    if (authStatePath != null && Files.exists(Paths.get(authStatePath))) {
      options.setStorageStatePath(Paths.get(authStatePath));
    }

    BrowserContext context = browser.newContext(options);
    context.setDefaultTimeout(timeout);
    return context;
  }

  /**
   * Disposes the Browser and Playwright instances at the end of suite execution. Safe to call even
   * if {@link #initBrowser()} was never called.
   */
  public static void closeBrowser() {
    if (browser != null) {
      browser.close();
      browser = null;
    }
    if (playwright != null) {
      playwright.close();
      playwright = null;
    }
  }
}
