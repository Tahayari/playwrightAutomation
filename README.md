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
│   │   │   ├── Item360Page.java           # Item360 page locators, methods & assertions
│   │   │   ├── me/
│   │   │   │   └── MePage.java            # Me page locators, methods & assertions
│   │   │   ├── stock/
│   │   │   │   └── StockPage.java         # Stock page locators, methods & assertions
│   │   │   └── utilities/
│   │   │       ├── ConfigManager.java     # Loads config/[env].properties (singleton)
│   │   │       ├── AuthStateManager.java  # Manages the session file (login-once logic)
│   │   │       ├── PlaywrightFactory.java # Creates BrowserContext with device profiles
│   │   │       ├── EvidenceManager.java   # Captures screenshots & traces on failure
│   │   │       ├── RetryAnalyzer.java     # Retry logic for flaky tests
│   │   │       └── RetryListener.java     # TestNG listener that wires in RetryAnalyzer
│   │   └── resources/
│   │       ├── config/
│   │       │   ├── ro.properties          # Romania environment config (URL, credentials)
│   │       │   ├── fr.properties          # France environment config
│   │       │   └── be.properties          # Belgium environment config
│   │       └── storageSession.json        # Saved browser session (auto-generated at runtime)
│   └── test/
│       ├── java/ro/carrefour/ucare/e2e/
│       │   ├── BaseTest.java              # Test lifecycle (@Before/@After hooks, login)
│       │   ├── HomePageTests.java         # Home page test cases
│       │   ├── LoginPageTests.java        # Login page test cases
│       │   ├── item360/
│       │   │   └── Item360Tests.java      # Item360 test cases
│       │   ├── stock/
│       │   │   └── StockPageTests.java    # Stock page test cases
│       │   └── me/
│       │       └── MePageTests.java       # Me page test cases
│       └── resources/
│           └── suites/
│               ├── sanity-romania.xml     # TestNG suite for Romania
│               └── sanity-france.xml      # TestNG suite for France
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
  public void assertStockPageMainElementsAreDisplayed_RO() {
      assertThat(page.locator(OOS_CARD)).isVisible();
      assertThat(page.locator(REGULAR_ORDER_OPTION)).isVisible();
      // ...
  }
  ```

`BasePage.java` holds locators and methods/assertions that are shared across all pages (e.g. footer navigation, search input).

### Test Classes (`src/test/java/.../e2e/`)

Test classes contain the actual `@Test` methods. Each test class extends `BaseTest` and focuses on a single page/feature area.

- Write tests here — **not** in page objects
- Each `@Test` method should represent one independent scenario
- Use page object methods to interact with the app; use Allure `step()` to document what each step does:
  ```java
  public class StockPageTests extends BaseTest {
      @Test
      public void verifyStockPageIsDisplayed() {
          verifyHomePage();
          navigateToStockPage();
      }
  }
  ```

### Where to Add Tests for a New Country (e.g. France)

1. **Config** — add/update `src/main/resources/config/fr.properties` with the correct URL and credentials
2. **Page object assertions** — add a country-specific assertion method suffixed `_FR` in the relevant page class
3. **Suite XML** — add/uncomment the test classes in `src/test/resources/suites/sanity-france.xml`
4. Run with `-Denv=fr -Dsuite=src/test/resources/suites/sanity-france.xml`

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
mvn clean test -Denv=be -Dsuite=src/test/resources/suites/sanity-romania.xml
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
  └─ @BeforeMethod fires for each test
        ├─ First call  → login via UI → save storageSession.json
        └─ All others  → load storageSession.json (no UI login)

Suite ends
  └─ @AfterSuite → reset storageSession.json to {}
```

---

## 📐 Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Page object classes | `PascalCase` + `Page` suffix | `StockPage`, `HomePage` |
| Test classes | `PascalCase` + `Tests` suffix | `StockPageTests`, `HomePageTests` |
| Locators | `UPPER_SNAKE_CASE` private static final | `STOCK_PAGE_TITLE`, `OOS_CARD` |
| Methods | `camelCase`, descriptive verb | `navigateToStockPage()`, `clickHomeMenu()` |
| Test methods | `camelCase`, descriptive | `verifyStockPageIsDisplayed()` |
| Country-specific assertions | method name + `_RO` / `_FR` / `_BE` suffix | `assertStockPageMainElementsAreDisplayed_RO()` |
| Config properties | `noun.noun` lowercase | `app.url`, `app.username` |

---

## 📱 Device Profiles

Tests run in a simulated mobile browser. The device is selected via `-Ddevice=<name>` (defaults to `Desktop` which uses a 360×720 viewport). Available profiles defined in `PlaywrightFactory`:

| `-Ddevice` value | Viewport | User Agent |
|---|---|---|
| `Desktop` (default) | 360×720 | Default Chromium |
| `iPhone 14` | 390×844 | Safari iOS 16 |
| `Pixel 7` | 412×915 | Chrome Android 13 |
| `Urovo` | 360×720 | Chrome Android 12 (DT50_5G) |

Example:
```bash
mvn clean test -Ddevice=Urovo
```

---

## 🐛 Failure Evidence

On test failure, `EvidenceManager` automatically:
- Attaches a **full-page screenshot** to the Allure report
- Saves a **Playwright trace** (`.zip`) to `target/evidence/traces/`

Traces can be opened at [trace.playwright.dev](https://trace.playwright.dev) for step-by-step visual replay.

---

**Maintained By:** Quality Assurance Team