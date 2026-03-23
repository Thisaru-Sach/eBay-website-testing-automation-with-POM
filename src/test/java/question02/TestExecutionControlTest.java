package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import pages.EbayHomePage;
import pages.SignInPage;

import java.time.Duration;

/**
 * Question 02 – Part 2: Test Execution Control
 *
 * Demonstrates:
 *   • Priority-based execution order  (@Test priority = N)
 *   • Disabled tests                  (enabled = false)
 *   • Conditional skip at runtime     (SkipException)
 *
 * Page interactions are delegated to EbayHomePage and SignInPage.
 * baseURL is injected from testng.xml via @Parameters.
 */
public class TestExecutionControlTest {

    // ── Fields ──────────────────────────────────────────────────────────
    WebDriver    driver;
    EbayHomePage homePage;
    SignInPage   signInPage;
    String       baseURL;          // injected from testng.xml

    // Flag used by testConditionalSkip — false means the test will skip
    private boolean featureEnabled = false;

    // ================================================================
    // LIFECYCLE
    // ================================================================

    @BeforeClass
    @Parameters({"baseURL"})
    public void setUpClass(@Optional("https://www.ebay.com") String baseURL) {
        this.baseURL = baseURL;
        driver     = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage   = new EbayHomePage(driver);
        signInPage = new SignInPage(driver);
        System.out.println("@BeforeClass: Driver ready | baseURL=" + baseURL);
    }

    @AfterClass
    public void tearDownClass() {
        if (driver != null) driver.quit();
    }

    /** Navigate home before every test for a consistent starting state. */
    @BeforeMethod
    public void navigateHome() {
        homePage.open(baseURL);
    }

    // ================================================================
    // PRIORITY-BASED TESTS
    // ================================================================

    /** Priority 1 — first to execute: verify home-page title and URL. */
    @Test(priority = 1)
    public void testNavigateToHomePage() {
        System.out.println("[Priority 1] testNavigateToHomePage");
        Assert.assertTrue(homePage.getPageTitle().contains("eBay"),
                "Title must contain 'eBay'");
        Assert.assertTrue(homePage.getCurrentUrl().contains("ebay.com"),
                "URL must contain 'ebay.com'");
        System.out.println("[Priority 1] PASSED");
    }

    /** Priority 2 — verify search box, category dropdown, and search button. */
    @Test(priority = 2)
    public void testVerifyFormElements() {
        System.out.println("[Priority 2] testVerifyFormElements");
        Assert.assertTrue(homePage.isSearchBoxDisplayed(),  "Search box must be displayed");
        Assert.assertTrue(homePage.isCategoryMenuDisplayed(),"Category dropdown must be displayed");
        Assert.assertTrue(homePage.isSearchButtonEnabled(), "Search button must be enabled");
        System.out.println("[Priority 2] PASSED");
    }

    /**
     * Priority 3 — navigate to sign-in, submit empty form.
     * eBay keeps the user on the sign-in page (client-side validation).
     */
    @Test(priority = 3)
    public void testValidateInputConstraints() {
        System.out.println("[Priority 3] testValidateInputConstraints");
        signInPage.open();
        Assert.assertTrue(signInPage.isEmailFieldDisplayed(), "Email field must be visible");
        signInPage.clickContinue();
        Assert.assertTrue(signInPage.isOnSignInPage(),
                "Empty submit should keep user on sign-in page");
        System.out.println("[Priority 3] PASSED");
    }

    /**
     * Priority 4 — enter a test e-mail and assert it was accepted.
     * Test stops at the login page as per assignment instructions.
     */
    @Test(priority = 4)
    public void testSuccessfulSubmission() {
        System.out.println("[Priority 4] testSuccessfulSubmission");
        signInPage.open();
        signInPage.enterEmail("testuser@example.com");
        Assert.assertEquals(signInPage.getEmailValue(), "testuser@example.com",
                "Email field must contain the typed address");
        System.out.println("[Priority 4] PASSED — stopped at login page as instructed");
    }

    // ================================================================
    // DISABLED TESTS  (enabled = false)
    //
    // Use enabled=false when:
    //   • A feature is not yet implemented and the test would always fail.
    //   • A test is temporarily broken or flaky and needs investigation.
    //   • The test environment does not support the functionality.
    //
    // The test is completely excluded from the run and does NOT appear
    // as SKIPPED — it simply does not execute at all.
    // ================================================================

    /** Disabled: unimplemented feature — would fail unconditionally. */
    @Test(enabled = false)
    public void testDisabledFeatureOne() {
        Assert.fail("This should never run.");
    }

    /** Disabled: flaky test under investigation — excluded until fixed. */
    @Test(enabled = false)
    public void testDisabledFeatureTwo() {
        Assert.fail("This should never run.");
    }

    // ================================================================
    // CONDITIONAL SKIP  (SkipException)
    //
    // More flexible than enabled=false: the decision is made at runtime
    // based on a flag, environment variable, or any runtime condition.
    // The test appears as SKIPPED in the TestNG report (not omitted).
    // ================================================================

    /** Priority 5 — skips at runtime when featureEnabled == false. */
    @Test(priority = 5)
    public void testConditionalSkip() {
        System.out.println("[Priority 5] testConditionalSkip");
        if (!featureEnabled) {
            throw new SkipException(
                    "Skipped: featureEnabled is false. Set featureEnabled=true to activate this test.");
        }
        Assert.assertTrue(featureEnabled, "Feature should be enabled when this runs");
    }
}