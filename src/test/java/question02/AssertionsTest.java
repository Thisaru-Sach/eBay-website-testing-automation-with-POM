package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.EbayHomePage;
import pages.SignInPage;

import java.time.Duration;

/**
 * Question 02 – Part 3.1: Assertions — Hard and Soft
 *
 * Hard Assertions → execution STOPS at the first failure.
 * Soft Assertions → execution CONTINUES; all failures are collected
 *                   and reported together when assertAll() is called.
 *
 * All eBay interactions are delegated to EbayHomePage and SignInPage.
 * baseURL is injected from testng.xml via @Parameters.
 */
public class AssertionsTest {

    // ── Fields ──────────────────────────────────────────────────────────
    WebDriver    driver;
    EbayHomePage homePage;
    SignInPage   signInPage;
    String       baseURL;          // injected from testng.xml

    // ── Expected values ──────────────────────────────────────────────────
    private static final String EXPECTED_HOME_TITLE   = "Electronics, Cars, Fashion, Collectibles & More | eBay";
    private static final String EXPECTED_PLACEHOLDER  = "Search for anything";
    private static final String EXPECTED_INPUT_TYPE   = "text";
    private static final String EXPECTED_HOME_URL     = "https://www.ebay.com/";
    private static final String EXPECTED_SIGNIN_TITLE = "Sign in or Register | eBay";

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

    @BeforeMethod
    public void navigateHome() {
        homePage.open(baseURL);
    }

    // ================================================================
    // HARD ASSERTIONS — execution STOPS at first failure
    // ================================================================

    /**
     * Hard Assert 1 — assertEquals on page title.
     * Any assertion BELOW a failing assertEquals is never reached.
     */
    @Test
    public void testHardAssert_PageTitle() {
        System.out.println("\n[Hard Assert 1] testHardAssert_PageTitle");
        String actualTitle = homePage.getPageTitle();

        // assertEquals: stops immediately if actual != expected
        Assert.assertEquals(actualTitle, EXPECTED_HOME_TITLE,
                "Page title mismatch — actual: " + actualTitle);
        System.out.println("  assertEquals PASSED — title: " + actualTitle);

        Assert.assertTrue(actualTitle.contains("eBay"),  "Title must contain 'eBay'");
        System.out.println("  assertTrue  PASSED — title contains 'eBay'");

        Assert.assertFalse(actualTitle.isEmpty(),        "Title must not be empty");
        System.out.println("  assertFalse PASSED — title is not empty");

        System.out.println("[Hard Assert 1] PASSED\n");
    }

    /**
     * Hard Assert 2 — assertEquals on search-box attributes.
     * If the placeholder check fails, the type and visibility checks are skipped.
     */
    @Test
    public void testHardAssert_SearchBoxAttributes() {
        System.out.println("\n[Hard Assert 2] testHardAssert_SearchBoxAttributes");

        String actualPlaceholder = homePage.getSearchBoxPlaceholder();
        Assert.assertEquals(actualPlaceholder, EXPECTED_PLACEHOLDER,
                "Placeholder mismatch — actual: " + actualPlaceholder);
        System.out.println("  assertEquals PASSED — placeholder: " + actualPlaceholder);

        String actualType = homePage.getSearchBoxType();
        Assert.assertEquals(actualType, EXPECTED_INPUT_TYPE,
                "Input type mismatch — actual: " + actualType);
        System.out.println("  assertEquals PASSED — type: " + actualType);

        Assert.assertTrue(homePage.isSearchBoxDisplayed(), "Search box must be displayed");
        System.out.println("  assertTrue  PASSED — search box is displayed");

        Assert.assertFalse(homePage.isSearchBoxSelected(), "Search box must not be pre-selected");
        System.out.println("  assertFalse PASSED — search box is not selected");

        System.out.println("[Hard Assert 2] PASSED\n");
    }

    /**
     * Hard Assert 3 — assertEquals on URL.
     * The assertTrue / assertFalse below are only reached if assertEquals passes.
     */
    @Test
    public void testHardAssert_URL() {
        System.out.println("\n[Hard Assert 3] testHardAssert_URL");
        String actualUrl = homePage.getCurrentUrl();

        // *** EXECUTION STOPS HERE if the URL does not match exactly ***
        Assert.assertEquals(actualUrl, EXPECTED_HOME_URL,
                "URL mismatch — actual: " + actualUrl);
        System.out.println("  assertEquals PASSED — URL: " + actualUrl);

        Assert.assertTrue(actualUrl.contains("ebay.com"),  "URL must contain 'ebay.com'");
        System.out.println("  assertTrue  PASSED — URL contains 'ebay.com'");

        Assert.assertFalse(actualUrl.contains("signin"),   "Home page URL must not contain 'signin'");
        System.out.println("  assertFalse PASSED — URL does not contain 'signin'");

        System.out.println("[Hard Assert 3] PASSED\n");
    }

