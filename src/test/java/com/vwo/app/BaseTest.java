package com.vwo.app;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    Playwright playwright;
    Browser browser;
    Page page;
    String url = "https://app.vwo.com/";

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeMethod
    public void beforeMethod() {
        page = browser.newPage();
        page.navigate(url);
    }

    @AfterMethod
    public void afterMethod() {
        page.close();
    }


    @AfterTest()
    public void teardown() {
        browser.close();
        playwright.close();
    }

}
