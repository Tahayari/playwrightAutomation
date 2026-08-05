# 🎭 Playwright UI Automation Framework

A mobile web automation testing framework built with **Playwright**, **TestNG**, and **Java**, designed for end-to-end testing of the uCare application across multiple country environments.

---

## 🛠️ Technologies & Libraries

| Technology | Version | Purpose |
|---|---|---|
| Java | 11+ | Core language |
| Playwright (Java) | 1.58.0 | Browser automation |
| TestNG | 7.11.0 | Test runner & lifecycle hooks |
| Maven | 3.9+ | Build & dependency management |
| Allure TestNG | 2.27.0 | Test reporting |
| AspectJ Weaver | 1.9.21 | Allure `@Step` instrumentation |
| Spotless (Google Java Format) | 3.6.0 | Auto code formatting on compile |

---

## ✅ Prerequisites

Before running tests, make sure you have the following installed:

- **Java JDK 11+** — verify with `java -version`
- **Maven 3.9+** — verify with `mvn -version`
- **Playwright browsers** — installed automatically on first `mvn test`. If needed manually:
  ```bash
  mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install chromium"
  ```

No separate WebDriver or browser drivers required — Playwright manages its own browser binaries.

---

## 📁 Project Structure

```
playwrightAutomation/
├── src/
│   ├── main/
│   │   ├── java/com/carrefour/ucare/
│   │   │   ├── BasePage.java              # Base page: shared locators, methods & assertions
│   │   │   ├── HomePage.java              # Home page locators, methods & assertions
│   │   │   ├── LoginPage.java             # Login page locators, methods & assertions
│   │   │   ├── Item360Page.java           # Item360 page locators, methods & assertions (_RO / _FR)
│   │   │   ├── me/
│   │   │   │   └── MePage.java            # Me page locators, methods & assertions
│   │   │   ├── stock/
│   │   │   │   └── StockPage.java         # Stock page locators, methods & assertions
│   │   │   └── utilities/
│   │   │       ├── ConfigManager.java     # Loads config/[env].properties (singleton)
│   │   │       ├── AuthStateManager.java  # Manages the session file (login-once logic)
│   │   │       ├── PlaywrightFactory.java # Creates BrowserContext with device profiles
│   │   │       ├── EvidenceManager.java   # Captures screenshots & traces on failure
│   │   │       ├── TestDataManager.java   # Loads testdata/[env].json; dot-notation key lookup
│   │   │       ├── RetryAnalyzer.java     # Retry logic for flaky tests
│   │   │       └── RetryListener.java     # TestNG listener that wires in RetryAnalyzer
│   │   └── resources/
│   │       ├── config/
│   │       │   ├── ro.properties          # Romania environment config (URL, credentials)
│   │       │   ├── fr.properties          # France environment config
│   │       │   └── be.properties          # Belgium environment config
│   │       ├── environment.properties     # Allure environment metadata (country, URL, device)
│   │       └── storageSession.json        # Saved browser session (auto-generated at runtime)
│   └── test/
│       ├── java/com/carrefour/ucare/e2e/
│       │   ├── romania/
│       │   │   ├── item360/
│       │   │   │   └── Item360Tests.java      # Item360 test cases (Romania)
│       │   │   ├── stock/
│       │   │   │   └── StockPageTests.java    # Stock page test cases
│       │   │   └── me/
│       │   │       └── MePageTests.java       # Me page test cases
│       │   ├── france/
│       │   │   └── item360/
│       │   │       └── Item360Tests.java      # Item360 test cases (France)
│       │   ├── BaseTest.java              # Test lifecycle (@Before/@After hooks, login)
│       │   ├── HomePageTests.java         # Home page test cases
│       │   └── LoginPageTests.java        # Login page test cases
│       └── resources/
│           ├── categories.json            # Categories used in Allure report
│           ├── testdata/
│           │   ├── ro.json                # Test data for Romania (product codes, EANs, etc.)
│           │   └── fr.json                # Test data for France
│           └── suites/
│               ├── sanity-romania.xml     # TestNG suite for Romania (parallel, thread-count=2)
│               └── sanity-france.xml      # TestNG suite for France (parallel, thread-count=2)
└── pom.xml
```

---

## 🏗️ POM Structure — Where Things Live

### Page Objects (`src/main/java/.../`)

Each page of the app has its own class (e.g. `StockPage.java`, `HomePage.java`). Inside each page object:

- **Locators** — defined as `private static final String` constants at the top of the class:
  ```java
  private static final String STOCK_PAGE_TITLE = "#stock-title";
  private static final String OOS_CARD = "//div[@data-testid='stock-carousel']/div[1]";
  ```

- **Methods** — encapsulate user interactions with the page (grouped under a `// ── Methods ──` comment):
  ```java
  public void navigateToStockPage() {
      page.locator(STOCK_FOOTER_MENU).click();
  }
  ```

