package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * AdvancedSearchPage — Page Object for
 * https://www.ebay.com/sch/ebayadvsearch
 *
 * Covers:
 *   • Keyword input and keyword-option dropdown
 *   • Category dropdown (selectByVisibleText / selectByIndex)
 *   • Price range (min / max) inputs
 *   • Checkbox interactions (TitleDesc, Complete, Sold)
 *   • Search button
 *   • General checkbox list (used by ElementInteraction for checkbox demo)
 *   • Submit / category-browse button (used by ElementInteraction button demo)
 */
public class AdvancedSearchPage extends BasePage {

    // ── URL ───────────────────────────────────────────────────────────────
    private static final String PAGE_URL = "https://www.ebay.com/sch/ebayadvsearch";

    // ── Locators ─────────────────────────────────────────────────────────
    // Standard ID locator — id contains no special characters
    private static final By KEYWORD_INPUT = By.id("_nkw");

    // CSS locators used because the ids contain special characters [ ] @
    private static final By KEYWORD_OPTION_SELECT = By.cssSelector(
            "select[id='s0-1-20-4[0]-7[1]-_in_kw']");

    private static final By CATEGORY_SELECT = By.cssSelector(
            "select[id='s0-1-20-4[0]-7[3]-_sacat']");

    private static final By MIN_PRICE = By.cssSelector(
            "input[id='s0-1-20-5[2]-@range-comp[]-@range-textbox[]-textbox']");

    private static final By MAX_PRICE = By.cssSelector(
            "input[id='s0-1-20-5[2]-@range-comp[]-@range-textbox[]_1-textbox']");

    private static final By CB_TITLE_DESC = By.cssSelector(
            "input[id='s0-1-20-5[1]-[0]-LH_TitleDesc']");

    private static final By CB_COMPLETE = By.cssSelector(
            "input[id='s0-1-20-5[1]-[1]-LH_Complete']");

    private static final By CB_SOLD = By.cssSelector(
            "input[id='s0-1-20-5[1]-[2]-LH_Sold']");

    private static final By SEARCH_SUBMIT = By.cssSelector(
            ".adv-form__actions button[type='submit']");

    private static final By ALL_CHECKBOXES = By.cssSelector(
            "input[type='checkbox']");

    // ── Constructor ───────────────────────────────────────────────────────
    public AdvancedSearchPage(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────

    /** Navigate to the Advanced Search page and wait for its title. */
    public AdvancedSearchPage open() {
        driver.get(PAGE_URL);
        wait.until(ExpectedConditions.titleContains("Advanced Search"));
        return this;
    }

    // ── Keyword Input ─────────────────────────────────────────────────────

    /** Type a keyword into the search field (clears first). */
    public AdvancedSearchPage enterKeyword(String keyword) {
        WebElement field = waitForVisible(KEYWORD_INPUT);
        field.clear();
        field.sendKeys(keyword);
        return this;
    }

    /** @return current value of the keyword field */
    public String getKeywordValue() {
        return driver.findElement(KEYWORD_INPUT).getAttribute("value");
    }

    // ── Keyword Option Dropdown ───────────────────────────────────────────

    /** @return a Select object for the keyword-option dropdown */
    public Select getKeywordOptionSelect() {
        return new Select(driver.findElement(KEYWORD_OPTION_SELECT));
    }

    /** Select keyword option by value attribute. */
    public AdvancedSearchPage selectKeywordOptionByValue(String value) {
        getKeywordOptionSelect().selectByValue(value);
        return this;
    }

    /** @return number of options in the keyword option dropdown */
    public int getKeywordOptionCount() {
        return getKeywordOptionSelect().getOptions().size();
    }

    /** @return selected text in the keyword option dropdown */
    public String getSelectedKeywordOption() {
        return getKeywordOptionSelect().getFirstSelectedOption().getText();
    }

    // ── Category Dropdown ─────────────────────────────────────────────────

    /** @return a Select object for the category dropdown */
    public Select getCategorySelect() {
        return new Select(driver.findElement(CATEGORY_SELECT));
    }

    /** Select a category by its visible label text. */
    public AdvancedSearchPage selectCategoryByVisibleText(String text) {
        getCategorySelect().selectByVisibleText(text);
        return this;
    }

    /** Select a category by its zero-based index. */
    public AdvancedSearchPage selectCategoryByIndex(int index) {
        getCategorySelect().selectByIndex(index);
        return this;
    }

    /** @return number of options in the category dropdown */
    public int getCategoryOptionCount() {
        return getCategorySelect().getOptions().size();
    }

    /** @return selected text in the category dropdown */
    public String getSelectedCategory() {
        return getCategorySelect().getFirstSelectedOption().getText();
    }

    // ── Price Range ───────────────────────────────────────────────────────

    /** Enter the minimum price (clears first). */
    public AdvancedSearchPage enterMinPrice(String price) {
        WebElement field = driver.findElement(MIN_PRICE);
        field.clear();
        field.sendKeys(price);
        return this;
    }

    /** Enter the maximum price (clears first). */
    public AdvancedSearchPage enterMaxPrice(String price) {
        WebElement field = driver.findElement(MAX_PRICE);
        field.clear();
        field.sendKeys(price);
        return this;
    }

    /** @return current value of the min price field */
    public String getMinPriceValue() {
        return driver.findElement(MIN_PRICE).getAttribute("value");
    }

    /** @return current value of the max price field */
    public String getMaxPriceValue() {
        return driver.findElement(MAX_PRICE).getAttribute("value");
    }

    // ── Checkboxes ────────────────────────────────────────────────────────

    /** @return whether the "Search in title and description" checkbox is selected */
    public boolean isTitleDescChecked() {
        return driver.findElement(CB_TITLE_DESC).isSelected();
    }

    /** Tick the "Search in title and description" checkbox if not already ticked. */
    public AdvancedSearchPage tickTitleDesc() {
        WebElement cb = driver.findElement(CB_TITLE_DESC);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    /** @return whether the "Completed listings" checkbox is selected */
    public boolean isCompleteChecked() {
        return driver.findElement(CB_COMPLETE).isSelected();
    }

    /** Tick the "Completed listings" checkbox if not already ticked. */
    public AdvancedSearchPage tickComplete() {
        WebElement cb = driver.findElement(CB_COMPLETE);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    /** @return whether the "Sold listings" checkbox is selected */
    public boolean isSoldChecked() {
        return driver.findElement(CB_SOLD).isSelected();
    }

    /** Tick the "Sold listings" checkbox if not already ticked. */
    public AdvancedSearchPage tickSold() {
        WebElement cb = driver.findElement(CB_SOLD);
        if (!cb.isSelected()) cb.click();
        return this;
    }

    /**
     * @return all checkbox elements on the page — used by ElementInteraction
     *         to iterate and interact with the first two checkboxes generically.
     */
    public List<WebElement> getAllCheckboxes() {
        return driver.findElements(ALL_CHECKBOXES);
    }

    // ── Submit ────────────────────────────────────────────────────────────

    /**
     * Click the search submit button and return a SearchResultsPage.
     * Also used by ElementInteraction to demonstrate button clicking.
     */
    public SearchResultsPage clickSearchButton() {
        waitForClickable(SEARCH_SUBMIT).click();
        return new SearchResultsPage(driver);
    }

    /** @return whether the search submit button is enabled */
    public boolean isSearchButtonEnabled() {
        return driver.findElement(SEARCH_SUBMIT).isEnabled();
    }
}