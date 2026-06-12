package ro.carrefour.ucare.utilities;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EvidenceManager {

  private static final String TRACE_DIR = "target/evidence/traces";

  private final Page page;
  private final BrowserContext context;

  public EvidenceManager(Page page, BrowserContext context) {
    this.page = page;
    this.context = context;
  }

  /**
   * Called on test failure. Attaches a full-page screenshot directly to the Allure report
   * (in-memory, no disk write) and saves the Playwright trace to disk with a link note in Allure.
   */
  public void captureEvidence(String testId) {
    attachScreenshotToAllure();
    saveTrace(testId);
  }

  /**
   * Called on test pass. Stops the trace recording without persisting any file. Must always be
   * called — an open trace leaks resources inside the browser process.
   */
  public void discardTrace() {
    if (context == null) return;
    try {
      context.tracing().stop();
    } catch (Exception ignored) {
    }
  }

  /**
   * Takes a full-page screenshot as raw bytes and attaches them directly to the current Allure test
   * case. Thread-safe: Allure.addAttachment() resolves the target test by the calling thread's
   * UUID.
   */
  private void attachScreenshotToAllure() {
    if (page == null) return;
    try {
      byte[] bytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
      Allure.addAttachment(
          "Screenshot on Failure", "image/png", new ByteArrayInputStream(bytes), ".png");
    } catch (Exception e) {
      System.err.println("Warning: screenshot attachment failed — " + e.getMessage());
    }
  }

  /**
   * Stops the Playwright trace and writes it to disk. A plain-text note is also attached to the
   * Allure report so you can find and open the trace file without hunting through the filesystem.
   * Open traces at: <a href="https://trace.playwright.dev">...</a>
   */
  private void saveTrace(String testId) {
    if (context == null) return;
    try {
      Path dir = Paths.get(TRACE_DIR);
      Path tracePath = dir.resolve(testId + ".zip");
      Files.createDirectories(dir);

      context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

      String note =
          "Playwright trace saved to:\n"
              + tracePath.toAbsolutePath()
              + "\n\nOpen at: https://trace.playwright.dev";
      Allure.addAttachment(
          "Playwright Trace Location",
          "text/plain",
          new ByteArrayInputStream(note.getBytes()),
          ".txt");
    } catch (Exception e) {
      System.err.println("Warning: trace save failed — " + e.getMessage());
    }
  }
}