- **Assertions** — verify expected page state (grouped under `// ── Assertions ──` comments). Country-specific assertions are suffixed with `_RO`, `_FR`, etc.:
  ```java
  public void assertItem360PageElements_RO() {
      assertThat(page.locator(PRODUCT_BRAND_ID)).isVisible();
      assertThat(page.locator(GO_TO_PRICE_AUDIT_ID)).isVisible();
      // ...
  }

  public void assertItem360PageElements_FR() {
      assertThat(page.locator(PRODUCT_BRAND_ID)).isVisible();
      assertThat(page.locator(RECOMMENDED_PRICE_ID)).isVisible();
      // ...
  }
  ```

`BasePage.java` holds locators and methods/assertions that are shared across all pages (e.g. footer navigation, search input).

### Test Classes (`src/test/java/.../e2e/`)

Test classes are organised by **country package** (`romania/`, `france/`) and then by **feature area** (`item360/`, `stock/`, `me/`). Each test class extends `BaseTest` and focuses on a single page/feature area.

- Write tests here — **not** in page objects
- Each `@Test` method should represent one independent scenario
- Use page object methods to interact with the app; use Allure `@Step` / `step()` to document what each step does
- Use `TestDataManager.get("key.path")` to supply environment-specific test data:
  ```java
  @Feature("Item360")
  public class Item360Tests extends BaseTest {
      @Test(groups = {"INT_ADM"})
      public void navigateTo_item360Page_test() {
          verifyHomePage();
          String productId = TestDataManager.get("item360.internalCode.id_1");
          searchProduct(productId);
          item360Page.assertItem360PageElements_FR();
      }
  }
  ```

### Where to Add Tests for a New Country (e.g. Belgium)

1. **Config** — add/update `src/main/resources/config/be.properties` with the correct URL and credentials
2. **Test data** — create `src/test/resources/testdata/be.json` with Belgium-specific product codes, EANs, etc.
3. **Page object assertions** — add a country-specific assertion method suffixed `_BE` in the relevant page class
4. **Test classes** — create the `belgium/<feature>/` package under `src/test/java/.../e2e/` and add the test class
5. **Suite XML** — create/update `src/test/resources/suites/sanity-belgium.xml`
6. Run with `-Denv=be -Dsuite=src/test/resources/suites/sanity-belgium.xml`

---

## 🗂️ Test Data Management

Test data is **decoupled from test code** and stored as JSON files per environment under `src/test/resources/testdata/`.

| File | Environment |
|---|---|
| `ro.json` | Romania |
| `fr.json` | France |

`TestDataManager` loads the correct file automatically based on the `-Denv` system property (defaults to `ro`) and exposes values via dot-notation key lookup:

```java
// Reads: testdata/fr.json → item360 → internalCode → id_1
String productId = TestDataManager.get("item360.internalCode.id_1");
```

Example `ro.json` structure:
```json
{
  "countryCode": "RO",
  "item360": {
    "internalCode": { "id_1": "10005000", "id_2": "10005001" },
    "ean":          { "id_1": "1234567890123", "id_2": "1234567890124" }
  }
}
```

---

## 🚀 Running Tests via Maven

### Default (Romania, sanity suite)
```bash
mvn clean test
```
This runs `sanity-romania.xml` with `env=ro` as defined by the defaults in `pom.xml`.

### Specify a country / suite
```bash
# Romania
mvn clean test -Denv=ro -Dsuite=src/test/resources/suites/sanity-romania.xml

# France
mvn clean test -Denv=fr -Dsuite=src/test/resources/suites/sanity-france.xml

# Belgium
mvn clean test -Denv=be -Dsuite=src/test/resources/suites/sanity-belgium.xml
```

### Override credentials locally (without editing any file)
`ConfigManager` always prefers system properties over the values in the `.properties` file, so you can pass credentials on the command line:

```bash
mvn clean test -Denv=ro -Dapp.username=myuser@example.com -Dapp.password=mypassword
```

### Run a specific test class
```bash
mvn clean test -Dtest=StockPageTests
```

### Run in headless mode (CI/CD)
By default the browser launches in **headed mode** (`headless=false`). Pass `-Dbrowser.headless=true` to suppress the UI for CI pipelines:
```bash
mvn clean test -Dbrowser.headless=true
```

### Generate and open the Allure report
```bash
mvn allure:report       # generates HTML into target/site/allure-maven-plugin/
mvn allure:serve        # builds and opens in browser
```

---

## 🔐 Login Flow & Session Management

Login is performed **once per suite run**, not before every test.

**How it works:**

