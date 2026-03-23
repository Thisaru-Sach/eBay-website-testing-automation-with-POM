package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * EbayHomePage — Page Object for https://www.ebay.com
 *
 * Encapsulates every locator and every user-facing action on the eBay
 * home page.  Test classes call these methods and never reference
 * By-locators or WebDriver calls directly.
 */
public class EbayHomePage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────
    private static final By SEARCH_BOX     = By.id("gh-ac");
    private static final By SEARCH_BUTTON  = By.cssSelector("form#gh-f button[type='submit']");
    private static final By CATEGORY_MENU  = By.id("gh-cat");
    private static final By SIGN_IN_LINK   = By.xpath("//a[contains(text(),'Sign in') or contains(@href,'signin')]");
    private static final By LOGO           = By.cssSelector(".gh-logo");
    private static final By LOGO_LINK      = By.cssSelector("#gh-logo");
    private static final By NAV_LINKS      = By.cssSelector(".gh-nav-link");
    private static final By FLYOUT_NAV     = By.cssSelector(".vl-flyout-nav__link-container");
    private static final By CATEGORY_TITLE = By.cssSelector(".gh-categories__title");
    private static final By PAUSE_BUTTON   = By.xpath("//button[contains(@aria-label,'Pause')]");

    // ── Constructor ──────────────────────────────────────────────────────
    public EbayHomePage(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ───────────────────────────────────────────────────────

    /**
     * Navigate to the home page and wait for eBay title.
     * @param url  injected from testng.xml via @Parameters
     */
    public EbayHomePage open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.titleContains("eBay"));
        return this;
    }

    // ── Search Box ───────────────────────────────────────────────────────

    /** Type text into the search box (clears first). */
    public EbayHomePage enterSearchTerm(String term) {
        WebElement box = waitForVisible(SEARCH_BOX);
        box.clear();
        box.sendKeys(term);
        return this;
    }

    /** Press ENTER in the search box to submit. */
    public SearchResultsPage submitSearchWithEnter() {
        driver.findElement(SEARCH_BOX).sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }

    /** Click the search button to submit. */
    public SearchResultsPage clickSearchButton() {
        waitForClickable(SEARCH_BUTTON).click();
        return new SearchResultsPage(driver);
    }

    /** @return current value attribute of the search box */
    public String getSearchBoxValue() {
        return driver.findElement(SEARCH_BOX).getAttribute("value");
    }

    /** @return placeholder attribute of the search box */
    public String getSearchBoxPlaceholder() {
        return waitForVisible(SEARCH_BOX).getAttribute("placeholder");
    }

    /** @return type attribute of the search box */
    public String getSearchBoxType() {
        return driver.findElement(SEARCH_BOX).getAttribute("type");
    }

    /** @return whether the search box is displayed */
    public boolean isSearchBoxDisplayed() {
        return waitForVisible(SEARCH_BOX).isDisplayed();
    }

    /** @return whether the search box is enabled */
    public boolean isSearchBoxEnabled() {
        return driver.findElement(SEARCH_BOX).isEnabled();
    }

    /** @return whether the search box is in a selected state */
    public boolean isSearchBoxSelected() {
        return driver.findElement(SEARCH_BOX).isSelected();
    }

    // ── Category Dropdown ─────────────────────────────────────────────────

    /** @return a Select object wrapping the category dropdown */
    public Select getCategorySelect() {
        return new Select(waitForVisible(CATEGORY_MENU));
    }

    /** @return whether the category dropdown is displayed */
    public boolean isCategoryMenuDisplayed() {
        return waitForVisible(CATEGORY_MENU).isDisplayed();
    }

    /** @return whether the category dropdown is enabled */
    public boolean isCategoryMenuEnabled() {
        return driver.findElement(CATEGORY_MENU).isEnabled();
    }

    /** @return the WebElement for the category menu (for assertNotNull checks) */
    public WebElement getCategoryMenuElement() {
        return waitForVisible(CATEGORY_MENU);
    }

    // ── Sign-in Link ──────────────────────────────────────────────────────

    /** @return the sign-in link element */
    public WebElement getSignInLink() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SIGN_IN_LINK));
    }

    /** @return whether the sign-in link is displayed */
    public boolean isSignInLinkDisplayed() {
        return getSignInLink().isDisplayed();
    }

    /** @return the visible text of the sign-in link */
    public String getSignInLinkText() {
        return driver.findElement(SIGN_IN_LINK).getText();
    }

    // ── Logo ──────────────────────────────────────────────────────────────

    /** @return whether the eBay logo is displayed */
    public boolean isLogoDisplayed() {
        return driver.findElement(LOGO).isDisplayed();
    }

    /** @return aria-labelledby attribute of the logo anchor */
    public String getLogoAriaLabelledby() {
        return driver.findElement(LOGO_LINK).getAttribute("aria-labelledby");
    }

    // ── Navigation Links ──────────────────────────────────────────────────

    /** @return list of all visible nav link text values (non-empty only) */
    public List<String> getNavLinkTexts() {
        List<WebElement> links  = driver.findElements(NAV_LINKS);
        List<String>     texts  = new ArrayList<>();
        for (WebElement link : links) {
            String t = link.getText().trim();
            if (!t.isEmpty()) texts.add(t);
        }
        return texts;
    }

    /** @return count of nav link elements found */
    public int getNavLinkCount() {
        return driver.findElements(NAV_LINKS).size();
    }

    // ── Text Extraction (Section 3.2 – Part 5) ───────────────────────────

    /** @return text of the first flyout nav container */
    public String getFirstNavCategoryText() {
        return driver.findElement(FLYOUT_NAV).getText();
    }

    /** @return text of the category dropdown title element */
    public String getCategoryDropdownTitleText() {
        return driver.findElement(CATEGORY_TITLE).getText();
    }

    /** @return the name attribute of the search input */
    public String getSearchBoxNameAttribute() {
        return driver.findElement(SEARCH_BOX).getAttribute("name");
    }

    // ── Carousel Pause Button ─────────────────────────────────────────────

    /**
     * Attempt to click the carousel pause button.
     * @return true if the button was found and clicked; false otherwise.
     */
    public boolean clickPauseButtonIfPresent() {
        try {
            WebElement btn = driver.findElement(PAUSE_BUTTON);
            if (btn.isDisplayed()) {
                btn.click();
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    // ── Search Button ─────────────────────────────────────────────────────

    /** @return whether the search button is enabled */
    public boolean isSearchButtonEnabled() {
        return driver.findElement(SEARCH_BUTTON).isEnabled();
    }
}