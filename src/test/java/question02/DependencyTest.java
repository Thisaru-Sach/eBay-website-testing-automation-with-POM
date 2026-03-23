package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EbayHomePage;
import pages.SignInPage;

import java.time.Duration;

/**
 * Question 02 – Part 3.4: Test Dependencies
 *
 * A six-test dependency chain:
 *
 *   [1] testOpenBrowser
 *         └─► [2] testNavigateToEbay
 *                   ├─► [3] testVerifyHomePage  ──┐
 *                   └─► [4] testVerifySignInLink──┤
 *                                                └─► [5] testSearchFunctionality
 *                                                          └─► [6] testCleanup (alwaysRun)
 *
 * If [1] fails → [2]–[5] SKIPPED; [6] still runs (alwaysRun = true).
 * If [2] fails → [3]–[5] SKIPPED; [6] still runs.
 *
 * Page interactions are delegated to EbayHomePage and SignInPage.
 * browser and baseURL are injected from testng.xml via @Parameters.
 */
public class DependencyTest {

    // Static so the single driver instance is shared across all tests
    static WebDriver    driver;
    static EbayHomePage homePage;
    static SignInPage   signInPage;

    // ================================================================
    // TEST 1 — ROOT
    // Opens the browser.  Everything else depends on this passing.
    // browser is injected from the suite-level parameter in testng.xml.
    // ================================================================
    @Test
    @Parameters({"browser"})
    public void testOpenBrowser(@Optional("chrome") String browser) {
        System.out.println("\n[Dep 1] testOpenBrowser — browser: " + browser);

        switch (browser.toLowerCase()) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                driver = new ChromeDriver();
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage   = new EbayHomePage(driver);
        signInPage = new SignInPage(driver);

        Assert.assertNotNull(driver, "WebDriver must be initialized");
        System.out.println("[Dep 1] PASSED — " + browser + " driver initialized");

        /*
         * WHAT HAPPENS IF THIS TEST FAILS:
         *   → [2] testNavigateToEbay      : SKIPPED
         *   → [3] testVerifyHomePage      : SKIPPED
         *   → [4] testVerifySignInLink    : SKIPPED
         *   → [5] testSearchFunctionality : SKIPPED
         *   → [6] testCleanup             : RUNS  (alwaysRun = true)
         */
    }

    // ================================================================
    // TEST 2 — Single dependency on testOpenBrowser
    // Navigates using the baseURL injected from testng.xml.
    // ================================================================
    @Test(dependsOnMethods = {"testOpenBrowser"})
    @Parameters({"baseURL"})
    public void testNavigateToEbay(@Optional("https://www.ebay.com") String baseURL) {
        System.out.println("\n[Dep 2] testNavigateToEbay — baseURL: " + baseURL);

        homePage.open(baseURL);

        Assert.assertTrue(homePage.getCurrentUrl().contains("ebay.com"),
                "URL must contain 'ebay.com'. Actual: " + homePage.getCurrentUrl());
        System.out.println("[Dep 2] PASSED — navigated to: " + homePage.getCurrentUrl());
    }

    // ================================================================
    // TEST 3 — Single dependency on testNavigateToEbay
    // Sibling of testVerifySignInLink; both must pass for [5] to run.
    // ================================================================
    @Test(dependsOnMethods = {"testNavigateToEbay"})
    public void testVerifyHomePage() {
        System.out.println("\n[Dep 3] testVerifyHomePage — depends on: testNavigateToEbay");

        String title = homePage.getPageTitle();
        Assert.assertNotNull(title,               "Page title must not be null");
        Assert.assertTrue(title.contains("eBay"), "Title must contain 'eBay'. Actual: " + title);
        Assert.assertFalse(title.isEmpty(),        "Title must not be empty");

        System.out.println("[Dep 3] PASSED — title: " + title);
    }

    // ================================================================
    // TEST 4 — Single dependency on testNavigateToEbay
    // ================================================================
    @Test(dependsOnMethods = {"testNavigateToEbay"})
    public void testVerifySignInLink() {
        System.out.println("\n[Dep 4] testVerifySignInLink — depends on: testNavigateToEbay");

        Assert.assertTrue(homePage.isSignInLinkDisplayed(),
                "Sign-in link must be displayed");
        Assert.assertFalse(homePage.getSignInLinkText().isEmpty(),
                "Sign-in link text must not be empty");

        System.out.println("[Dep 4] PASSED — sign-in text: " + homePage.getSignInLinkText());
    }

    // ================================================================
    // TEST 5 — Multiple dependencies: BOTH [3] AND [4] must pass
    // Re-navigates via baseURL for a fresh state.
    // ================================================================
    @Test(dependsOnMethods = {"testVerifyHomePage", "testVerifySignInLink"})
    @Parameters({"baseURL"})
    public void testSearchFunctionality(@Optional("https://www.ebay.com") String baseURL) {
        System.out.println("\n[Dep 5] testSearchFunctionality");
        System.out.println("        Both testVerifyHomePage AND testVerifySignInLink must PASS");

        homePage.open(baseURL);

        Assert.assertTrue(homePage.isSearchBoxDisplayed(), "Search box must be visible");
        Assert.assertTrue(homePage.isSearchBoxEnabled(),   "Search box must be enabled");

        homePage.enterSearchTerm("laptop");
        Assert.assertEquals(homePage.getSearchBoxValue(), "laptop",
                "Search box must contain 'laptop'");

        System.out.println("[Dep 5] PASSED — search box contains: " + homePage.getSearchBoxValue());
    }

    // ================================================================
    // TEST 6 — alwaysRun = true CLEANUP
    // Runs regardless of whether [5] passed, failed, or was skipped.
    // Guarantees the browser is always closed.
    // ================================================================
    @Test(dependsOnMethods = {"testSearchFunctionality"}, alwaysRun = true)
    public void testCleanup() {
        System.out.println("\n[Dep 6] testCleanup — alwaysRun=true, always executes");

        if (driver != null) {
            driver.quit();
            driver     = null;
            homePage   = null;
            signInPage = null;
            System.out.println("[Dep 6] PASSED — browser closed successfully");
        } else {
            System.out.println("[Dep 6] PASSED — driver was null, nothing to close");
        }
    }
}