    // ================================================================
    // SOFT ASSERTIONS — execution CONTINUES after failures;
    // all failures collected and reported together at assertAll()
    // ================================================================

    /**
     * Soft Assert 1 — multiple home-page element checks.
     * Check 2 is intentionally wrong to show that Checks 3–5 still run.
     * CRITICAL: assertAll() MUST be called — without it all soft-assert
     * failures are silently swallowed and the test shows as PASSED.
     */
    @Test
    public void testSoftAssert_PageElements() {
        System.out.println("\n[Soft Assert 1] testSoftAssert_PageElements");

        // Always create a NEW SoftAssert per test method — never share across tests.
        SoftAssert softAssert = new SoftAssert();

        // Check 1 — title contains "eBay" (expected to PASS)
        String title = homePage.getPageTitle();
        softAssert.assertTrue(title.contains("eBay"),
                "Check 1 FAILED: Title must contain 'eBay'. Actual: " + title);
        System.out.println("  Check 1 evaluated — title: " + title);

        // Check 2 — intentionally wrong to demonstrate soft-assert continues on failure
        softAssert.assertEquals(title, "INTENTIONALLY_WRONG_TITLE",
                "Check 2 FAILED: Intentionally wrong — demonstrating soft assert continues");
        System.out.println("  Check 2 evaluated — assertEquals with wrong expected (will fail)");

        // Check 3 — URL (still runs despite Check 2 having failed)
        String url = homePage.getCurrentUrl();
        softAssert.assertTrue(url.contains("ebay.com"),
                "Check 3 FAILED: URL must contain 'ebay.com'. Actual: " + url);
        System.out.println("  Check 3 evaluated — URL: " + url);

        // Checks 4 & 5 — search-box state
        softAssert.assertTrue(homePage.isSearchBoxDisplayed(),
                "Check 4 FAILED: Search box must be visible");
        softAssert.assertFalse(homePage.isSearchBoxSelected(),
                "Check 5 FAILED: Search box must not be selected");
        System.out.println("  Check 4 & 5 evaluated — search box state verified");

        // CRITICAL: triggers the actual test failure and reports ALL collected failures
        System.out.println("  Calling assertAll() — reporting all collected failures...");
        softAssert.assertAll();

        System.out.println("[Soft Assert 1] COMPLETED\n");
    }

    /**
     * Soft Assert 2 — sign-in page checks.
     * Demonstrates collecting failures across page navigation within one test.
     */
    @Test
    public void testSoftAssert_SignInPage() {
        System.out.println("\n[Soft Assert 2] testSoftAssert_SignInPage");

        SoftAssert softAssert = new SoftAssert();

        signInPage.open();

        // Check 1 — URL contains "signin"
        softAssert.assertTrue(signInPage.isOnSignInPage(),
                "Check 1 FAILED: URL must contain 'signin'. Actual: " + signInPage.getCurrentUrl());
        System.out.println("  Check 1 evaluated — URL: " + signInPage.getCurrentUrl());

        // Check 2 — exact page title
        String actualTitle = signInPage.getPageTitle();
        softAssert.assertEquals(actualTitle, EXPECTED_SIGNIN_TITLE,
                "Check 2 FAILED: Sign-in title mismatch. Actual: " + actualTitle);
        System.out.println("  Check 2 evaluated — title: " + actualTitle);

        // Checks 3–5 — email field properties
        softAssert.assertTrue(signInPage.isEmailFieldDisplayed(),
                "Check 3 FAILED: Email field must be visible");
        softAssert.assertTrue(signInPage.isEmailFieldEnabled(),
                "Check 4 FAILED: Email field must be enabled");
        softAssert.assertEquals(signInPage.getEmailFieldType(), "text",
                "Check 5 FAILED: Email field type mismatch. Actual: " + signInPage.getEmailFieldType());
        System.out.println("  Check 3, 4 & 5 evaluated — email field type: " + signInPage.getEmailFieldType());

        // Check 6 — title not empty
        softAssert.assertFalse(signInPage.getPageTitle().isEmpty(),
                "Check 6 FAILED: Sign-in page title must not be empty");
        System.out.println("  Check 6 evaluated — title not empty");

        // CRITICAL: without assertAll() ALL soft failures are silently ignored
        System.out.println("  Calling assertAll() — reporting all collected failures...");
        softAssert.assertAll();

        System.out.println("[Soft Assert 2] COMPLETED\n");
    }
}