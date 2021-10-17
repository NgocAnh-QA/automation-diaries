package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AbstractTest {
    private WebDriver driver;
    protected final Log log;
    private final String workingDir = System.getProperty("user.dir");

    public AbstractTest() {
        log = LogFactory.getLog(getClass());
    }

    public WebDriver getDriver() {
        return driver;
    }


    private boolean checkTrue(boolean condition) {
        boolean pass = true;
        try {
            if (condition == true) {
                log.info(" -------------------------- PASSED -------------------------- ");
            } else {
                log.info(" -------------------------- FAILED -------------------------- ");
            }
            Assert.assertTrue(condition);
        } catch (Throwable e) {
            pass = false;
            log.error("|AbstractTest| - |checkTrue| - Error while assert true " + e.getMessage());
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    /**
     * To verify actual result is true
     *
     * @param condition
     * @return
     */
    protected boolean verifyTrue(boolean condition) {
        return checkTrue(condition);
    }


    private boolean checkFailed(boolean condition) {
        boolean pass = true;
        try {
            if (condition == false) {
                log.info(" -------------------------- PASSED -------------------------- ");
            } else {
                log.info(" -------------------------- FAILED -------------------------- ");
            }
            Assert.assertFalse(condition);
        } catch (Throwable e) {
            pass = false;
            log.error("|AbstractTest| - |checkFailed| - Error while assert false " + e.getMessage());

            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    /**
     * To verify actual result is false
     *
     * @param condition
     * @return
     */
    protected boolean verifyFalse(boolean condition) {
        return checkFailed(condition);
    }

    private boolean checkEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");
            log.error("|AbstractTest| - |checkEquals| - Error while assert equal " + e.getMessage());
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }

    /**
     * To verify actual result and expected result with data type
     *
     * @param actual
     * @param expected
     * @return
     */
    protected boolean verifyEquals(Object actual, Object expected) {
        return checkEquals(actual, expected);
    }


    /**
     * To start webdriver and open browser
     *
     * @param browserName: the name of browser that will be opened
     * @param url:         the url that user is navigated
     * @return
     */
    protected WebDriver openBrowserDriver(String browserName, String url) {
        log.debug("|AbstractTest| - |openBrowserDriver|: Open browser and driver");
        BROWSER browser = BROWSER.valueOf(browserName.toUpperCase());
        if (browser == BROWSER.FIREFOX) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-infobars");
            driver = new FirefoxDriver(options);
        } else if (browser == BROWSER.FIREFOX_HEADLESS) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            options.addArguments("window-size=1920x1080");
            driver = new FirefoxDriver(options);
        } else if (browser == BROWSER.CHROME) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver = new ChromeDriver();
        } else if (browser == BROWSER.CHROME_HEADLESS) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("window-size=1920x1080");
            driver = new ChromeDriver(options);
        } else if (browser == BROWSER.EDGE) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if (browser == BROWSER.SAFARI) {
            driver = new SafariDriver();
        } else {
            log.error("|AbstractTest| - |openBrowserDriver| - Open browser has error");
            throw new RuntimeException("Please input correct the browser name");
        }

        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        return driver;
    }

    /**
     * To close browser and quit driver executable file in Task Manager
     *
     * @param driver
     */
    protected void closeBrowserAndDriver(WebDriver driver) {
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String cmd = "";
            if (driver != null) {
                driver.quit();
            }
            if (driver.toString().toLowerCase().contains("chrome")) {
                if (osName.toLowerCase().contains("mac")) {
                    cmd = "pkill chromedriver";
                } else if (osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
                }
            } else if (driver.toString().toLowerCase().contains("firefox")) {
                if (osName.toLowerCase().contains("mac")) {
                    cmd = "pkill geckodriver";
                } else if (osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq geckodriver*\"";
                }
            } else if (driver.toString().toLowerCase().contains("edge")) {
                if (osName.toLowerCase().contains("mac")) {
                    cmd = "pkill msedgedriver";
                } else if (osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq msedgedriver*\"";
                }
            }
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            log.error("|AbstractTest| - |closeBrowserAndDriver| - Cannot close browser and quit driver: " + e.getMessage());
        }
    }

    /**
     * To generate unique number
     *
     * @return
     */
    protected int getRandomNumber() {
        Random rand = new Random();
        int number = rand.nextInt(99999);
        return number;
    }

    /**
     * To clear cookie on browser
     *
     * @param driver
     */
    protected void clearCookie(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }

    /**
     * To check color is converted to string
     *
     * @param driver
     * @param locator
     * @return
     */
    protected String isColorConvert(WebDriver driver, String locator) {
        String actualColor = "";
        try {
            String color = driver.findElement(By.xpath(locator)).getCssValue("color");
            String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
            int hexValue1 = Integer.parseInt(hexValue[0]);
            hexValue[1] = hexValue[1].trim();
            int hexValue2 = Integer.parseInt(hexValue[1]);
            hexValue[2] = hexValue[2].trim();
            int hexValue3 = Integer.parseInt(hexValue[2]);
            actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
        } catch (Exception e) {
            log.error("|AbstractTest| - |isColorConvert| - Convert color error" + e.getMessage());
        }
        return actualColor;
    }

    /**
     * To take screen shot
     *
     * @param strPath
     */
    protected void captureIfFailed(String strPath) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("strPath"));
            System.out.println("Screenshot taken");
        } catch (Exception e) {
            log.error("|AbstractTest| - |captureIfFailed| - Capture screen shot error" + e.getMessage());
        }
    }


}
