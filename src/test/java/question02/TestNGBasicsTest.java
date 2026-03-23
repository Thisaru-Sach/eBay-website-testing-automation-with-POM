package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EbayHomePage;

import java.time.Duration;
import java.util.List;

/**
 * Question 02 – Part 1: TestNG Basics and Annotations
 *
 * Demonstrates all four lifecycle annotations and three @Test methods.
 * All eBay-specific interactions are delegated to EbayHomePage.
 *
 * baseURL is injected from the suite-level <parameter> in testng.xml.
 * @Optional ensures the class also runs cleanly when launched directly
 * from IntelliJ without a testng.xml suite providing the value.
 */
public class TestNGBasicsTest {

    // ── Fields ──────────────────────────────────────────────────────────
    WebDriver    driver;
    EbayHomePage homePage;
    String       baseURL;          // injected from testng.xml

    // ================================================================
    // LIFECYCLE
    // ================================================================

    /**
     * @BeforeClass — runs ONCE before all @Test methods.
     * Creates the WebDriver and the EbayHomePage page object.
     * baseURL is injected from testng.xml via @Parameters.
     * @Optional("https://www.ebay.com") allows direct IntelliJ runs
     * without a testng.xml suite defining the parameter.
     */
    @BeforeClass
    @Parameters({"baseURL"})
    public void setUpClass(@Optional("https://www.ebay.com") String baseURL) {
        this.baseURL = baseURL;
        driver   = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage = new EbayHomePage(driver);
        System.out.println("@BeforeClass: Setting up test environment | baseURL=" + baseURL);
    }

    /**
     * @AfterClass — runs ONCE after all @Test methods.
     * Closes the browser and releases WebDriver resources.
     */
    @AfterClass
    public void tearDownClass() {
        System.out.println("@AfterClass: Cleaning up test environment");
        if (driver != null) {
            driver.quit();
            System.out.println("@AfterClass: Browser closed successfully");
        }
    }

    /**
     * @BeforeMethod — runs before EACH @Test method.
     * Navigates to a fresh home page so every test starts from a known state.
     */
    @BeforeMethod
    public void setUp() {
        System.out.println("@BeforeMethod: Preparing for test");
        homePage.open(baseURL);
    }

    /**
     * @AfterMethod — runs after EACH @Test method.
     * Clears cookies to reset session state between tests.
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("@AfterMethod: Cleaning up after test");
        driver.manage().deleteAllCookies();
        System.out.println("=========================================================");
    }

    // ================================================================
    // TEST METHODS
    // ================================================================

    /** Test 1 — Verify home-page navigation: title and URL. */
    @Test
    public void testNavigation() {
        System.out.println("Executing: testNavigation");
        Assert.assertTrue(homePage.getPageTitle().contains("eBay"),
                "Title must contain 'eBay'. Actual: " + homePage.getPageTitle());
        Assert.assertTrue(homePage.getCurrentUrl().contains("ebay.com"),
                "URL must contain 'ebay.com'");
        System.out.println("testNavigation PASSED");
    }

    /** Test 2 — Verify the search box and search button are visible and active. */
    @Test
    public void testSearchFunctionality() {
        System.out.println("Executing: testSearchFunctionality");
        Assert.assertTrue(homePage.isSearchBoxDisplayed(), "Search box must be visible");
        Assert.assertTrue(homePage.isSearchBoxEnabled(),   "Search box must be enabled");
        Assert.assertTrue(homePage.isSearchButtonEnabled(), "Search button must be enabled");
        System.out.println("testSearchFunctionality PASSED");
    }

    /** Test 3 — Verify category menu, sign-in link, and nav links are present. */
    @Test
    public void testPageElements() {
        System.out.println("Executing: testPageElements");

        // Category dropdown
        Assert.assertNotNull(homePage.getCategoryMenuElement(), "Category dropdown must be present");
        Assert.assertTrue(homePage.isCategoryMenuDisplayed(),   "Category dropdown must be visible");
        Assert.assertTrue(homePage.isCategoryMenuEnabled(),     "Category dropdown must be enabled");
        System.out.println("  [Category Menu] PASSED — visible and enabled");

        // Sign-in link
        Assert.assertTrue(homePage.isSignInLinkDisplayed(), "Sign in link must be visible");

        // Navigation links
        int navCount = homePage.getNavLinkCount();
        Assert.assertFalse(navCount == 0, "Navigation links must be present on the page");
        System.out.println("  [Nav Links] Total found: " + navCount);

        List<String> expectedLabels = List.of("Deals", "Brand Outlet", "Gift Cards", "Help & Contact", "Sell");
        List<String> actualTexts    = homePage.getNavLinkTexts();

        System.out.println("  [Nav Links] Validation:");
        for (String expected : expectedLabels) {
            boolean found = actualTexts.stream().anyMatch(a -> a.equalsIgnoreCase(expected));
            Assert.assertTrue(found, "Expected nav link not found: '" + expected + "'");
        }

        System.out.println("testPageElements PASSED");
    }
}