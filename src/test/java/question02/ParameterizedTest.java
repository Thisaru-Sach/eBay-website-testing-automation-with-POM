package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EbayHomePage;
import pages.SignInPage;

import java.time.Duration;

/**
 * Question 02 – Part 3.3: Parameterization with testng.xml
 *
 * All parameters — browser, baseURL, username, password — are injected
 * from testng.xml via @Parameters.  No value is hard-coded in this class.
 *
 * testng.xml defines TWO <test> blocks for this class (Set A / Set B),
 * each supplying different username/password values so the login test
 * runs independently with different credentials.
 *
 * @Optional default values are provided on every parameter so that the
 * class can also be run directly from IntelliJ (without testng.xml),
 * which generates a temporary suite with no <parameter> tags defined.
 * When run via testng.xml the @Optional defaults are simply ignored.
 *
 * All eBay interactions are delegated to EbayHomePage and SignInPage.
 */
public class ParameterizedTest {

    // ── Fields (all injected via @Parameters) ───────────────────────────
    WebDriver    driver;
    EbayHomePage homePage;
    SignInPage   signInPage;
    String       browser;
    String       baseURL;

    // ================================================================
    // LIFECYCLE
    // ================================================================

    /**
     * @BeforeClass reads browser and baseURL from testng.xml.
     * @Optional ensures the class runs cleanly when launched directly
     * from IntelliJ without a testng.xml suite providing the values.
     */
    @BeforeClass
    @Parameters({"browser", "baseURL"})
    public void setUpClass(@Optional("chrome") String browser,
                           @Optional("https://www.ebay.com") String baseURL) {
        this.browser = browser;
        this.baseURL = baseURL;
        System.out.println("\n[Param] Browser=" + browser + " | URL=" + baseURL);

        if ("chrome".equalsIgnoreCase(browser)) {
            driver = new ChromeDriver();
        } else {
            throw new RuntimeException(
                    "Browser '" + browser + "' is not configured. Use 'chrome'.");
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage   = new EbayHomePage(driver);
        signInPage = new SignInPage(driver);
    }

    @AfterClass
    public void tearDownClass() {
        if (driver != null) driver.quit();
    }

    // ================================================================
    // TEST METHODS
    // ================================================================

    /**
     * Test 1 — Navigate to the baseURL injected from testng.xml and verify the page.
     */
    @Test
    public void testNavigateToBaseURL() {
        System.out.println("\n[Param] testNavigateToBaseURL — " + baseURL);
        homePage.open(baseURL);
        Assert.assertTrue(homePage.getPageTitle().contains("eBay"),
                "Title must contain 'eBay'");
        System.out.println("[Param] testNavigateToBaseURL PASSED");
    }

    /**
     * Test 2 — Open the sign-in page and type the username injected from
     * testng.xml.  Stops here — no real account created.
     *
     * username and password are injected per <test> block, so Set A and
     * Set B each run with their own credentials.
     *
     * @Optional ensures this test can also run standalone from IntelliJ.
     */
    @Test
    @Parameters({"username", "password"})
    public void testLoginPageWithParameters(@Optional("testuser@example.com") String username,
                                            @Optional("TestPassword1") String password) {
        System.out.println("\n[Param] testLoginPageWithParameters — user: " + username);
        signInPage.open();
        signInPage.enterEmail(username);
        Assert.assertEquals(signInPage.getEmailValue(), username,
                "Email field must contain: " + username);
        // password is available but intentionally not used — test stops at sign-in page
        System.out.println("[Param] Entered username, stopping at login page. PASSED.");
    }
}