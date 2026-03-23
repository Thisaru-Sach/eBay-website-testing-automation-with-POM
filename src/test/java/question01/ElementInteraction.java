package question01;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AdvancedSearchPage;
import pages.EbayHomePage;
import pages.SearchResultsPage;

import java.time.Duration;
import java.util.List;

/**
 * Question 01 – Part 3: Element Selection and Interaction
 *
 * Section 3.1 — Locator Strategy Implementation
 *   Demonstrates all five locator strategies with comments explaining
 *   when and why each is preferred.
 *
 * Section 3.2 — Element Interaction Techniques
 *   Text input, dropdowns, checkboxes, button clicking, text extraction.
 *
 * All eBay interactions are delegated to the relevant Page Object.
 * Raw By-locators appear ONLY in section 3.1 to satisfy the assignment
 * requirement of explicitly demonstrating each locator strategy — the
 * rest of the class uses page-object methods exclusively.
 *
 * Runs as a standalone main() — URL is passed directly to page objects.
 */
public class ElementInteraction {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            System.out.println("=== Element Interaction Test ===\n");
            demonstrateLocatorStrategies(driver);
            demonstrateElementInteractions(driver);

        } finally {
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }

    // ================================================================
    // SECTION 3.1 — LOCATOR STRATEGY IMPLEMENTATION
    //
    // Raw By-locators are used here deliberately so each strategy is
    // clearly visible.  In section 3.2 the same elements are accessed
    // through page-object methods.
    // ================================================================
    static void demonstrateLocatorStrategies(WebDriver driver) {
        System.out.println("--- 3.1 Locator Strategy Implementation ---\n");

        EbayHomePage homePage = new EbayHomePage(driver);
        homePage.open("https://www.ebay.com");

        // ── 1. ID LOCATOR ────────────────────────────────────────────────
        // ID is the preferred locator: unique per page, fastest browser
        // lookup, and the least brittle when layout or styling changes.
        try {
            System.out.println("1. ID LOCATOR");
            WebElement searchBox = driver.findElement(By.id("gh-ac"));
            searchBox.clear();
            searchBox.sendKeys("laptop");
            System.out.println("[ID Locator] id='gh-ac', value: " + searchBox.getAttribute("value"));
        } catch (Exception e) {
            System.out.println("[ID Locator] FAILED: " + e.getMessage());
        }

        // ── 2. NAME LOCATOR ──────────────────────────────────────────────
        // Useful for form fields where the HTML 'name' attribute is set
        // consistently but an 'id' is absent or auto-generated.
        try {
            System.out.println("2. NAME LOCATOR");
            WebElement searchByName = driver.findElement(By.name("_nkw"));
            searchByName.clear();
            searchByName.sendKeys("Air conditioner");
            System.out.println("[Name Locator] name='_nkw', tag: " + searchByName.getTagName());
        } catch (Exception e) {
            System.out.println("[Name Locator] FAILED: " + e.getMessage());
        }

        // ── 3. CLASS NAME LOCATOR ────────────────────────────────────────
        // Limitation: unreliable when many elements share the same class
        // or when class names are generated dynamically.
        // By.className() only accepts a SINGLE class token — multiple
        // space-separated classes will cause an InvalidSelectorException.
        try {
            System.out.println("3. CLASS NAME LOCATOR");
            WebElement logo = driver.findElement(By.className("gh-logo"));
            System.out.println("[ClassName Locator] class='gh-logo', tag: " + logo.getTagName());
        } catch (Exception e) {
            System.out.println("[ClassName Locator] FAILED: " + e.getMessage());
        }

        // ── 4. CSS SELECTOR LOCATOR ──────────────────────────────────────
        try {
            System.out.println("4. CSS SELECTOR LOCATOR");

            // Pattern 1 — Attribute selector [name='_nkw']:
            //   targets any <input> whose name attribute equals '_nkw' exactly.
            WebElement byAttr = driver.findElement(By.cssSelector("input[name='_nkw']"));
            System.out.println("[CSS Attribute] input[name='_nkw'] displayed: " + byAttr.isDisplayed());

            // Pattern 2 — Class selector .gh-search-button:
            //   targets any element that carries the CSS class 'gh-search-button'.
            WebElement byClass = driver.findElement(By.cssSelector(".gh-search-button"));
            System.out.println("[CSS Class]  .gh-search-button enabled: " + byClass.isEnabled());
        } catch (Exception e) {
            System.out.println("[CSS Locator] FAILED: " + e.getMessage());
        }

        // ── 5. XPATH LOCATOR ─────────────────────────────────────────────
        try {
            System.out.println("5. XPATH LOCATOR");

            // Pattern 1 — Attribute: //input[@id='gh-ac']
            //   Selects any <input> whose id attribute is exactly 'gh-ac'.
            WebElement byAttrXpath = driver.findElement(By.xpath("//input[@id='gh-ac']"));
            System.out.println("[XPath Attribute] //input[@id='gh-ac'] tag: " + byAttrXpath.getTagName());

            // Pattern 2 — text(): //a[text()='Electronics']
            //   Selects <a> elements whose full visible text is exactly 'Electronics'.
            List<WebElement> byText = driver.findElements(By.xpath("//a[text()='Electronics']"));
            System.out.println("[XPath text()]    'Electronics' links found: " + byText.size());

            // Pattern 3 — contains(): //button[contains(@type,'submit')]
            //   Selects buttons whose type attribute contains the word 'submit'.
            List<WebElement> byContains = driver.findElements(
                    By.xpath("//button[contains(@type,'submit')]")
            );
            System.out.println("[XPath contains()] submit buttons found: " + byContains.size());
        } catch (Exception e) {
            System.out.println("[XPath Locator] FAILED: " + e.getMessage());
        }

        System.out.println();
    }

    // ================================================================
    // SECTION 3.2 — ELEMENT INTERACTION TECHNIQUES
    //
    // From here all interactions use page-object methods.
    // ================================================================
    static void demonstrateElementInteractions(WebDriver driver) {
        System.out.println("--- 3.2 Element Interaction Techniques ---\n");

        EbayHomePage      homePage  = new EbayHomePage(driver);
        AdvancedSearchPage advSearch = new AdvancedSearchPage(driver);

        // ── 1. TEXT INPUT ─────────────────────────────────────────────────
        // sendKeys(), clear(), Keys.ENTER — demonstrated via EbayHomePage
        try {
            System.out.println("1. TEXT INPUT Operations");
            homePage.open("https://www.ebay.com");

            homePage.enterSearchTerm("camera");
            System.out.println("[sendKeys] Field 1: " + homePage.getSearchBoxValue());

            homePage.enterSearchTerm("phone");          // enterSearchTerm calls clear() internally
            System.out.println("[clear+sendKeys] Field 1 re-entered: " + homePage.getSearchBoxValue());

            SearchResultsPage results = homePage.submitSearchWithEnter();   // Keys.ENTER inside page method
            System.out.println("[Keys.ENTER] Search submitted");
            results.waitForResultsPage();

            driver.navigate().back();
            homePage.open("https://www.ebay.com");
        } catch (Exception e) {
            System.out.println("[Text Input] FAILED: " + e.getMessage());
        }

        // ── 2. DROPDOWN ───────────────────────────────────────────────────
        // selectByIndex, selectByValue, selectByVisibleText, getOptions
        try {
            System.out.println("2. DROPDOWN Interactions");
            Select select = homePage.getCategorySelect();

            select.selectByIndex(1);
            System.out.println("[selectByIndex(1)]     : " + select.getFirstSelectedOption().getText());

            select.selectByValue("1");
            System.out.println("[selectByValue('1')]   : " + select.getFirstSelectedOption().getText());

            select.selectByVisibleText("Computers/Tablets & Networking");
            System.out.println("[selectByVisibleText]  : " + select.getFirstSelectedOption().getText());

            List<WebElement> options = select.getOptions();
            System.out.println("[getOptions] Total options: " + options.size());
            for (int i = 0; i < options.size(); i++) {
                System.out.println("  [" + i + "] " + options.get(i).getText());
            }
            System.out.println("[getCurrentlySelected] : " + select.getFirstSelectedOption().getText());
        } catch (Exception e) {
            System.out.println("[Dropdown] FAILED: " + e.getMessage());
        }

        // ── 3. CHECKBOXES ─────────────────────────────────────────────────
        // The eBay home page has no checkboxes, so we navigate to Advanced Search.
        try {
            System.out.println("3. CHECKBOX / Radio Button Handling");
            advSearch.open();

            List<WebElement> checkboxes = advSearch.getAllCheckboxes();
            if (checkboxes.size() >= 2) {
                WebElement cb1 = checkboxes.get(0);
                System.out.println("[Checkbox 1] Before: " + cb1.isSelected());
                if (!cb1.isSelected()) cb1.click();
                System.out.println("[Checkbox 1] After : " + cb1.isSelected());

                WebElement cb2 = checkboxes.get(1);
                System.out.println("[Checkbox 2] Before: " + cb2.isSelected());
                if (!cb2.isSelected()) cb2.click();
                System.out.println("[Checkbox 2] After : " + cb2.isSelected());
            }
        } catch (Exception e) {
            System.out.println("[Checkbox] FAILED: " + e.getMessage());
        }

        // ── 4. BUTTON CLICKING ────────────────────────────────────────────
        // Two different buttons via two different page-object methods.
        try {
            System.out.println("4. BUTTON CLICKING");

            // Button 1: Advanced Search submit button (CSS selector inside page object)
            System.out.println("[Button 1 - AdvSearch submit] enabled: " + advSearch.isSearchButtonEnabled());
            SearchResultsPage resultsPage = advSearch.clickSearchButton();
            resultsPage.waitForResultsPage();
            System.out.println("[Button 1] Clicked — navigated to: " + resultsPage.getCurrentUrl());

            // Brief pause before navigating away (avoids bot-detection)
            Thread.sleep(3000);

            // Button 2: Home page carousel pause button (XPath inside page object)
            homePage.open("https://www.ebay.com");
            boolean paused = homePage.clickPauseButtonIfPresent();
            System.out.println("[Button 2 - Pause/XPath] clicked: " + paused);
        } catch (Exception e) {
            System.out.println("[Button Click] FAILED: " + e.getMessage());
        }

        // ── 5. TEXT EXTRACTION ────────────────────────────────────────────
        // getText() and getAttribute() on three elements each, plus comparisons.
        try {
            System.out.println("5. TEXT EXTRACTION & Attribute Reading");
            homePage.open("https://www.ebay.com");

            // getText() — 3 elements
            String signInText   = homePage.getSignInLinkText();
            String firstNavText = homePage.getFirstNavCategoryText();
            String dropDownText = homePage.getCategoryDropdownTitleText();
            System.out.println("[getText] Sign-in link text  : " + signInText);
            System.out.println("[getText] First nav category : " + firstNavText);
            System.out.println("[getText] Category title     : " + dropDownText);

            // getAttribute() — 3 attributes
            String placeholder    = homePage.getSearchBoxPlaceholder();
            String searchName     = homePage.getSearchBoxNameAttribute();
            String ariaLabelledby = homePage.getLogoAriaLabelledby();
            System.out.println("\n[getAttribute] Search placeholder   : " + placeholder);
            System.out.println("[getAttribute] Search input name    : " + searchName);
            System.out.println("[getAttribute] Logo aria-labelledby : " + ariaLabelledby);

            // Comparisons
            System.out.println("\n[Comparison] Validation Results:");
            boolean signInMatch      = "Sign in".equalsIgnoreCase(signInText.trim());
            boolean navMatch         = "Saved".equalsIgnoreCase(firstNavText.trim());
            boolean placeholderMatch = "Search for anything".equalsIgnoreCase(placeholder.trim());
            boolean nameMatch        = "_nkw".equals(searchName);

            System.out.println("  Sign-in text     : " + (signInMatch      ? "PASSED" : "FAILED")
                    + " — expected='Sign in',             actual='" + signInText.trim()   + "'");
            System.out.println("  First nav item   : " + (navMatch         ? "PASSED" : "FAILED")
                    + " — expected='Saved',               actual='" + firstNavText.trim() + "'");
            System.out.println("  Placeholder text : " + (placeholderMatch ? "PASSED" : "FAILED")
                    + " — expected='Search for anything', actual='" + placeholder.trim()  + "'");
            System.out.println("  Search name attr : " + (nameMatch        ? "PASSED" : "FAILED")
                    + " — expected='_nkw',                actual='" + searchName          + "'");

            System.out.println("\n[Result] " + (signInMatch && navMatch && placeholderMatch && nameMatch
                    ? "All comparisons PASSED." : "One or more comparisons FAILED."));

        } catch (Exception e) {
            System.out.println("[Text Extraction] FAILED: " + e.getMessage());
        }

        System.out.println("\nElement Interaction Test COMPLETED.");
    }
}