package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * SignInPage — Page Object for https://signin.ebay.com/signin/
 *
 * All test classes that need the sign-in page use this object instead
 * of referencing By-locators or driver calls directly.
 *
 * Note: Tests do NOT create an eBay account and stop at this page
 * as per the assignment instructions.
 */
public class SignInPage extends BasePage {

    // ── URL ───────────────────────────────────────────────────────────────
    private static final String PAGE_URL = "https://signin.ebay.com/signin/";

    // ── Locators ─────────────────────────────────────────────────────────
    private static final By EMAIL_FIELD    = By.id("userid");
    private static final By CONTINUE_BTN  = By.id("signin-continue-btn");

    // ── Constructor ───────────────────────────────────────────────────────
    public SignInPage(WebDriver driver) {
        super(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────

    /** Navigate directly to the sign-in page and wait for the email field. */
    public SignInPage open() {
        driver.get(PAGE_URL);
        waitForVisible(EMAIL_FIELD);
        return this;
    }

    // ── Email Field ───────────────────────────────────────────────────────

    /** Type an e-mail address into the email field (clears first). */
    public SignInPage enterEmail(String email) {
        WebElement field = waitForVisible(EMAIL_FIELD);
        field.clear();
        field.sendKeys(email);
        return this;
    }

    /** @return the current value of the email field */
    public String getEmailValue() {
        return driver.findElement(EMAIL_FIELD).getAttribute("value");
    }

    /** @return the type attribute of the email field */
    public String getEmailFieldType() {
        return driver.findElement(EMAIL_FIELD).getAttribute("type");
    }

    /** @return whether the email field is displayed */
    public boolean isEmailFieldDisplayed() {
        return waitForVisible(EMAIL_FIELD).isDisplayed();
    }

    /** @return whether the email field is enabled */
    public boolean isEmailFieldEnabled() {
        return driver.findElement(EMAIL_FIELD).isEnabled();
    }

    // ── Continue Button ───────────────────────────────────────────────────

    /**
     * Click the Continue button with no email entered.
     * Used to test empty-submission validation (should stay on sign-in page).
     */
    public SignInPage clickContinue() {
        driver.findElement(CONTINUE_BTN).click();
        return this;
    }

    // ── Page State ────────────────────────────────────────────────────────

    /** @return true if the current URL contains "signin" */
    public boolean isOnSignInPage() {
        return driver.getCurrentUrl().contains("signin");
    }

    /**
     * Wait for the title to contain "Sign in" — useful after navigation
     * to confirm the page has fully loaded.
     */
    public SignInPage waitForPageLoad() {
        wait.until(ExpectedConditions.titleContains("Sign in"));
        return this;
    }
}