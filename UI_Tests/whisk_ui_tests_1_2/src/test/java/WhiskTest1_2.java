
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestNGListener;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.net.URI;

public class WhiskTest1_2 {

    private RemoteWebDriver driver;
    private String baseUrl = "http://my.whisk-dev.com";
    private StringBuffer verificationErrors = new StringBuffer();

    // Test Credentials for enter
    private String myEmail = "ptsla3@gmail.com";
    private String myPassword = "12345678qazW";

    // Item name
    private String itemName1 = "Tomato";
    private String itemName2 = "Cabbage";
    private String itemName3 = "Zucchini";
    private String itemName4 = "Fresh basil";
    private String itemName5 = "Lamb";

    // Locators of elements
    // Sign up page
    private String emailFieldLocator = "username";
    private String continueButtonLocator = "/html/body/div[2]/div/div[2]/div/div/div[2]/form/button/div/div";
    private String passwordClickLocator = "span.sc-9to9fk.cqsdLE > svg > use";
    private String passwordFieldLocator = "password";
    private String loginButtonLocator = "/html/body/div[2]/div/div[2]/div/div/div[2]/form/button";

    // Main page
    private String letsGetCookingButton = "/html/body/div[2]/div/div[2]/div/button/div/div";
    private String shoppingTab = "/html/body/div[1]/div[1]/nav[1]/div/div[4]/a";
    private String whiskLogo = "use";

    // Shopping Tab
    private String createNewListButton = "Create new list";
    private String createShoppingListButton = "/html/body/div[2]/div/div[2]/div[2]/form/div[2]/button[2]/div/div";
    private String addItemField = "/html/body/div[1]/div[2]/div[1]/div[2]/div[1]/div[2]/div/div/div/div[1]/input";
    private String addItemButton = "/html/body/div[1]/div[2]/div[1]/div[2]/div[1]/div[2]/div/div/div/div[2]/div/div[1]/div/div[2]/span";
    private String shoppingListLabel = "//div[@id='app']/div[2]/div/div[2]/div/div/h2";
    private String shoppingListOptionsButton = "button.sc-1s47cec.sc-wt4p2i.fFXDsB > svg > use";

    private String locatorItem1 = "//div[@id='app']/div[2]/div/div[2]/div[2]/div[5]/div/div/div/div[2]/div/div/div/div[2]/div/span";
    private String locatorItem2 = "//div[@id='app']/div[2]/div/div[2]/div[2]/div[3]/div/div/div/div[2]/div/div/div/div[2]/div/span";
    private String locatorItem3 = "//div[@id='app']/div[2]/div/div[2]/div[2]/div[4]/div/div/div/div[2]/div/div/div/div[2]/div/span";
    private String locatorItem4 = "//div[@id='app']/div[2]/div/div[2]/div[2]/div[2]/div/div/div/div[2]/div/div/div/div[2]/div/span";
    private String locatorItem5 = "//div[@id='app']/div[2]/div/div[2]/div[2]/div[7]/div/div/div/div[2]/div/div/div/div[2]/div/span";

    private String deleteListButton = "(//button[@type='button'])[5]";
    private String defaultListButton = "(//button[@type='button'])[4]";
    private String confirmDeleteButton = "//button[2]/div/div";
    private String listWasDeletedNotification = "//div[@id='app']/div[3]/div/div/div";

    private String shoppingList = "//div[@id='app']/div[2]/div/div/div/div/div";
    private String textDefaultShoppingList = "Shopping List";
    private String textDefaultList = "Default list";

    public static void main(String[] args) {

        System.out.printf("Whisk UI Test1 selenoid start");
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.addListener((ITestNGListener) tla);
        testng.run();

    }

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("84.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);

        driver = new RemoteWebDriver(
                URI.create("http://localhost:4444/wd/hub").toURL(),
                capabilities
        );

        //driver.manage().window().setSize(new Dimension(1920, 1080)); // to set any dimension for test
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
    }

    private void signIn() {

        driver.findElement(By.name(emailFieldLocator)).click();
        driver.findElement(By.name(emailFieldLocator)).clear();
        driver.findElement(By.name(emailFieldLocator)).sendKeys(myEmail); // enter user email
        driver.findElement(By.xpath(continueButtonLocator)).click(); // push "Continue" button
        driver.findElement(By.cssSelector(passwordClickLocator)).click();
        driver.findElement(By.name(passwordFieldLocator)).click();
        driver.findElement(By.name(passwordFieldLocator)).clear();
        driver.findElement(By.name(passwordFieldLocator)).sendKeys(myPassword); // enter password
        driver.findElement(By.xpath(loginButtonLocator)).click(); // push "Log in" button
        driver.findElement(By.xpath(letsGetCookingButton)).click(); // push OK in notification window

    }

