package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — root of the Page Object hierarchy.
 *
 * Every page class extends BasePage so it automatically receives:
 *   • a reference to the shared WebDriver instance
 *   • a pre-configured WebDriverWait (15 s)
 *   • common low-level helpers (waitForVisible, waitForClickable, etc.)
 *
 * Test classes never interact with WebDriver directly; they call page
 * methods instead, keeping all driver logic in the page layer.
 */
public abstract class BasePage {

    protected final WebDriver    driver;
    protected final WebDriverWait wait;

    /**
     * @param driver  the WebDriver instance created in the test's @BeforeClass
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Common helpers ───────────────────────────────────────────────────

    /** Wait until element is visible, then return it. */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is clickable, then return it. */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** @return the current page title */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /** @return the current browser URL */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** @return full HTML source of the current page */
    public String getPageSource() {
        return driver.getPageSource();
    }
}