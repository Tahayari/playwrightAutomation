package ro.carrefour.ucare.app.me;

import com.microsoft.playwright.Page;
import ro.carrefour.ucare.app.BasePage;

public class MePage extends BasePage {
  private final Page page;

  public String mePageTitle = "#me-title";
  public String daysOffRequestsCard = "";
  public String planningVisualizationOption = "#planning";
  public String daysOffOption = "#days-off";
  public String myContactsOption = "#contacts";

  public MePage(Page page) {
    this.page = page;
  }
}
