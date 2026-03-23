package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * SearchResultsPage — Page Object for the eBay search results page
 * (URL pattern: https://www.ebay.com/sch/...)
 *
 * Reached after submitting a search from EbayHomePage or
 * AdvancedSearchPage.  Encapsulates result-container visibility
 * and result-count text extraction.
 */
public class SearchResultsPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────
    private static final By RESULTS_CONTAINER = By.id("srp-river-results");
    private static final By RESULT_COUNT      = By.cssSelector(".srp-controls__count-heading");

    // ── Constructor ───────────────────────────────────────────────────────
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    // ── Wait for page ─────────────────────────────────────────────────────

    /**
     * Wait until the results page URL contains "sch", signalling that
     * the results page has loaded.
     */
    public SearchResultsPage waitForResultsPage() {
        wait.until(ExpectedConditions.urlContains("sch"));
        return this;
    }

    // ── Results Container ─────────────────────────────────────────────────

    /** Wait for and return the main results container element. */
    public WebElement getResultsContainer() {
        return waitForVisible(RESULTS_CONTAINER);
    }

    /** @return true when the results container is displayed on the page */
    public boolean areResultsDisplayed() {
        try {
            return waitForVisible(RESULTS_CONTAINER).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Result Count ──────────────────────────────────────────────────────

    /**
     * @return the result-count heading text (e.g. "1,234 results for laptop"),
     *         or a fallback string if the element is not present.
     */
    public String getResultCountText() {
        try {
            return driver.findElement(RESULT_COUNT).getText();
        } catch (Exception e) {
            return "(count not available)";
        }
    }

    // ── URL / Title ───────────────────────────────────────────────────────

    /** @return true if the current URL contains "sch" (results URL pattern) */
    public boolean isOnResultsPage() {
        return driver.getCurrentUrl().contains("sch");
    }
}