1. Before the first test method runs, `AuthStateManager.ensureAuthState()` checks whether a valid session file (`src/main/resources/storageSession.json`) already exists.
2. If it doesn't, a temporary browser context is created, a full login is performed against the app, and Playwright's storage state (cookies + local storage) is serialised to `storageSession.json`.
3. All subsequent test contexts load that saved session file — no login UI interaction needed.
4. At the end of the suite (`@AfterSuite`), the session file is reset to `{}` so the next run always starts fresh and avoids stale/expired sessions.

This is thread-safe: if tests run in parallel, only the first thread performs the login; all others wait and then load the already-saved file.

```
Suite starts
  └─ @BeforeSuite  → sets global assertion timeout (20 s)

<test> tag starts (one per thread)
  └─ @BeforeTest   → initialises Playwright + Chromium browser for this thread

  @BeforeMethod fires for each test
        ├─ First call  → login via UI → save storageSession.json
        └─ All others  → load storageSession.json (no UI login)
        └─ Creates isolated BrowserContext + starts Playwright trace

  @AfterMethod fires after each test
        ├─ FAILURE  → captures screenshot & saves trace .zip to target/evidence/traces/
        └─ PASS     → discards trace (frees browser resources)

<test> tag ends
  └─ @AfterTest    → closes Browser + Playwright for this thread

Suite ends
  └─ @AfterSuite   → resets storageSession.json to {}
```

---

## ⚡ Parallel Execution

Both the Romania and France suites run `<test>` blocks **in parallel** with a thread count of 2:

```xml
<suite name="Romania_Smoke_TestSuite" parallel="tests" thread-count="2">
```

Each `<test>` block (e.g. `Homepage_Tests`, `Item360_Tests`) runs in its own thread with its own independent Playwright instance and browser, managed via `ThreadLocal` in `PlaywrightFactory`. Test classes within the same `<test>` block run sequentially.

---

## 📐 Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Page object classes | `PascalCase` + `Page` suffix | `StockPage`, `HomePage` |
| Test classes | `PascalCase` + `Tests` suffix | `StockPageTests`, `HomePageTests` |
| Locators | `UPPER_SNAKE_CASE` private static final | `STOCK_PAGE_TITLE`, `OOS_CARD` |
| Methods | `camelCase`, descriptive verb | `navigateToStockPage()`, `clickHomeMenu()` |
| Test methods | `camelCase`, descriptive | `verifyStockPageIsDisplayed()` |
| Country-specific assertions | method name + `_RO` / `_FR` / `_BE` suffix | `assertItem360PageElements_FR()` |
| Test data keys | dot-notation, `section.sub.key` | `item360.internalCode.id_1` |
| Config properties | `noun.noun` lowercase | `app.url`, `app.username` |

---

## 📱 Device Profiles

Tests run in a simulated mobile browser with **geolocation pre-configured** (Bucharest, Romania by default). The device is selected via `-Ddevice=<name>` (defaults to `Desktop` which uses a 360×720 viewport). Available profiles defined in `PlaywrightFactory`:

| `-Ddevice` value | Viewport | User Agent | Geolocation |
|---|---|---|---|
| `Desktop` (default) | 360×720 | Default Chromium | 44.439°N, 26.096°E |
| `iPhone 14` | 390×844 | Safari iOS 16 | 44.439°N, 26.096°E |
| `Pixel 7` | 412×915 | Chrome Android 13 | 44.439°N, 26.096°E |
| `Urovo` | 360×720 | Chrome Android 12 (DT50_5G) | 44.439°N, 26.096°E |

Example:
```bash
mvn clean test -Ddevice=Urovo
```

All profiles also set `isMobile=true`, `hasTouch=true`, and grant the `geolocation` browser permission automatically.

---

## 🐛 Failure Evidence

On test failure, `EvidenceManager` automatically:
- Attaches a **full-page screenshot** to the Allure report (in-memory, no disk write)
- Saves a **Playwright trace** (`.zip`) to `target/evidence/traces/` and attaches a plain-text path note to the Allure report

Tracing is started at `@BeforeMethod` with **screenshots**, **DOM snapshots**, and **source files** captured, giving a complete step-by-step replay on failure.

Traces can be opened at [trace.playwright.dev](https://trace.playwright.dev) for step-by-step visual replay.

On test **pass**, the open trace is discarded cleanly via `EvidenceManager.discardTrace()` to free browser resources.

---

## 📊 Allure Environment Metadata

The `environment.properties` file (in `src/main/resources/`) is automatically copied into `target/allure-results/` at build time by the `maven-resources-plugin`. It surfaces the following details on the Allure report's **Environment** widget:

| Key | Value |
|---|---|
| `Country` | Active `-Denv` value (e.g. `ro`, `fr`) |
| `URL` | Active `app.url` property |
| `Username` | Active `app.username` property |
| `Device` | Active `-Ddevice` value |

---

**Maintained By:** Quality Assurance Team