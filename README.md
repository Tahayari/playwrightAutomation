# Playwright Automation Testing Framework

A comprehensive test automation framework built with Playwright and Java for mobile web application testing. This project provides a robust foundation for end-to-end testing with features like session persistence, configuration management, and best-practice test organization.

## 📋 Project Overview

This project automates testing of Carrefour's u.Care Progressive Web App (Romania). It uses the Playwright browser automation library to simulate real user interactions on mobile devices, with intelligent session management to reuse authentication credentials across tests.

**Key Features:**
- 🔐 Smart login management (login once, reuse credentials across tests)
- 📱 Mobile device simulation (Android 12, custom viewport)
- 🗺️ Geolocation-aware testing
- ⚙️ Centralized configuration management
- 💾 Safe authentication state persistence
- 🛡️ Defensive cleanup and error handling
- 📊 Structured test organization with page object model

---

## 🛠️ Tech Stack

| Technology | Purpose | Version |
|-----------|---------|---------|
| ![Java](https://img.shields.io/badge/Java-11+-ED8B00?style=flat-square&logo=java&logoColor=white) | Programming Language | 11+ |
| ![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=flat-square&logo=apache-maven&logoColor=white) | Build Tool | 3.6+ |
| ![Playwright](https://img.shields.io/badge/Playwright-1.40+-2EAD33?style=flat-square&logo=microsoft&logoColor=white) | Browser Automation | 1.40+ |
| ![TestNG](https://img.shields.io/badge/TestNG-7.0+-F7931E?style=flat-square&logo=java&logoColor=white) | Test Framework | 7.0+ |

---

## 📁 Project Structure

```
playwrightAutomation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ro/carrefour/ucare/
│   │   │       ├── app/                    # Page Object Model classes
│   │   │       │   ├── BasePage.java
│   │   │       │   ├── HomePage.java
│   │   │       │   ├── LoginPage.java
│   │   │       │   └── Item360Page.java
│   │   │       └── utilities/              # Utility and helper classes
│   │   │           ├── ConfigManager.java
│   │   │           └── AuthStateManager.java
│   │   └── resources/                      # Configuration files
│   │       └── environment.properties
│   │
│   └── test/
│       └── java/
│           └── ro/carrefour/ucare/app/     # Test classes
│               ├── BaseTest.java           # Base class for all tests
│               ├── DashboardTests.java
│               ├── LoginPageTests.java
│               └── item360/
│                   └── Item360Tests.java
│
├── pom.xml                                 # Maven configuration
├── testng.xml                              # TestNG suite configuration
└── README.md                               # This file
```

---

## 🏗️ Architecture

### Page Object Model (POM)
The project follows the **Page Object Model** design pattern, where each page in the application has a corresponding class:
- Each page class extends `BasePage` for common functionality
- Page elements are defined as locators (selectors)
- Business logic methods encapsulate user interactions

### Base Test Class
`BaseTest.java` provides:
- Lifecycle management (`@BeforeSuite`, `@BeforeTest`, `@BeforeMethod`, etc.)
- Playwright and Browser initialization
- Mobile device configuration
- Authentication state management
- Common test utilities (e.g., `verifyHomePage()`, `searchProduct()`)

### Configuration Management
- `ConfigManager.java` - Singleton pattern for reading `environment.properties`
- `AuthStateManager.java` - Handles authentication state persistence in `target/playwright/`

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/TBD/playwrightAutomation.git
   cd playwrightAutomation
   ```

2. **Install Maven dependencies:**
   ```bash
   mvn clean install
   ```

### Configuration

Edit `src/main/resources/environment.properties`:
```properties
app.url=https://ucare-uat.tc.carrefour.ro/
app.username=your-username@email.com
app.password=your-password
```

---

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
mvn test -Dsuites=testng.xml
```

### Run Specific Test Class
```bash
mvn test -Dtest=DashboardTests
```

### Run Tests in Headless Mode
Update `BaseTest.java`:
```java
.setHeadless(true)  // Change from false to true
```

---

## 📊 Test Execution Flow

```
@BeforeSuite
└─ Initialize Playwright + Browser
└─ Initialize AuthStateManager
   │
   ├─ @BeforeTest (per test method)
   │  └─ Setup test-level resources
   │     │
   │     ├─ @BeforeMethod (first test)
   │     │  ├─ Create mobile context
   │     │  ├─ Navigate to URL
   │     │  ├─ Login & save credentials
   │     │  └─ Execute test logic
   │     │
   │     ├─ @BeforeMethod (subsequent tests)
   │     │  ├─ Create mobile context with saved credentials
   │     │  ├─ Navigate to URL
   │     │  └─ Execute test logic (skip login)
   │     │
   │     @AfterMethod
   │     └─ Close page
   │
   │  @AfterTest
   │  └─ Close context
   │
   @AfterSuite
   └─ Close browser
   └─ Close Playwright
   └─ Clear auth state file
```

---

## 🔐 Authentication & Session Management

### How It Works:

1. **First Test Execution:**
   - Fresh browser context created
   - User logs in with credentials from `environment.properties`
   - Session cookies/storage saved to `target/playwright/storageState.json`

2. **Subsequent Tests:**
   - Browser context created with saved session state
   - Credentials pre-loaded from storage (no re-login needed)
   - Significant test speed improvement

3. **After Suite:**
   - Auth state file cleared for next test run
   - No credentials left on disk

### Benefits:
- ⚡ Faster test execution (skip login for each test)
- 🔄 Realistic session persistence testing
- 🛡️ Safe credential handling

---

## 📝 Writing New Tests

### 1. Create a Page Object Class

```java
public class NewPage extends BasePage {
    public String elementLocator = "#element-id";
    
    public NewPage(Page page) {
        super(page);
    }
    
    public void clickElement() {
        page.click(elementLocator);
    }
}
```

### 2. Create a Test Class

```java
public class NewTests extends BaseTest {
    private NewPage newPage;
    
    @Test
    public void newTest() {
        newPage = new NewPage(page);
        newPage.clickElement();
        // Add your assertions
    }
}
```

### 3. Add to TestNG Suite

Edit `testng.xml`:
```xml
<test name="NewTests">
    <classes>
        <class name="ro.carrefour.ucare.app.NewTests"/>
    </classes>
</test>
```

---

## 🐛 Debugging (Work-in-progress)

### Enable Logging
The project uses Java's built-in `Logger`. Logs are printed to console during execution.

### Debug Mode
Run tests with additional logging:
```bash
mvn test -Dorg.testng.internal.debug=true
```

### Run in Headed Mode
Keep `.setHeadless(false)` in `BaseTest.java` to visually follow test execution.

### Inspect Auth State File
Check the saved session: `target/playwright/storageState.json`

---

## 📦 Dependencies

View all dependencies in `pom.xml`:
- **Playwright Java** - Browser automation
- **TestNG** - Test framework
- **Maven Compiler Plugin** - Java compilation

---

## 🔧 Best Practices

1. **Page Object Model** - Keep page logic separate from test logic
2. **Configuration Externalization** - Use `environment.properties` for sensitive data
3. **Defensive Cleanup** - Always clean up resources in `@After` methods
4. **Explicit Waits** - Use Playwright's built-in waiting mechanisms
5. **Logging** - Log important milestones for debugging
6. **Test Independence** - Tests should not depend on execution order

---

## 📈 Future Enhancements

- [ ] Add parallel test execution support (ThreadLocal for contexts)
- [ ] Implement visual regression testing
- [ ] Add screenshot on failure feature
- [ ] Integrate with CI/CD pipeline (Jenkins, GitHub Actions)
- [ ] Add test reporting (Allure, ExtentReports)
- [ ] Desktop browser testing profiles
- [ ] Data-driven testing with parameters
- [ ] API testing integration

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 Support

For issues, questions, or contributions, please open an issue on the GitHub repository.

---

## 🎯 Quick Reference

| Command | Purpose |
|---------|---------|
| `mvn clean install` | Install dependencies |
| `mvn test` | Run all tests |
| `mvn clean compile` | Compile source code only |
| `mvn test -Dtest=TestClassName` | Run specific test |
| `mvn test -Dsuites=testng.xml` | Run specific TestNG suite |

---

**Last Updated:** May 29, 2026  
**Maintained By:** Dan-Laurentiu Hosman (dan_hosman_1@ext.carrefour.com) / u.Care Team

