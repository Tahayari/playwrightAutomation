package ro.carrefour.ucare.utilities;

import java.util.logging.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener {
  private static final Logger logger = Logger.getLogger(ScreenshotListener.class.getName());

  @Override
  public void onTestFailure(ITestResult result) {
    Object testInstance = result.getInstance();
    String testName = result.getMethod().getMethodName();

    try {
      EvidenceManager evidenceManager = getEvidenceManagerFromTestInstance(testInstance);

      if (evidenceManager != null) {
        // Capture screenshot + trace on failure
        evidenceManager.captureEvidence(testName);
        logger.info("Evidence captured for failed test: " + testName);
      } else {
        logger.warning("EvidenceManager not found in test instance.");
      }
    } catch (Exception e) {
      logger.warning("Failed to capture evidence: " + e.getMessage());
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    Object testInstance = result.getInstance();
    String testName = result.getMethod().getMethodName();

    try {
      EvidenceManager evidenceManager = getEvidenceManagerFromTestInstance(testInstance);

      if (evidenceManager != null) {
        // Discard trace on success (no need to save)
        evidenceManager.discardTrace();
        logger.info("Trace discarded for passed test: " + testName);
      }
    } catch (Exception e) {
      logger.warning("Failed to discard trace: " + e.getMessage());
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {}

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

  @Override
  public void onTestFailedWithTimeout(ITestResult result) {
    onTestFailure(result);
  }

  @Override
  public void onTestStart(ITestResult result) {}

  /** * Reflection helper to get EvidenceManager from test instance */
  protected EvidenceManager getEvidenceManagerFromTestInstance(Object testInstance) {
    try {
      java.lang.reflect.Field evidenceManagerField =
          testInstance.getClass().getDeclaredField("evidenceManager");
      evidenceManagerField.setAccessible(true);
      return (EvidenceManager) evidenceManagerField.get(testInstance);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      logger.warning(
          "Could not access 'evidenceManager' field from test instance: " + e.getMessage());
      return null;
    }
  }
}
