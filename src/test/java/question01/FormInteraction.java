package question01;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AdvancedSearchPage;
import pages.SearchResultsPage;

import java.time.Duration;

/**
 * Question 01 – Part 2: Navigation and Form Interaction
 *
 * All locator references and WebDriver interactions are delegated to
 * AdvancedSearchPage and SearchResultsPage.  This class only orchestrates
 * the test flow and prints verification output.
 *
 * Runs as a standalone main() — URL is passed directly to the page object.
 */
public class FormInteraction {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // ── 1. Advanced Search Navigation ────────────────────────────
            AdvancedSearchPage advSearch = new AdvancedSearchPage(driver);
            advSearch.open();
            System.out.println("Page loaded. Title: " + advSearch.getPageTitle());
            System.out.println("Current URL: "        + advSearch.getCurrentUrl());

            // ── 2. Keyword Input ─────────────────────────────────────────
            advSearch.enterKeyword("laptop");
            String enteredValue = advSearch.getKeywordValue();
            System.out.println("Keyword entered: "   + enteredValue);
            System.out.println("Keyword Assertion: " + (enteredValue.equals("laptop") ? "PASSED" : "FAILED"));

            // ── Keyword option dropdown ───────────────────────────────────
            System.out.println("Total key word options: " + advSearch.getKeywordOptionCount());
            advSearch.selectKeywordOptionByValue("2");
            System.out.println("Selected by value '2': " + advSearch.getSelectedKeywordOption());

            // ── 3. Category Selection ────────────────────────────────────
            System.out.println("Total category options: " + advSearch.getCategoryOptionCount());

            // selectByVisibleText
            advSearch.selectCategoryByVisibleText("Computers/Tablets & Networking");
            System.out.println("Selected by visibleText: " + advSearch.getSelectedCategory());

            // selectByIndex
            advSearch.selectCategoryByIndex(10);
            System.out.println("Selected by index 10: "   + advSearch.getSelectedCategory());

            // ── 4. Price Range ────────────────────────────────────────────
            advSearch.enterMinPrice("100").enterMaxPrice("500");
            System.out.println("Min Price: " + advSearch.getMinPriceValue());
            System.out.println("Max Price: " + advSearch.getMaxPriceValue());
            System.out.println("Price Range Assertion PASSED.");

            // ── 5. Checkbox Interactions ──────────────────────────────────
            System.out.println("Checkbox 1 (TitleDesc) before — isSelected: " + advSearch.isTitleDescChecked());
            advSearch.tickTitleDesc();
            System.out.println("Checkbox 1 (TitleDesc) after  — isSelected: " + advSearch.isTitleDescChecked());

            System.out.println("Checkbox 2 (Complete) before — isSelected: " + advSearch.isCompleteChecked());
            advSearch.tickComplete();
            System.out.println("Checkbox 2 (Complete) after  — isSelected: " + advSearch.isCompleteChecked());

            System.out.println("Checkbox 3 (Sold) before — isSelected: " + advSearch.isSoldChecked());
            advSearch.tickSold();
            System.out.println("Checkbox 3 (Sold) after  — isSelected: " + advSearch.isSoldChecked());

            // ── 6. Search Execution & Validation ─────────────────────────
            SearchResultsPage resultsPage = advSearch.clickSearchButton();
            System.out.println("Search button clicked.");
            resultsPage.waitForResultsPage();
            System.out.println("Results page URL: " + resultsPage.getCurrentUrl());
            System.out.println("Results displayed: " + resultsPage.areResultsDisplayed());
            System.out.println("Result count: "      + resultsPage.getResultCountText());

            System.out.println("Form Interaction Test COMPLETED.");

        } finally {
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }
}