package common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AbstractPage {
    protected WebDriverWait explicitWait;
    protected WebElement element;
    protected JavascriptExecutor jsExecutor;
    protected Actions action;
    protected Select select;
    private final Log log = LogFactory.getLog(getClass());

    /**
     * To open your website after launching a browser
     *
     * @param driver
     * @param url:   input url to open page
     */
    protected void openPageUrl(WebDriver driver, String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            log.error("|AbstractPage| - |openPageUrl| - URL cannot opened: " + e.getMessage());
        }
    }

    /**
     * To read the current URL from the browser’s address bar
     *
     * @param driver
     */
    protected String getCurrentPageUrl(WebDriver driver) {
        String url = "";
        try {
            url = driver.getCurrentUrl();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getCurrentPageUrl| - Cannot get current URL: " + e.getMessage());
        }
        return url;
    }

    /**
     * To press the browser’s back button
     *
     * @param driver
     */
    protected void backToPage(WebDriver driver) {
        try {
            driver.navigate().back();
        } catch (Exception e) {
            log.error("|AbstractPage| - |backToPage| - Cannot back to page: " + e.getMessage());
        }
    }

    /**
     * To press the browser’s forward button
     *
     * @param driver
     */
    protected void forwardToPage(WebDriver driver) {
        try {
            driver.navigate().forward();
        } catch (Exception e) {
            log.error("|AbstractPage| - |forwardToPage| - Cannot forward to page: " + e.getMessage());
        }
    }

    /**
     * To refresh the current page
     *
     * @param driver
     */
    protected void refreshCurrentPage(WebDriver driver) {
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            log.error("|AbstractPage| - |refreshCurrentPage| - Cannot refresh current page: " + e.getMessage());
        }
    }

    /**
     * To read the current page title from the browser
     *
     * @param driver
     */
    protected String getCurrentPageTitle(WebDriver driver) {
        String title = "";
        try {
            title = driver.getTitle();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getCurrentPageTitle| - Cannot get current Title: " + e.getMessage());
        }
        return title;
    }

    /**
     * To read the current page source from the browser
     *
     * @param driver
     */
    protected String getCurrentPageSource(WebDriver driver) {
        String pageSource = "";
        try {
            pageSource = driver.getPageSource();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getCurrentPageSource| - Cannot get current Page Source: " + e.getMessage());
        }
        return pageSource;
    }


    /**
     * To get the window handle of the current window
     *
     * @param driver
     * @return the ID of window at the current window
     */
    protected String getWindowID(WebDriver driver) {
        String windowID = "";
        try {
            windowID = driver.getWindowHandle();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getWindowID| - Cannot get window ID: " + e.getMessage());
        }
        return windowID;
    }

    /**
     * To get list of window handles at  the current window
     *
     * @param driver
     * @return the set ID of windows
     */
    protected Set<String> getListWindowsID(WebDriver driver) {
        Set<String> listWindowID = new HashSet<String>();
        try {
            listWindowID = driver.getWindowHandles();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getListWindowsID| - Cannot get list window ID: " + e.getMessage());
        }
        return listWindowID;
    }

    /**
     * To switch to the new window/tab by ID
     *
     * @param driver
     * @param parentID the id of window that will switch to
     */
    protected void switchWindowByID(WebDriver driver, String parentID) {
        try {
            Set<String> IdWinDow = driver.getWindowHandles();
            for (String a : IdWinDow) {
                if (!a.equals(parentID)) {
                    driver.switchTo().window(a);
                }
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |switchWindowByID| - Cannot switch window by ID: " + e.getMessage());
        }
    }

    /**
     * To switch to the new window/tab by Title
     *
     * @param driver
     * @param title: the title of window that will switch to
     */
    protected void switchWindowByTitle(WebDriver driver, String title) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String a : windows) {
                driver.switchTo().window(a);
                if (driver.getTitle().equals(title)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |switchWindowByTitle| - Cannot switch window by title: " + e.getMessage());
        }
    }


    /**
     * To close all windows and switch back to the original window
     *
     * @param driver
     * @param ID:    the ID of parent window
     */
    protected void closeAllWindowsWithoutParentWindow(WebDriver driver, String ID) {
        try {
            Set<String> IdWindows = driver.getWindowHandles();
            for (String a : IdWindows) {
                if (!a.equals(ID)) {
                    driver.switchTo().window(a);
                    sleepInSecond(2);
                    driver.close();
                }
            }
            driver.switchTo().window(ID);
        } catch (Exception e) {
            log.error("|AbstractPage| - |closeAllWindowsWithoutParent| - Cannot close all window without parent window: " + e.getMessage());
        }
    }

    /**
     * To find the frame using your xpath expression and switch to it
     *
     * @param driver
     * @param locator: the locator of frame that will switch to
     */
    protected void switchToFrame(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            driver.switchTo().frame(element);
        } catch (Exception e) {
            log.error("|AbstractPage| - |switchToFrame| - Cannot switch to frame: " + e.getMessage());
        }
    }

    /**
     * To leave an iframe or frameset, switch back to the default content
     *
     * @param driver
     */
    protected void switchToDefaultContent(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            log.error("|AbstractPage| - |switchToDefaultContent| - Cannot switch to default content: " + e.getMessage());
        }
    }


    /**
     * To cast rest parameter
     *
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return full xpath expression with dynamic values
     */
    protected String castToParameter(String locator, String... values) {
        try {
            locator = String.format(locator, (Object[]) values);
            return locator;
        } catch (Exception e) {
            log.error("|AbstractPage| - |castToParameter| - Cannot switch to default content: " + e.getMessage());
        }
        return locator;
    }

    /**
     * To find an element by matching an XPath expression
     *
     * @param locator: the xpath expression of an element
     * @return way to find an element by xpath
     */
    protected By getByXpath(String locator) {
        return By.xpath(locator);
    }

    /**
     * To find an element and returns a first matching on the page
     *
     * @param driver
     * @param locator: the xpath expression of an element that will get
     * @return element matching xpath locator
     */
    protected WebElement getElement(WebDriver driver, String locator) {
        try {
            element = driver.findElement(getByXpath(locator));
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElement| - Cannot get element: " + e.getMessage());
        }
        return element;
    }


    /**
     * To find an element and returns a first matching with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element that will get
     * @param values:  at least a value that will cast to locator
     * @return element matching dynamic xpath locator
     */
    protected WebElement getElement(WebDriver driver, String locator, String... values) {
        try {
            element = driver.findElement(getByXpath(castToParameter(locator, values)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElement| - Cannot get element: " + e.getMessage());
        }
        return element;
    }

    /**
     * To find a list of web elements. If only one element is found, it will still return a collection (of one element). If no element matches the locator, an empty list will be returned.
     *
     * @param driver
     * @param locator: the xpath expression of elements that will get
     * @return list of elements matching xpath locator
     */
    protected List<WebElement> getElements(WebDriver driver, String locator) {
        List<WebElement> listElements = new ArrayList<WebElement>();
        try {
            listElements = driver.findElements(getByXpath(locator));
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElements| - Cannot get list elements: " + e.getMessage());
        }
        return listElements;
    }

    /**
     * To find a collection of web elements with rest parameter in xpath locator. If only one element is found, it will still return a collection (of one element). If no element matches the locator, an empty list will be returned.
     *
     * @param driver
     * @param locator: the xpath expression of elements that will get
     * @param values:  at least a value that will cast to locator
     * @return list of elements matching dynamic xpath locator
     */
    protected List<WebElement> getElements(WebDriver driver, String locator, String... values) {
        List<WebElement> listElements = new ArrayList<WebElement>();
        try {
            listElements = driver.findElements(getByXpath(castToParameter(locator, values)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElements| - Cannot get list elements: " + e.getMessage());
        }
        return listElements;
    }

    /**
     * To return the size of element matching an xpath expression
     *
     * @param driver
     * @param locator: the xpath expression of elements that will get
     * @return the size of list elements matching xpath locator
     */
    protected int getSizeElements(WebDriver driver, String locator) {
        int sizeElement = 0;
        try {
            sizeElement = getElements(driver, locator).size();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getSizeElements| - Cannot get size elements: " + e.getMessage());
        }
        return sizeElement;
    }

    /**
     * To return the size of elements matching an xpath expression with rest parameter
     *
     * @param driver
     * @param locator: the xpath expression of elements that will get
     * @param values:  at least a value that will cast to locator
     * @return the size of list elements matching dynamic xpath locator
     */
    protected int getSizeElements(WebDriver driver, String locator, String... values) {
        int sizeElement = 0;
        try {
            sizeElement = getElements(driver, locator, values).size();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getSizeElements| - Cannot get size elements: " + e.getMessage());
        }
        return sizeElement;
    }

    /**
     * To click an element matching an xpath expression
     *
     * @param driver
     * @param locator: the xpath expression of an element that will be clicked on
     */
    protected void clickToElement(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            element.click();
        } catch (Exception e) {
            log.error("|AbstractPage| - |clickToElement| - Cannot click to element: " + e.getMessage());
        }
    }

    /**
     * To click an element matching an xpath expression with rest parameter
     *
     * @param driver
     * @param locator: the xpath expression of an element that will be clicked on
     * @param values:  at least a value that will cast to locator
     */
    protected void clickToElement(WebDriver driver, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            element.click();
        } catch (Exception e) {
            log.error("|AbstractPage| - |clickToElement| - Cannot click to element: " + e.getMessage());
        }
    }

    /**
     * To type a key sequence in DOM element
     *
     * @param driver
     * @param locator:   the xpath expression of an element that will be sent keys
     * @param textValue: the content that will be sent to text box
     */
    protected void sendKeyToElement(WebDriver driver, String locator, String textValue) {
        try {
            element = getElement(driver, locator);
            clearTextBox(driver, locator);
            element.sendKeys(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyToElement| - Cannot send key to element: " + e.getMessage());
        }
    }

    /**
     * To type a key sequence in DOM element matching an xpath expression with rest parameter
     *
     * @param driver
     * @param textValue: the content that will be sent to text box
     * @param locator:   the xpath expression of a text box that will be sent keys
     * @param values:    at least a value that will cast to locator
     */
    protected void sendKeyToElement(WebDriver driver, String textValue, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            element.clear();
            element.sendKeys(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyToElement| - Cannot send key to element: " + e.getMessage());
        }
    }

    /**
     * To click to a text box has type number
     *
     * @param driver
     * @param locator: the xpath expression of a text box that will be clicked on
     * @param value:   the number of times that will click on the text box
     */
    protected void clickToTextBoxTypeNumber(WebDriver driver, String locator, int value) {
        try {
            element = getElement(driver, locator);
            for (int i = 0; i < value; i++) {
                clickToElement(driver, locator);
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |clickToTextBoxTypeNumber| - Cannot click to textbox by type number: " + e.getMessage());
        }
    }

    /**
     * To clear the content of an editable element with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of a text box that will be cleared
     * @param values:  at least a value that will cast to locator
     */
    protected void clearTextBox(WebDriver driver, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            element.clear();
        } catch (Exception e) {
            log.error("|AbstractPage| - |clearTextBox| - Cannot clear text box: " + e.getMessage());
        }
    }

    /**
     * To clear the content of an editable element
     *
     * @param driver
     * @param locator: the xpath expression of a text box that will be cleared
     */
    protected void clearTextBox(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            element.clear();
        } catch (Exception e) {
            log.error("|AbstractPage| - |clearTextBox| - Cannot clear text box: " + e.getMessage());
        }
    }

    /**
     * To switch to the alert and press the OK button
     *
     * @param driver
     */
    protected void acceptAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            log.error("|AbstractPage| - |acceptAlert| - Cannot accept Alert: " + e.getMessage());
        }
    }

    /**
     * To switch to the alert and press the Cancel button
     *
     * @param driver
     */
    protected void cancelAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            log.error("|AbstractPage| - |cancelAlert| - Cannot cancel Alert: " + e.getMessage());
        }
    }

    /**
     * To switch to the alert and store the alert in a variable for reuse
     *
     * @param driver
     * @return the text is stored at alert
     */
    protected String getTextAlert(WebDriver driver) {
        String textAlert = "";
        try {
            textAlert = driver.switchTo().alert().getText();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getTextAlert| - Cannot get text alert: " + e.getMessage());
        }
        return textAlert;
    }

    /**
     * To switch to the alert and type a message to text box
     *
     * @param driver
     * @param value  the content that will be sent to alert
     */
    protected void setTextAlert(WebDriver driver, String value) {
        try {
            driver.switchTo().alert().sendKeys(value);
        } catch (Exception e) {
            log.error("|AbstractPage| - |setTextAlert| - Cannot set to text alert: " + e.getMessage());
        }
    }

    /**
     * To select an option based upon its text
     *
     * @param driver
     * @param textValue: the option's text that will be selected
     * @param locator:   the xpath expression of a dropdown
     */
    protected void selectItemByVisible(WebDriver driver, String textValue, String locator) {
        try {
            element = getElement(driver, locator);
            select = new Select(element);
            select.selectByVisibleText(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |selectItemByVisible| - Cannot select item by visible: " + e.getMessage());
        }
    }

    /**
     * To select an option based upon its text with rest parameter in xpath locator
     *
     * @param driver
     * @param textValue: the option's text that will be selected
     * @param locator:   the xpath expression of a dropdown
     * @param values:    at least a value that will cast to locator
     */
    protected void selectItemByVisible(WebDriver driver, String textValue, String locator, String... values) {
        try {
            element = getElement(driver, locator, values);
            select = new Select(element);
            select.selectByVisibleText(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |selectItemByVisible| - Cannot select item by visible: " + e.getMessage());
        }
    }

    /**
     * Return a text of webElement referencing the first selection option found
     *
     * @param driver
     * @param locator: the xpath expression of a dropdown
     * @return the option's text was selected
     */
    protected String getFirstSelectedTextInDropdown(WebDriver driver, String locator) {
        String text = "";
        try {
            element = getElement(driver, locator);
            select = new Select(element);
            text = select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getFirstSelectedTextInDropdown| - Cannot get selected in dropdown: " + e.getMessage());
        }
        return text;
    }

    /**
     * Return a text of webElement referencing the first selection option with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of a dropdown
     * @param values:  at least a value that will cast to locator
     * @return the option's text was selected
     */
    protected String getFirstSelectedTextInDropdown(WebDriver driver, String locator, String... values) {
        String text = "";
        try {
            element = getElement(driver, castToParameter(locator, values));
            select = new Select(element);
            text = select.getFirstSelectedOption().getText();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getFirstSelectedTextInDropdown| - Cannot get selected in dropdown: " + e.getMessage());
        }
        return text;
    }

    /**
     * To check elements allow you to select more than one option
     *
     * @param driver
     * @param locator: the xpath expression of a dropdown
     * @return the status of dropdown is multiple or single
     */
    protected boolean isDropdownMultiple(WebDriver driver, String locator) {
        boolean checkMultiple = false;
        try {
            element = getElement(driver, locator);
            select = new Select(element);
            checkMultiple = select.isMultiple();
        } catch (Exception e) {
            log.error("|AbstractPage| - |isDropdownMultiple| - Dropdown multiple error: " + e.getMessage());
        }
        return checkMultiple;
    }

    /**
     * To deselect an <option> based upon its text
     *
     * @param driver
     * @param textValue: the option's text  that will be deselected
     * @param locator:   the xpath expression of a checkbox/dropdown
     */
    protected void deSelectItemByVisible(WebDriver driver, String textValue, String locator) {
        try {
            element = getElement(driver, locator);
            select = new Select(element);
            select.deselectByVisibleText(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |deSelectItemByVisible| - Cannot deselect item by visible: " + e.getMessage());
        }
    }

    /**
     * To deselect an <option> based upon its text with rest parameter in xpath locator
     *
     * @param driver
     * @param textValue: the option's text  that will be deselected
     * @param locator:   the xpath expression of a checkbox/dropdown
     * @param values:    at least a value that will cast to locator
     */
    protected void deSelectItemByVisible(WebDriver driver, String textValue, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            select = new Select(element);
            select.deselectByVisibleText(textValue);
        } catch (Exception e) {
            log.error("|AbstractPage| - |deSelectItemByVisible| - Cannot deselect item by visible: " + e.getMessage());
        }
    }

    /**
     * To deselect all selected <option> elements
     *
     * @param driver
     * @param locator: the xpath expression of a checkbox/dropdown
     */
    protected void deSelectAllOptions(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            select = new Select(element);
            select.deselectAll();
        } catch (Exception e) {
            log.error("|AbstractPage| - |deSelectAllOptions| - Cannot deselect all items: " + e.getMessage());
        }
    }

    /**
     * To return the rendered attribute of the specified element
     *
     * @param driver
     * @param locator:       the xpath expression of an element
     * @param attributeName: the name of attribute that will be got
     * @return the attribute value of an element
     */
    protected String getElementAttribute(WebDriver driver, String locator, String attributeName) {
        String elementAttribute = "";
        try {
            element = getElement(driver, locator);
            elementAttribute = element.getAttribute(attributeName);
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementAttribute| - Cannot get element attribute: " + e.getMessage());
        }
        return elementAttribute;
    }

    /**
     * To return the rendered attribute of the specified element with rest parameter in xpath locator
     *
     * @param driver
     * @param locator:       the xpath expression of an element
     * @param attributeName: the name of attribute that will be got
     * @param values:        at least a value that will cast to locator
     * @return the attribute value of an element matching a dynamic locator
     */
    protected String getElementAttribute(WebDriver driver, String locator, String attributeName, String... values) {
        String elementAttribute = "";
        try {
            element = getElement(driver, castToParameter(locator, values));
            elementAttribute = element.getAttribute(attributeName);
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementAttribute| - Cannot get element attribute: " + e.getMessage());
        }
        return elementAttribute;
    }

    /**
     * To return the rendered text of the specified element
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the element's text matching locator
     */
    protected String getElementText(WebDriver driver, String locator) {
        String elementText = "";
        try {
            element = getElement(driver, locator);
            elementText = element.getText().trim();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementText| - Cannot get element text: " + e.getMessage());
        }
        return elementText;
    }

    /**
     * To return the rendered text of the specified element with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the element's text matching dynamic locator
     */
    protected String getElementText(WebDriver driver, String locator, String... values) {
        String elementText = "";
        try {
            element = getElement(driver, castToParameter(locator, values));
            elementText = element.getText();
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementText| - Cannot get element text: " + e.getMessage());
        }
        return elementText;
    }

    /**
     * To return the rendered text of a list of elements
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the list text of elements matching locator
     */
    protected List<String> getElementsText(WebDriver driver, String locator) {
        List<WebElement> items = getElements(driver, locator);
        ArrayList<String> listItems = new ArrayList<String>();
        try {
            for (WebElement webElement : items) {
                listItems.add(webElement.getText().trim());
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementsText| - Cannot get elements text: " + e.getMessage());
        }
        return listItems;
    }

    /**
     * To return the rendered text of a list of elements with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the list text of elements matching dynamic locator
     */
    protected List<String> getElementsText(WebDriver driver, String locator, String... values) {
        List<WebElement> items = getElements(driver, castToParameter(locator, values));
        ArrayList<String> listItems = new ArrayList<String>();
        try {
            for (WebElement webElement : items) {
                listItems.add(webElement.getText().trim());
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |getElementsText| - Cannot get element text: " + e.getMessage());
        }
        return listItems;

    }

    /**
     * To return the size of elements found
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the size of list elements matching locator
     */
    protected int countElementSize(WebDriver driver, String locator) {
        int elementSize = 0;
        try {
            elementSize = getElements(driver, locator).size();
        } catch (Exception e) {
            log.error("|AbstractPage| - |countElementSize| - Cannot get elements size: " + e.getMessage());
        }
        return elementSize;
    }

    /**
     * To return the size of elements found with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the size of list elements matching dynamic locator
     */
    protected int countElementSize(WebDriver driver, String locator, String... values) {
        int elementSize = 0;
        try {
            elementSize = getElements(driver, castToParameter(locator, values)).size();
        } catch (Exception e) {
            log.error("|AbstractPage| - |countElementSize| - Cannot get elements size: " + e.getMessage());
        }
        return elementSize;
    }

    /**
     * To click on the checkbox of the specific element
     *
     * @param driver
     * @param locator: the xpath expression of a check box
     * @param values:  at least a value that will cast to locator
     */
    protected void checkToCheckBox(WebDriver driver, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            if (!element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |checkToCheckBox| - Cannot check to checkbox: " + e.getMessage());
        }
    }

    /**
     * To click on the checkbox of the specific element with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of a check box
     */
    protected void checkToCheckBox(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            if (!element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |checkToCheckBox| - Cannot check to checkbox: " + e.getMessage());
        }
    }

    /**
     * To uncheck the checkbox of specific element
     *
     * @param driver
     * @param locator: the xpath expression of a check box
     */
    protected void unCheckToCheckBox(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            if (element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |unCheckToCheckBox| - Cannot uncheck to checkbox: " + e.getMessage());
        }
    }

    protected void unCheckToCheckBox(WebDriver driver, String locator, String... values) {
        try {
            element = getElement(driver, castToParameter(locator, values));
            if (element.isSelected()) {
                element.click();
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |unCheckToCheckBox| - Cannot uncheck to checkbox: " + e.getMessage());
        }
    }

    /**
     * To check if the element is displayed or undisplayed.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the boolean value, true if the element is undisplayed on a web page, else returns false.
     */
    protected boolean isElementUnDisplayed(WebDriver driver, String locator) {
        boolean status = true;
        try {
            overrideGlobalTimeout(driver, GlobalConstants.SHORT_TIMEOUT);
            List<WebElement> elements = getElements(driver, locator);
            overrideGlobalTimeout(driver, GlobalConstants.LONG_TIMEOUT);
            if (elements.size() == 0) {
                return status;
            } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
                return status;
            } else {
                status = false;
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isElementUnDisplayed| - Element is undisplayed error : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check if the element is displayed or undisplayed with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the boolean value, true if the element is undisplayed on a web page, else returns false.
     */
    protected boolean isElementUnDisplayed(WebDriver driver, String locator, String... values) {
        boolean status = true;
        try {
            overrideGlobalTimeout(driver, GlobalConstants.SHORT_TIMEOUT);
            List<WebElement> elements = getElements(driver, castToParameter(locator, values));
            overrideGlobalTimeout(driver, GlobalConstants.LONG_TIMEOUT);
            if (elements.size() == 0) {
                return status;
            } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
                return status;
            } else {
                status = false;
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isElementUnDisplayed| - Element is undisplayed error : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check if the element is displayed or undisplayed.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the boolean value, true if the element is displayed on a web page, else returns false.
     */
    protected boolean isElementDisplayed(WebDriver driver, String locator) {
        boolean check = true;
        try {
            if (getElement(driver, locator).isDisplayed()) {
                return check;
            }
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isElementDisplayed| - Element is displayed error: " + e.getMessage());
        }
        return check;

    }

    /**
     * To check if the element is displayed or undisplayed with rest parameter in xpath locator. Returns a boolean value, true if the element is displayed on a web page, else returns false.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the boolean value, true if the element is displayed on a web page, else returns false.
     */
    protected boolean isElementDisplayed(WebDriver driver, String locator, String... values) {
        boolean check = true;
        try {
            if (getElement(driver, castToParameter(locator, values)).isDisplayed()) {
                return check;
            }
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isElementDisplayed| - Element is displayed error: " + e.getMessage());
        }
        return check;
    }

    /**
     * to check if the element is enabled or disabled.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the boolean value, True if the element is enabled in the current browsing context, else returns false.
     */
    protected boolean isElementEnabled(WebDriver driver, String locator) {
        boolean check = true;
        try {
            if (getElement(driver, locator).isEnabled()) {
                return check;
            }
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isElementEnabled| - Element is enabled error: " + e.getMessage());
        }
        return check;
    }

    /**
     * To determines if the referenced Element is selected or not.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return the boolean value, True if referenced element is selected in the current browsing context, else returns false.
     */
    protected boolean isElementSelected(WebDriver driver, String locator) {
        boolean check = true;
        try {
            if (getElement(driver, locator).isSelected()) {
                return check;
            }
            ;
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isElementSelected| - Element is selected error: " + e.getMessage());
        }
        return check;
    }

    /**
     * To determines if the referenced Element is selected or not with rest parameter in xpath locator.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return the boolean value, True if referenced element is selected in the current browsing context, else returns false.
     */
    protected boolean isElementSelected(WebDriver driver, String locator, String... values) {
        boolean check = true;
        try {
            if (getElement(driver, castToParameter(locator, values)).isSelected()) {
                return check;
            }
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isElementSelected| - Element is selected error: " + e.getMessage());
        }
        return check;
    }

    /**
     * To perform double-click action on the element
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void doubleClickToElement(WebDriver driver, String locator) {
        try {
            action = new Actions(driver);
            action.doubleClick(getElement(driver, locator)).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |doubleClickToElement| - Cannot double click to element: " + e.getMessage());
        }
    }

    /**
     * To perform context-click action on the element
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void rightClickToElement(WebDriver driver, String locator) {
        try {
            action = new Actions(driver);
            action.contextClick(getElement(driver, locator)).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |rightClickToElement| - Cannot right click to element: " + e.getMessage());
        }
    }

    /**
     * To moves the mouse to the middle of the element
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void hoverMouseToElement(WebDriver driver, String locator) {
        try {
            action = new Actions(driver);
            action.moveToElement(getElement(driver, locator)).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |hoverMouseToElement| - Cannot hover mouse to element: " + e.getMessage());
        }
    }

    /**
     * To moves the mouse to the middle of the element with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     */
    protected void hoverMouseToElement(WebDriver driver, String locator, String... values) {
        try {
            action = new Actions(driver);
            action.moveToElement(getElement(driver, castToParameter(locator, values))).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |hoverMouseToElement| - Cannot hover mouse to element: " + e.getMessage());
        }
    }

    /**
     * To perform move to the element and clicks (without releasing) in the middle of the given element
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void clickAndHoldToElement(WebDriver driver, String locator) {
        try {
            action = new Actions(driver);
            action.clickAndHold(getElement(driver, locator)).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |clickAndHoldToElement| - Cannot click and hold to element: " + e.getMessage());
        }
    }

    /**
     * This method firstly performs a click-and-hold on the source element, moves to the location of the target element and then releases the mouse.
     *
     * @param driver
     * @param sourceLocator: the xpath of source locator of an element
     * @param targetLocator: the xpath of target locator of an element
     */
    protected void dragAnDropElement(WebDriver driver, String sourceLocator, String targetLocator) {
        try {
            action = new Actions(driver);
            action.dragAndDrop(getElement(driver, sourceLocator), getElement(driver, targetLocator)).perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |dragAnDropElement| - Cannot drag and drop element: " + e.getMessage());
        }
    }


    /**
     * To perform keyboard action
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param key:     keyboard that will be pressed
     */
    protected void sendKeyBoardToElement(WebDriver driver, String locator, Keys key) {
        try {
            action = new Actions(driver);
            action.sendKeys(getElement(driver, locator), key).build().perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyBoardToElement| - Cannot send key board to element: " + e.getMessage());
        }
    }


    protected void keyDownToElement(WebDriver driver, Keys key) {
        try {
            action = new Actions(driver);
            action.keyDown(key).build().perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyDownControlToElement| - Cannot send key down control to element: " + e.getMessage());
        }
    }

    protected void keyUpToElement(WebDriver driver, Keys key) {
        try {
            action = new Actions(driver);
            action.keyUp(key).build().perform();
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyDownControlToElement| - Cannot send key up control to element: " + e.getMessage());
        }
    }


    /**
     * To send key to element by executing Java script
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param text:    the content that will be sent to text box
     */
    protected void sendKeyToElementByJS(WebDriver driver, String locator, String text) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].setAttribute('value','" + text + "')", getElement(driver, locator));
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyToElementByJS| - Cannot send key to element by JS: " + e.getMessage());
        }
    }

    /**
     * To send key to element with rest parameter in xpath locator by executing JavaScript
     *
     * @param driver
     * @param text:    the content that will be sent to element
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     */
    protected void sendKeyToElementByJS(WebDriver driver, String text, String locator, String... values) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].setAttribute('value','" + text + "')", getElement(driver, castToParameter(locator, values)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |sendKeyToElementByJS| - Cannot send key to element by JS: " + e.getMessage());
        }
    }

    /**
     * To click on element by executing JavaScript
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void clickElementByJS(WebDriver driver, String locator) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click()", getElement(driver, locator));
        } catch (Exception e) {
            log.error("|AbstractPage| - |clickElementByJS| - Cannot click on element by JS: " + e.getMessage());
        }
    }


    /**
     * To open new tab and switch to it
     *
     * @param driver
     */
    protected void openNewTabByJS(WebDriver driver) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("window.open()");
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
        } catch (Exception e) {
            log.error("|AbstractPage| - |openNewTabByJS| - Cannot open new tab by JS: " + e.getMessage());
        }
    }


    /**
     * To navigate to url by executing JavaScript
     *
     * @param driver
     * @param url:   The address that will navigate to
     */
    protected void navigateToUrlByJS(WebDriver driver, String url) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("window.location='" + url + "'");
        } catch (Exception e) {
            log.error("|AbstractPage| - |navigateToUrlByJS| - Cannot navigate to url by JS: " + e.getMessage());
        }
    }

    /**
     * To get return domain from script
     *
     * @param driver
     * @return the domain at the current page
     */
    protected Object getDomainByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |getDomainByJS| - Cannot get domain at the current page by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("return document.domain");
    }

    /**
     * To get return title from script
     *
     * @param driver
     * @return the title at the current page
     */
    protected Object getTitleByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |getTitleByJS| - Cannot get title at the current page by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("return document.title");
    }

    /**
     * To get return url from script
     *
     * @param driver
     * @return the url address at the current page
     */
    protected Object getUrlByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |getUrlByJS| - Cannot get url by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("return document.URL");
    }


    protected Object getAuthorAccessTokenByJS(WebDriver driver) {
        Object token ="";
        try {
            jsExecutor = (JavascriptExecutor) driver;
            token = jsExecutor.executeScript("return window.localStorage.getItem('auth0AccessToken')");
        } catch (Exception e) {
            log.error("|AbstractPage| - |getAuthorAccessToken| - Cannot get token by JS: " + e.getMessage());
        }
        return token;
    }


    /**
     * To get return text from script
     *
     * @param driver
     * @return the text content of the specified node, and all its descendants
     */
    protected Object getInnerTextByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |getInnerTextByJS| - Cannot get inner text by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("return document.documentElement.innerText");
    }

    /**
     * To scroll to element with rest parameter in xpath locator by executing JavaScript
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     * @return
     */
    protected Object scrollToElementByJS(WebDriver driver, String locator, String... values) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |scrollToElementByJS| - Cannot scroll to element by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("arguments[0].scrollIntoView(true)", getElement(driver, castToParameter(locator, values)));
    }

    /**
     * To scroll to element by executing JavaScript
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return
     */
    protected Object scrollToElementByJS(WebDriver driver, String locator) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |scrollToElementByJS| - Cannot scroll to element by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("arguments[0].scrollIntoView(true)", getElement(driver, locator));
    }

    /**
     * To scroll to the bottom page by executing JavaScript
     *
     * @param driver
     * @return
     */
    protected Object scrollToBottomPageByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |scrollToBottomPageByJS| - Cannot scroll to bottom of the page by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }


    /**
     * To scroll to the top page by executing JavaScript
     *
     * @param driver
     * @return
     */
    protected Object scrollToTopPageByJS(WebDriver driver) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |scrollToTopPageByJS| - Cannot scroll to top of the page by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("window.scrollTo(0, 0)");

    }

    /**
     * To remove the specific attribute of element by executing JavaScript
     *
     * @param driver
     * @param locator:   the xpath expression of an element
     * @param attribute: the name of attribute that will be removed
     * @return
     */
    protected Object removeAttributeByJS(WebDriver driver, String locator, String attribute) {
        try {
            jsExecutor = (JavascriptExecutor) driver;
        } catch (Exception e) {
            log.error("|AbstractPage| - |scrollToTopPageByJS| - Cannot remove attribute by JS: " + e.getMessage());
        }
        return jsExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "')", getElement(driver, locator));

    }

    /**
     * To highlight an element by executing JavaScript
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void highlightElement(WebDriver driver, String locator) {
        try {
            element = getElement(driver, locator);
            String originalStyle = element.getAttribute("style");
            jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
            sleepInSecond(1);
            jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
        } catch (Exception e) {
            log.error("|AbstractPage| - |highlightElement| - Cannot highlight an element by JS: " + e.getMessage());
        }
    }

    /**
     * To check image is loaded or not by executing JavaScript
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @return a boolean value, true if image is load, else false
     */
    protected boolean isImageLoaded(WebDriver driver, String locator) {
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth !=\"underfined\"&& arguments[0].naturalWidth > 0", getElement(driver, locator));
        try {
            if (status == true) {
                return status;
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |isImageLoaded| - Image cannot be loaded error: " + e.getMessage());
        }
        return status;
    }

    /**
     * To sleep without any conditions
     *
     * @param time
     */
    protected void sleepInSecond(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            log.error("|AbstractPage| - |sleepInSecond| - Sleep in second error: " + e.getMessage());
        }
    }

    /**
     * To wait till all elements present on the web page that match the locator are visible.
     *
     * @param driver
     * @param locator: the xpath expression of all elements
     */
    protected void waitForAllElementsVisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
        try {
            explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForAllElementsVisible| - Error wait for all elements are visible : " + e.getMessage());
        }
    }

    /**
     * To wait till all elements present and visible on the web page that match the dynamic locator.
     *
     * @param driver
     * @param locator: the xpath expression of all elements
     * @param values:  at least a value that will cast to locator
     */
    protected void waitForAllElementsVisible(WebDriver driver, String locator, String... values) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
        try {
            explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(castToParameter(locator, values))));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForAllElementsVisible| - Error wait for all elements are visible : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is present on the DOM of a page and visible
     *
     * @param driver
     * @param locator:the xpath expression of an element
     */
    protected void waitForElementVisible(WebDriver driver, String locator) {
        explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
        try {
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForElementVisible| - Error wait for an element is visible : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is present on the DOM of a page and visible with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     */
    protected void waitForElementVisible(WebDriver driver, String locator, String... values) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(castToParameter(locator, values))));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForElementVisible| - Error wait for an element is visible : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is visible and enabled such that you can click it.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void waitForElementClickable(WebDriver driver, String locator) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForElementClickable| - Error wait for an element is clickable : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is visible and enabled such that you can click it with rest parameter in xpath locator
     *
     * @param driver
     * @param locator: the xpath expression of an element
     * @param values:  at least a value that will cast to locator
     */
    protected void waitForElementClickable(WebDriver driver, String locator, String... values) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(castToParameter(locator, values))));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForElementClickable| - Error wait for an element is clickable : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is either invisible or not present on the DOM.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void waitForElementInvisible(WebDriver driver, String locator) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.SHORT_TIMEOUT);
            overrideGlobalTimeout(driver, GlobalConstants.SHORT_TIMEOUT);
            explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
            overrideGlobalTimeout(driver, GlobalConstants.LONG_TIMEOUT);
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitForElementInvisible| - Error wait for an element is invisible : " + e.getMessage());
        }
    }

    /**
     * To wait till an alert is presence on a web page
     *
     * @param driver
     */
    protected void waitAlertPresence(WebDriver driver) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.alertIsPresent());
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitAlertPresence| - Error wait for an alert is presence : " + e.getMessage());
        }
    }

    /**
     * To wait till an element is present on the DOM of a page.
     *
     * @param driver
     * @param locator: the xpath expression of an element
     */
    protected void waitElementPresence(WebDriver driver, String locator) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.presenceOfElementLocated(getByXpath(locator)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitElementPresence| - Error wait for an element is presence : " + e.getMessage());
        }
    }

    /**
     * To wait till there is at least one element present on a web page.
     *
     * @param driver
     * @param locator: the xpath expression of all elements
     */
    protected void waitAllElementsPresence(WebDriver driver, String locator) {
        try {
            explicitWait = new WebDriverWait(driver, GlobalConstants.LONG_TIMEOUT);
            explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(locator)));
        } catch (Exception e) {
            log.error("|AbstractPage| - |waitAllElementsPresence| - Error wait for all elements are presence : " + e.getMessage());
        }
    }

    /**
     * To override global time out. The implicit wait is set for the life of the session since override
     *
     * @param driver
     * @param timeOut
     */
    protected void overrideGlobalTimeout(WebDriver driver, long timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }

    /**
     * To check data type is string sorted ascending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted ascending, else false
     */
    protected boolean isDataStringSortedAscending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(element.getText());
            }
            ArrayList<String> sortedList = new ArrayList<>();
            for (String child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isDataStringSortedAscending| - Error data string is sorted ascending : " + e.getMessage());
        }
        return status;

    }

    /**
     * To check data type is string sorted descending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted descending, else false
     */
    protected boolean isDataStringSortedDescending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(element.getText());
            }
            ArrayList<String> sortedList = new ArrayList<>();
            for (String child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            Collections.reverse(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isDataStringSortedDescending| - Error data string is sorted descending : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check data type is float sorted ascending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted ascending, else false
     */
    protected boolean isDataFloatSortedAscending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<Float> arrayList = new ArrayList<Float>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(Float.parseFloat(element.getText().replace("$", "").replace(",", "").trim()));
            }
            ArrayList<Float> sortedList = new ArrayList<Float>();
            for (Float child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isDataFloatSortedAscending| - Error data float is sorted ascending : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check data type is float sorted descending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted descending, else false
     */
    protected boolean isDataFloatSortedDescending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<Float> arrayList = new ArrayList<Float>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(Float.parseFloat(element.getText().replace("$", "").replace(",", "").trim()));
            }
            ArrayList<Float> sortedList = new ArrayList<Float>();
            for (Float child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            Collections.reverse(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (NumberFormatException e) {
            status = false;
            log.error("|AbstractPage| - |isDataFloatSortedDescending| - Error data float is sorted descending : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check data type is date sorted ascending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted ascending, else false
     */
    protected boolean isDataDateSortedAscending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<Date> arrayList = new ArrayList<Date>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(convertStringToDate(element.getText().trim()));
            }
            ArrayList<Date> sortedList = new ArrayList<Date>();
            for (Date child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isDataDateSortedAscending| - Error data date is sorted ascending : " + e.getMessage());
        }
        return status;
    }

    /**
     * To check data type is date sorted descending
     *
     * @param driver
     * @param locator: the xpath expression of all elements which will be sorted
     * @return a boolean value, true if data is sorted descending, else false
     */
    protected boolean isDataDateSortedDescending(WebDriver driver, String locator) {
        boolean status = true;
        try {
            ArrayList<Date> arrayList = new ArrayList<Date>();
            List<WebElement> elementList = driver.findElements(By.xpath(locator));
            for (WebElement element : elementList) {
                arrayList.add(convertStringToDate(element.getText().trim()));
            }
            ArrayList<Date> sortedList = new ArrayList<Date>();
            for (Date child : arrayList) {
                sortedList.add(child);
            }
            Collections.sort(sortedList);
            Collections.reverse(sortedList);
            if (sortedList.equals(arrayList)) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isDataDateSortedDescending| - Error data date is sorted descending : " + e.getMessage());
        }
        return status;
    }

    /**
     * To convert data type from String to Date
     *
     * @param dateInString
     * @return
     */
    protected Date convertStringToDate(String dateInString) {
        dateInString = dateInString.replace(".", "");
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            log.error("|AbstractPage| - |convertStringToDate| - Cannot convert from String to Data: " + e.getMessage());
        }
        return date;
    }

    /**
     * To check if data at table is displayed or not
     *
     * @param driver
     * @param locatorColumnName: locator of column's name
     * @param locatorColumn:     locator of column contains text value
     * @param columnName:        column's name on table
     * @param textValue:         expected text value
     * @return a boolean value, true if data is displayed correctly, else false
     */
    protected boolean isDataDisplayedAtTable(WebDriver driver, String locatorColumnName, String locatorColumn, String columnName, String textValue) {
        boolean check = true;
        try {
            int indexOfColumnName = getSizeElements(driver, locatorColumnName, columnName) + 1;
            locatorColumn = castToParameter(locatorColumn, String.valueOf(indexOfColumnName));
            List<String> items = getElementsText(driver, locatorColumn);
            for (String s : items) {
                if (s.contains(textValue)) {
                    return check;
                }
            }
        } catch (Exception e) {
            check = false;
            log.error("|AbstractPage| - |isDataDisplayedAtTable| - Data is not displayed at table: " + e.getMessage());
        }
        return check;
    }

    /**
     * To check if text result equals keyword or not
     *
     * @param driver
     * @param keyword:       text value that you want to search for
     * @param resultLocator: locator of text result
     * @return return a boolean value, true if text result equals keyword, else false
     */
    protected boolean isResultEqualsKeyword(WebDriver driver, String keyword, String resultLocator) {
        boolean status = true;
        try {
            int checkCount = 0;
            List<String> listItems = getElementsText(driver, resultLocator);
            for (String p : listItems) {
                if (p.trim().equals(keyword)) {
                    checkCount++;
                }
            }
            if (listItems.size() == checkCount) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isResultEqualsKeyword| - Error search result doesn't equals keyword: " + e.getMessage());
        }
        return status;
    }

    /**
     * To check if text result contains keyword or not
     *
     * @param driver
     * @param keyword:       text value that you want to search for
     * @param resultLocator: locator of text result
     * @return a boolean value, true if text result contains keyword, else false
     */
    protected boolean isResultContainsKeyword(WebDriver driver, String keyword, String resultLocator) {
        boolean status = true;
        try {
            int checkCount = 0;
            List<String> listItems = getElementsText(driver, resultLocator);
            for (String p : listItems) {
                if (p.trim().contains(keyword)) {
                    checkCount++;
                }
            }
            if (listItems.size() == checkCount) {
                return status;
            }
        } catch (Exception e) {
            status = false;
            log.error("|AbstractPage| - |isResultContainsKeyword| - Error search result doesn't contains keyword: " + e.getMessage());
        }
        return status;
    }

    /**
     * To check if text result contains keyword values
     * @param driver
     * @param resultLocator: locator of text result
     * @param valueExpected
     * @return a boolean value, true if text result contains keywords, else false
     */
    public boolean isResultContainsKeyword(WebDriver driver, String resultLocator, String... valueExpected) {
        int check = 0;
        List<String> listItems = getElementsText(driver, resultLocator);
        String[] arrayItems = new String[listItems.size()];
        arrayItems = listItems.toArray(arrayItems);

        for (int i = 0; i < arrayItems.length; i++) {
            for (int j = 0; j < valueExpected.length; j++) {
                if (arrayItems[i].equals(valueExpected[j])) {
                    check++;
                }
            }
        }
        if (listItems.size() == check) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * To select multiple options in customer dropdown
     *
     * @param driver
     * @param xpathLocatorParent:   the xpath expression contains all items in dropdown
     * @param xpathLocatorAllItems: the xpath expression of all items
     * @param expectedValue:        the value that you want to select
     */
    protected void multipleSelect(WebDriver driver, String xpathLocatorParent, String xpathLocatorAllItems, String... expectedValue) {
        try {
            driver.findElement(By.xpath(xpathLocatorParent)).click();
            explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathLocatorAllItems)));
            List<WebElement> allItems = driver.findElements(By.xpath(xpathLocatorAllItems));
            for (WebElement items : allItems) {
                for (String item : expectedValue) {
                    if (items.getText().equals(item)) {
                        jsExecutor.executeScript("arguments[0].scrollIntoView(true)", items);
                        sleepInSecond(1);
                        items.click();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |multipleSelect| - Cannot select multiple options: " + e.getMessage());
        }
    }

    /**
     * To select an option in custom dropdown
     *
     * @param driver
     * @param xpathLocatorParent:   the xpath expression contains all items in dropdown
     * @param xpathLocatorAllItems: the xpath expression of all items
     * @param textExpected
     */
    protected void selectItemInCustomDropdown(WebDriver driver, String xpathLocatorParent, String xpathLocatorAllItems, String textExpected) {
        try {
            driver.findElement(By.xpath(xpathLocatorParent)).click();
            explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathLocatorAllItems)));
            List<WebElement> listAllElement = driver.findElements(By.xpath(xpathLocatorAllItems));
            for (WebElement itemElement : listAllElement) {
                if (itemElement.getText().equals(textExpected)) {
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true)", itemElement);
                    explicitWait.until(ExpectedConditions.elementToBeClickable(itemElement));
                    sleepInSecond(2);
                    itemElement.click();
                    sleepInSecond(2);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("|AbstractPage| - |selectItemInCustomDropdown| - Cannot select option in custom dropdowm: " + e.getMessage());
        }

    }


}