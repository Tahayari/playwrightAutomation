package ro.carrefour.ucare.utilities;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EvidenceManager {

  private static final String SCREENSHOT_DIR = "target/evidence/screenshots";
  private static final String TRACE_DIR = "target/evidence/traces";

  private final Page page;
  private final BrowserContext context;

  public EvidenceManager(Page page, BrowserContext context) {
    this.page = page;
    this.context = context;
  }

  /**
   * Captures both a full-page screenshot and the Playwright trace for a failed test. Both
   * operations are non-fatal — a capture failure never masks the original test failure.
   */
  public void captureEvidence(String testId) {
    captureScreenshot(testId);
    saveTrace(testId);
  }

  /**
   * Stops tracing without writing any file — called when the test passes. Must always be called;
   * leaving an open trace leaks resources.
   */
  public void discardTrace() {
    if (context == null) return;
    try {
      context.tracing().stop();
    } catch (Exception ignored) {
    }
  }

  private void captureScreenshot(String testId) {
    if (page == null) return;
    try {
      Path dir = Paths.get(SCREENSHOT_DIR);
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
      Path dir = Paths.get(TRACE_DIR);
      Files.createDirectories(dir);
      context.tracing().stop(new Tracing.StopOptions().setPath(dir.resolve(testId + ".zip")));
    } catch (Exception e) {
      System.err.println("Warning: trace save failed — " + e.getMessage());
    }
  }
}