    private void navigateToShoppingTab() {

        WebElement elementShoppingTabButton = (new WebDriverWait(driver, 100))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(shoppingTab))); // wait page load
        elementShoppingTabButton.click(); // go to shopping tab

    }

    private void createShoppingList() {

        driver.findElement(By.linkText(createNewListButton)).click(); // create new list
        driver.findElement(By.xpath(createShoppingListButton)).click(); // push create button

    }

    private void addItemsToList() {

        WebElement elementAddItemField = (new WebDriverWait(driver, 300))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(addItemField)));
        elementAddItemField.sendKeys(itemName1); // send Item1 name
        driver.findElement(By.xpath(addItemButton)).click(); // push add item
        elementAddItemField.sendKeys(itemName2); // send Item2 name
        driver.findElement(By.xpath(addItemButton)).click(); // push add item
        elementAddItemField.sendKeys(itemName3); // send Item3 name
        driver.findElement(By.xpath(addItemButton)).click();  // push add item
        elementAddItemField.sendKeys(itemName4); // send Item4 name
        driver.findElement(By.xpath(addItemButton)).click(); // push add item
        elementAddItemField.sendKeys(itemName5); // send Item5 name
        driver.findElement(By.xpath(addItemButton)).click(); // push add item

    }

    private void checkItemsByName() {

        WebElement elementItem1 = driver.findElement(By.xpath(locatorItem1));
        WebElement elementItem2 = driver.findElement(By.xpath(locatorItem2));
        WebElement elementItem3 = driver.findElement(By.xpath(locatorItem3));
        WebElement elementItem4 = driver.findElement(By.xpath(locatorItem4));
        WebElement elementItem5 = driver.findElement(By.xpath(locatorItem5));

        WebDriverWait waitElementItems = new WebDriverWait(driver, 20);
        waitElementItems.until(ExpectedConditions.textToBePresentInElement(elementItem1, itemName1)); // check that element1 name compare
        waitElementItems.until(ExpectedConditions.textToBePresentInElement(elementItem2, itemName2));
        waitElementItems.until(ExpectedConditions.textToBePresentInElement(elementItem3, itemName3));
        waitElementItems.until(ExpectedConditions.textToBePresentInElement(elementItem4, itemName4));
        waitElementItems.until(ExpectedConditions.textToBePresentInElement(elementItem5, itemName5));

    }

    private void deleteShoppingList() {

        driver.findElement(By.xpath(shoppingListLabel)).click();
        WebElement elementListOptionsButton = (new WebDriverWait(driver, 100))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(shoppingListOptionsButton)));
        elementListOptionsButton.click();

        driver.findElement(By.xpath(deleteListButton)).click();
        driver.findElement(By.xpath(confirmDeleteButton)).click(); // confirm deletion
        driver.findElement(By.xpath(listWasDeletedNotification)).click(); // "list was deleted" click notification

    }

    private void noUsersShoppingLists() {

        WebElement defaultShoppingList = driver.findElement(By.xpath(shoppingList));
        WebDriverWait waitShoppingListName = new WebDriverWait(driver, 20);
        waitShoppingListName.until(ExpectedConditions.textToBePresentInElement(defaultShoppingList, textDefaultShoppingList)); // wait for text to be present
        driver.findElement(By.cssSelector(shoppingListOptionsButton)).click(); // click to list options button
        WebElement elementDefaultListButton = driver.findElement(By.xpath(defaultListButton));
        WebDriverWait waitElementDefaultList = new WebDriverWait(driver, 50);
        waitElementDefaultList.until(ExpectedConditions.textToBePresentInElement(elementDefaultListButton, textDefaultList)); // wait for text to be present

    }


    @Test(priority = 1, description = "create new shopping list, add 5 items to list, check items")
    public void testWhisk1() {

        driver.get(baseUrl);
        signIn();
        navigateToShoppingTab();
        createShoppingList();
        addItemsToList();
        checkItemsByName();

    }

    @Test(priority = 2, description = "delete shopping list from test1")
    public void testWhisk2() {

        deleteShoppingList();
        navigateToShoppingTab();
        noUsersShoppingLists();

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}