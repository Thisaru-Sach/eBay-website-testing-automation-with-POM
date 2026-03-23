package question02;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.EbayHomePage;
import pages.SearchResultsPage;

import java.time.Duration;

/**
 * Question 02 – Part 3.2: Data Providers for Data-Driven Testing
 *
 * @DataProvider feeds rows of test data; the @Test method executes once
 * per row.  Row schema: [searchTerm, expectedCategory, shouldFindResults]
 *
 * All eBay interactions are delegated to EbayHomePage and SearchResultsPage.
 * baseURL is injected from testng.xml via @Parameters.
 */
public class DataProviderTest {

    // ── Fields ──────────────────────────────────────────────────────────
    WebDriver    driver;
    EbayHomePage homePage;
    String       baseURL;          // injected from testng.xml

    // ================================================================
    // LIFECYCLE
    // ================================================================

    @BeforeClass
    @Parameters({"baseURL"})
    public void setUpClass(@Optional("https://www.ebay.com") String baseURL) {
        this.baseURL = baseURL;
        driver   = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage = new EbayHomePage(driver);
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
    // DATA PROVIDER
    // ================================================================

    /**
     * Supplies test data as a 2-D Object array.
     * Each inner array is one test run: [searchTerm, expectedCategory, shouldFindResults].
     *
     *  Rows 1–4 → valid search terms expected to produce results.
     *  Row 5    → empty string edge case: no results expected.
     */
    @DataProvider(name = "searchTerms")
    public Object[][] provideSearchTerms() {
        return new Object[][] {
                { "laptop",         "Electronics",     true  },
                { "shoes",          "Fashion",         true  },
                { "headphones",     "Electronics",     true  },
                { "vintage camera", "Cameras & Photo", true  },
                { "",               "All Categories",  false },
        };
    }

    // ================================================================
    // TEST — runs once per @DataProvider row
    // ================================================================

    /**
     * Searches eBay with the provided term and asserts result visibility
     * according to the shouldFindResults flag.
     */
    @Test(dataProvider = "searchTerms")
    public void testSearchWithDataProvider(String searchTerm,
                                           String expectedCategory,
                                           boolean shouldFindResults) {

        System.out.println("\n[DataProvider] Term='" + searchTerm
                + "' | Category=" + expectedCategory
                + " | ExpectResults=" + shouldFindResults);

        // Type the term (if not empty, assert it was accepted)
        homePage.enterSearchTerm(searchTerm);
        if (!searchTerm.isEmpty()) {
            Assert.assertEquals(homePage.getSearchBoxValue(), searchTerm,
                    "Search box must contain: " + searchTerm);
            System.out.println("  Entered and verified: " + searchTerm);
        }

        // Submit and evaluate results
        SearchResultsPage resultsPage = homePage.submitSearchWithEnter();

        if (shouldFindResults) {
            Assert.assertTrue(resultsPage.areResultsDisplayed(),
                    "Results must be visible for: " + searchTerm);
            System.out.println("  Results displayed: YES");
            System.out.println("  Count: " + resultsPage.getResultCountText());
        } else {
            System.out.println("  Empty search submitted — no results expected.");
        }

        System.out.println("  Run for '" + searchTerm + "' DONE.");
    }
}