package question01;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pages.EbayHomePage;

import java.time.Duration;

/**
 * Question 01 – Part 1: Browser Commands
 1. Browser Setup
 ● Initialize ChromeDriver with a proper WebDriver Manager or manual driver setup
 ● Maximize the browser window
 ● Set implicit wait (recommended: 10 seconds)
 2. Navigation Operations
 ● Navigate to https://www.ebay.com
 ● Verify the page URL contains 'ebay.com'
 ● Use assertions to validate successful navigation
 3. Page Information Extraction
 ● Retrieve and print: Page title and its character length
 ● Current URL
 ● Page source length (total characters)
 ● Format output clearly with descriptive labels
 4. Browser Cleanup
 ● Implement proper browser closure in a finally block or try-with-resources
 ● Ensure all browser instances are terminated
 */
public class BrowserCommands {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // ── Page Object ──────────────────────────────────────────────
            EbayHomePage homePage = new EbayHomePage(driver);
            homePage.open("https://www.ebay.com");

            // ── URL Assertion ────────────────────────────────────────────
            Assert.assertTrue(homePage.getCurrentUrl().contains("ebay.com"),
                    "URL Verification Failed!");

            // ── Page Information Extraction ──────────────────────────────
            String title = homePage.getPageTitle();
            System.out.println("=== Browser Commands Test ===");
            System.out.println("Page Title: "         + title);
            System.out.println("Title Length: "       + title.length() + " characters");
            System.out.println("Current URL: "        + homePage.getCurrentUrl());
            System.out.println("Page Source Length: " + homePage.getPageSource().length());

        } finally {
            // ── Cleanup ──────────────────────────────────────────────────
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }
}