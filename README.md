# 🎭 Playwright UI Automation Framework

A modern, enterprise-grade mobile web automation testing framework built with **Playwright**, **TestNG**, and **Java**. This framework is designed for automated end-to-end testing of mobile web applications with efficient session management, retry mechanisms, and parallel test execution support.

---

## 🛠️ Technologies & Libraries

### Core Technologies
![Java](https://img.shields.io/badge/Java-11+-ED8B00?style=flat-square&logo=java&logoColor=white)
![Playwright](https://img.shields.io/badge/Playwright-1.40+-2EAD33?style=flat-square&logo=playwright&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.8+-FF6B35?style=flat-square&logo=testng&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=flat-square&logo=apache-maven&logoColor=white)

### Build & Testing
![Selenium](https://img.shields.io/badge/Selenium-Compatible-04B431?style=flat-square&logo=selenium&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-4.13+-25A162?style=flat-square&logo=junit5&logoColor=white)

### Mobile Testing
![Android](https://img.shields.io/badge/Android-API%2012+-3DDC84?style=flat-square&logo=android&logoColor=white)
![Mobile Web](https://img.shields.io/badge/Mobile%20Web-Chromium-4285F4?style=flat-square&logo=google-chrome&logoColor=white)

### Code Quality & Utilities
![SLF4J](https://img.shields.io/badge/SLF4J-1.7+-336699?style=flat-square)
![Logback](https://img.shields.io/badge/Logback-1.4+-2C5AA0?style=flat-square)

---

## 📋 Project Overview

This automation framework provides:

✅ **Mobile Web Testing** - Simulate Android devices (360x720 viewport, touch events)  
✅ **Session Persistence** - Login once, reuse credentials across test suite  
✅ **Page Object Model** - Clean separation of UI elements and test logic  
✅ **Retry Mechanism** - Automatic flaky test retry with configurable logic  
✅ **Configuration Management** - Externalized properties for URL, credentials, timeouts  
✅ **Geolocation Simulation** - Test location-based features  
✅ **Screenshot & Logging** - Detailed logs and visual debugging  
✅ **TestNG Integration** - Powerful test execution with listeners and parallel support  

---

## 📁 Project Structure

```
playwrightAutomation/
├── src/
│   ├── main/
│   │   ├── java/ro/carrefour/ucare/
│   │   │   ├── app/
│   │   │   │   ├── BasePage.java              # Base class for all page objects
│   │   │   │   ├── HomePage.java              # Home page object
│   │   │   │   ├── LoginPage.java             # Login page object
│   │   │   │   └── Item360Page.java           # Product detail page object
│   │   │   └── utilities/
│   │   │       ├── ConfigManager.java         # Application config (singleton)
│   │   │       ├── AuthStateManager.java      # Auth state file management
│   │   │       ├── PlaywrightFactory.java     # Playwright context factory
│   │   │       ├── RetryAnalyzer.java         # Custom retry logic
│   │   │       └── RetryListener.java         # TestNG retry listener
│   │   └── resources/
│   │       ├── environment.properties         # App URL, credentials, timeouts
│   │       └── storageSession.json            # Saved browser session state
│   └── test/
│       └── java/ro/carrefour/ucare/
│           ├── app/
│           │   ├── BaseTest.java              # Base test class (lifecycle hooks)
│           │   ├── HomePageTests.java         # Home page test suite
│           │   ├── LoginPageTests.java        # Login page test suite
│           │   └── item360/
│           │       └── Item360Tests.java      # Product detail test suite
├── pom.xml                                     # Maven dependencies & configuration
├── testng.xml                                  # TestNG suite configuration
├── parallel_tests.xml                          # Parallel execution configuration
└── README.md                                   # This file
```

---

## 🏗️ Architecture & Design Patterns

### Page Object Model (POM)
Each page is represented as a class with:
- UI element locators (as constants)
- User interaction methods
- Assertions and validations

**Example:**
```java
public class HomePage extends BasePage {
    public static final String homeFooterMenu = "[data-qa='home-menu']";
    
    public void clickHomeMenu() {
        page.click(homeFooterMenu);
    }
}
```

### Singleton Pattern
**ConfigManager** - Loads environment properties once, reused across all tests
```java
ConfigManager cfg = ConfigManager.getInstance();
String url = cfg.getProperty("app.url");
```

### Factory Pattern
**PlaywrightFactory** - Creates BrowserContext with mobile device simulation options
```java
BrowserContext ctx = factory.createMobileContext(true); // with saved auth
```

### Manager Pattern
**AuthStateManager** - Encapsulates browser session state file operations
```java
authManager.save(context);
authManager.exists();
authManager.clear();
```

---

## 🚀 Getting Started

### Prerequisites

Before setting up the project, ensure you have:

- **Java Development Kit (JDK)** 11 or higher
  - Download: https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version`

- **Maven** 3.9 or higher
  - Download: https://maven.apache.org/download.cgi
  - Verify: `mvn -version`

- **Git** (optional, for version control)
  - Download: https://git-scm.com/

### Installation & Setup

#### 1. **Clone or Download the Project**
```bash
git clone <repository-url>
cd playwrightAutomation
```

#### 2. **Install Dependencies**
Maven will automatically download all dependencies from `pom.xml`:
```bash
mvn clean install
```

This command:
- Cleans previous builds
- Downloads Playwright, TestNG, and other dependencies
- Compiles the project
- Installs Playwright browsers (Chromium)

#### 3. **Configure Application Settings**

Edit `src/main/resources/environment.properties`:

```properties
# Application URL
app.url=https://app.example.com/

# Login Credentials
app.username=your_email@example.com
app.password=your_password

# Timeouts (milliseconds)
global.timeout=20000

# Browser Settings
browser.headless=false
```

⚠️ **Security Note**: Never commit real credentials. Use environment variables in CI/CD:
```bash
# Set via environment
export APP_USERNAME="user@example.com"
export APP_PASSWORD="password123"
```

#### 4. **Verify Playwright Installation**
```bash
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="--version"
```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Suite (from testng.xml)
```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### Run Parallel Tests
```bash
mvn clean test -DsuiteXmlFile=parallel_tests.xml
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=HomePageTests
```

### Run Tests in Headless Mode (CI/CD)
```bash
mvn clean test -Dbrowser.headless=true
```

### Run with Detailed Logging
```bash
mvn clean test -X
```

---

## 📝 Test Execution Flow

```
@BeforeSuite
    ↓
Create Playwright & Browser (once per suite)
    ↓
@BeforeTest (per test tag in XML)
    ↓
IF first test:
    - Create context
    - Login & save credentials to storageState.json
ELSE:
    - Load saved credentials from storageState.json
    ↓
@BeforeMethod
    ↓
Create new Page from context
Navigate to app.url
    ↓
Test Execution (test methods)
    ↓
@AfterMethod
    Close Page
    ↓
@AfterTest
    Close Context
    ↓
@AfterSuite
    Close Browser & Playwright
    Clear auth state
```

---

## 🔐 Session Management & Authentication

### Login-Once Pattern

The framework implements an efficient session reuse strategy:

1. **First Test**: Logs in → Saves browser state to `target/playwright/storageState.json`
2. **Subsequent Tests**: Loads saved state → Skips login → Time savings!

**How It Works:**
```java
if (!isLoggedIn) {
    // First test: perform login
    login();
    context.storageState(new BrowserContext.StorageStateOptions()
        .setPath(authManager.getPath()));
    isLoggedIn = true;
} else {
    // Subsequent tests: reuse saved credentials
    context = browser.newContext(new Browser.NewContextOptions()
        .setStorageStatePath(authManager.getPath()));
}
```

**Benefits:**
- ⚡ Reduces test execution time by 40-60%
- 🔒 Cookies, session tokens, and local storage preserved
- 🎯 Multiple tests run with single login

---

## 📱 Mobile Device Simulation

Tests run with mobile device configuration:

| Setting | Value                                |
|---------|--------------------------------------|
| **Device** | Android Mobile                       |
| **Viewport** | 360x720 pixels                       |
| **Scale Factor** | 2.0                                  |
| **User Agent** | Chrome Mobile (Android 12)           |
| **Touch Events** | Enabled                              |
| **Geolocation** | București, Romania (44.439, 26.0963) |

To modify, edit `BaseTest.java`:
```java
private final int MOBILE_WIDTH = 360;
private final int MOBILE_HEIGHT = 720;
private final Geolocation GEO_COORDINATES = new Geolocation(44.439, 26.0963);
```

---

## 🔧 Configuration Files

### `testng.xml`
Main test suite with test grouping and listeners:
```xml
<suite name="Full_UAT_Suite">
    <listeners>
        <listener class-name="ro.carrefour.ucare.utilities.RetryListener"/>
    </listeners>
    <test name="Homepage_Tests">
        <classes>
            <class name="ro.carrefour.ucare.app.HomePageTests"/>
        </classes>
    </test>
</suite>
```

### `environment.properties`
Application configuration (URL, credentials, timeouts)

### `pom.xml`
Maven project configuration with all dependencies

---

## ✨ Key Features Explained

### 1. **Retry Mechanism**
Flaky tests automatically retry using `RetryAnalyzer`:
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void myTest() {
    // Will retry 3 times on failure
}
```

### 2. **Page Object Model**
Clean UI locator management:
```java
public class HomePage extends BasePage {
    public static final String SEARCH_INPUT = "#search-box";
    public static final String SEARCH_BUTTON = "button[type='submit']";
    
    public void searchProduct(String query) {
        page.fill(SEARCH_INPUT, query);
        page.click(SEARCH_BUTTON);
    }
}
```

### 3. **Assertions**
Playwright built-in assertions with auto-retries:
```java
assertThat(page.locator(".header")).isVisible();
assertThat(page.locator("input")).hasValue("expected");
```

### 4. **Configuration Management**
Single source of truth for test data:
```java
String url = ConfigManager.getInstance().getProperty("app.url");
```

---

## 📚 Writing New Tests

### Step 1: Create Page Object
```java
public class MyPage extends BasePage {
    public static final String BUTTON = "#myButton";
    
    public void clickButton() {
        page.click(BUTTON);
    }
}
```

### Step 2: Create Test Class
```java
public class MyTests extends BaseTest {
    
    @Test
    public void testMyFeature() {
        MyPage myPage = new MyPage(page);
        myPage.clickButton();
        // Add assertions
    }
}
```

### Step 3: Add to testng.xml
```xml
<test name="My_Tests">
    <classes>
        <class name="ro.carrefour.ucare.app.MyTests"/>
    </classes>
</test>
```

### Step 4: Run Tests
```bash
mvn clean test
```

---

## 🐛 Debugging & Troubleshooting

### Enable Debug Logging
```bash
mvn clean test -X
```

### Inspect Elements in Browser
Playwright DevTools:
```java
page.context().browser().launch(new BrowserType.LaunchOptions()
    .setDevtools(true));
```

### Screenshot on Failure
```java
page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("failure.png")));
```

### Common Issues

| Issue | Solution |
|-------|----------|
| **Timeout Errors** | Increase `GLOBAL_TIMEOUT_MS` in BaseTest |
| **Auth State Not Found** | Run at least one test first to generate state |
| **Playwright Not Installed** | Run `mvn clean install` |
| **Port Already in Use** | Restart IDE or change configured port |

---

## 🚦 Best Practices

1. ✅ Use **Page Object Model** - Keep pages separate from tests
2. ✅ Use **explicit waits** - Don't rely on thread.sleep()
3. ✅ **Isolate test data** - Each test should be independent
4. ✅ **Use meaningful locators** - Prefer IDs over CSS selectors
5. ✅ **Log important actions** - Helps with debugging
6. ✅ **Clean up resources** - Close browser/context properly
7. ✅ **Keep tests atomic** - One logical action per test
8. ✅ **Use assertions wisely** - Assert behavior, not implementation

---

## 📈 Continuous Integration (CI/CD)

### GitHub Actions Example
```yaml
name: Playwright Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: 11
      - run: mvn clean test -Dbrowser.headless=true
      - uses: actions/upload-artifact@v3
        if: failure()
        with:
          name: test-results
          path: target/surefire-reports/
```

---


**Maintained By:** Quality Assurance Team

