package ro.carrefour.ucare.utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Geolocation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlaywrightFactory {
  private static final ThreadLocal<Playwright> playwrightTL = new ThreadLocal<>();
  private static final ThreadLocal<Browser> browserTL = new ThreadLocal<>();

  /** Utility class — no instantiation. */
  private PlaywrightFactory() {}

  /**
   * Initializes the core Playwright and Browser instances once per suite. Safe to call multiple
   * times; subsequent calls are no-ops if already initialized.
   */
  public static void initBrowser() {
    if (playwrightTL.get() == null) {
      playwrightTL.set(Playwright.create());
    }
    if (browserTL.get() == null || !browserTL.get().isConnected()) {
      browserTL.set(
          playwrightTL.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
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
    if (browserTL.get() == null || !browserTL.get().isConnected()) {
      initBrowser();
    }

    String deviceName = System.getProperty("device", "Desktop");

    Browser.NewContextOptions options;

    if (DEVICE_PROFILES.containsKey(deviceName)) {
      options = DEVICE_PROFILES.get(deviceName);
      System.out.println("Running emulation for device: " + deviceName);
    } else {
      // 4. Fallback: Standard Desktop defaults
      options = new Browser.NewContextOptions()
              .setViewportSize(360, 720)
              .setGeolocation(geolocation.latitude, geolocation.longitude)
              .setDeviceScaleFactor(2.0)
              .setIsMobile(true);
      System.out.println("Running with default Desktop viewport.");
    }

    if (authStatePath != null && Files.exists(Paths.get(authStatePath))) {
      options.setStorageStatePath(Paths.get(authStatePath));
    }

    BrowserContext context = browserTL.get().newContext(options);
    context.setDefaultTimeout(timeout);
    return context;
  }

  /**
   * Disposes the Browser and Playwright instances at the end of suite execution. Safe to call even
   * if {@link #initBrowser()} was never called.
   */
  public static void closeBrowser() {
    Browser browser = browserTL.get();
    if (browser != null) {
      browser.close();
      browserTL.remove();
    }
    Playwright pw = playwrightTL.get();
    if (pw != null) {
      pw.close();
      playwrightTL.remove();
    }
  }

  private static final Map<String, Browser.NewContextOptions> DEVICE_PROFILES = new HashMap<>();

  private static final Geolocation geolocation = new Geolocation(44.439, 26.0963);

  static {
    // Define iPhone 14
    DEVICE_PROFILES.put(
        "iPhone 14",
        new Browser.NewContextOptions()
            .setPermissions(Collections.singletonList("geolocation"))
            .setGeolocation(geolocation.latitude, geolocation.longitude)
            .setUserAgent(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1")
            .setViewportSize(390, 844)
            .setDeviceScaleFactor(3.0)
            .setIsMobile(true)
            .setHasTouch(true));

    // Define Android (Pixel 7)
    DEVICE_PROFILES.put(
        "Pixel 7",
        new Browser.NewContextOptions()
            .setPermissions(Collections.singletonList("geolocation"))
            .setGeolocation(geolocation.latitude, geolocation.longitude)
            .setUserAgent(
                "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36")
            .setViewportSize(412, 915)
            .setDeviceScaleFactor(2.625)
            .setIsMobile(true)
            .setHasTouch(true));

    // Define Android (UROVO DT50)
    DEVICE_PROFILES.put(
        "Urovo",
        new Browser.NewContextOptions()
            .setPermissions(Collections.singletonList("geolocation"))
            .setGeolocation(geolocation.latitude, geolocation.longitude)
            .setUserAgent(
                "Mozilla/5.0 (Linux; Android 12; DT50_5G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Mobile Safari/537.36")
            .setViewportSize(360, 720)
            .setDeviceScaleFactor(2.0)
            .setIsMobile(true)
            .setHasTouch(true));
  }
}
