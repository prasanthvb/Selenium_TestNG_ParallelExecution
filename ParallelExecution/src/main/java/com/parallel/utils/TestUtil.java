package com.parallel.utils;

import static java.time.Duration.ofSeconds;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.parallel.base.TestBase;

public class TestUtil extends TestBase {
	public static Actions actions;
	public static Select select;
	public static Alert alert;

	public static JavascriptExecutor javaScript;

	public static String downloadPath = "";

	public static WebElement element(WebDriver driver, By locator) {
		return new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static List<WebElement> elements(WebDriver driver, By locator) {
		return new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(locator)));
	}

	public static int arrayListToInt(String[] items, String list) {
		return ArrayUtils.indexOf(items, list);
	}

	// To Switch into a Frame using Name.
	public void switchToFrame(String frameName) {
		try {
			getDriver().switchTo().frame(frameName);
			System.out.println("Navigated to Frame with Name ::: " + frameName);
		} catch (NoSuchFrameException e) {
			System.out.println("Unable to Locate Frame with Name ::: " + frameName + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to Navigate to Frame with Name ::: " + frameName + e.getStackTrace());
		}
	}

	public static void waitForSec(int seconds) {
		try {
			long time = seconds * 1000;
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/* To scroll to bottom of the page */
	public static void scrollToBottom(WebDriver driver) {

		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

	}

	/* To Scroll to mid of the page */
	public static void scrollToMid(WebDriver driver) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,200)");
		waitForSec(10);
	}

	/* To mouse hover on a element */
	public static WebElement moveTo(WebElement element) {
		Actions actions = new Actions(getDriver());
		actions.moveToElement(element).build().perform();
		return element;
	}

	public static By moveTo(WebDriver driver, By element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(findElement(driver, element)).build().perform();
		return element;
	}

	// To Switch into a Frame using Index.
	public void switchToFrame(int frame) {
		try {
			getDriver().switchTo().frame(frame);
			System.out.println("Navigated to Frame with Index ::: " + frame);
		} catch (NoSuchFrameException e) {
			System.out.println("Unable to Locate Frame with Index ::: " + frame + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to Navigate to Frame with Index ::: " + frame + e.getStackTrace());
		}
	}

	// To Switch into a Frame using Index.
	public static void switchToFrame(WebDriver driver, int frame) {
		try {
			driver.switchTo().frame(frame);
			System.out.println("Navigated to Frame with Index ::: " + frame);
		} catch (NoSuchFrameException e) {
			System.out.println("Unable to Locate Frame with Index ::: " + frame + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to Navigate to Frame with Index ::: " + frame + e.getStackTrace());
		}
	}

	// To Take Screenshot at End Of Test.
	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(currentDir + "/Screenshots/" + System.currentTimeMillis() + ".png"));
	}

	// Explicit Wait to Click on WebElement.
	public static void clickOn(WebDriver driver, By element) {
		scrollTillElementVisible(driver, element);
		new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.elementToBeClickable(driver.findElement(element)));
		findElement(driver, element).click();
	}

	/* Required */
	// Explicit Wait to Click on WebElement.
	public static void clickOn(WebDriver driver, WebElement element) {
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	// Zoom in and Zoom out Application
	public static void zoomInAndOut(WebDriver driver, String size) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.style.zoom='" + size + "%'");
	}

	// Explicit Wait to Click on WebElement.
	public static void javaScriptClick(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	public static void javaScriptClick(WebDriver driver, By element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", findElement(driver, element));
	}

	// Click with index
	public static void clickWithIndex(WebDriver driver, By locator, int index) {
		elements(driver, locator).get(index).click();
	}

	// Actions click with index
	public static void clickActionsWithIndex(WebDriver driver, By locator, int i) {
		Actions actions = new Actions(driver);
		actions.click(elements(driver, locator).get(i)).build().perform();
	}

	public static List<String> convertToListOfString(List<WebElement> lst) {
		List<String> str = new ArrayList<String>();
		for (WebElement e : lst) {
			str.add(e.getText());
		}
		return str;
	}

	// Explicit Wait to Send Data to WebElement.
	public static void sendKeys(WebDriver driver, WebElement element, String value) {
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(element));
		element.sendKeys(value);
	}

	public static void sendKeys(WebDriver driver, By element, String value) {
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOf(findElement(driver, element)));
		findElement(driver, element).sendKeys(value);
	}

	public static void clearValue(WebDriver driver, By element) {
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOf(findElement(driver, element)));
		findElement(driver, element).clear();
	}

	public static void clearAndSendKeys(WebDriver driver, By element, String value) {
		clearValue(driver, element);
		findElement(driver, element).sendKeys(value);
	}

	// Explicit Wait for Element To Be Visible.
	public static void waitForElementToBeVisible(WebDriver driver, WebElement locator) {
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(locator));
	}

	// Explicit Wait for Element To Be Visible.
	public static void waitForElementToBeVisible(WebDriver driver, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
	}

	// Explicit Wait for all Element To Be Visible.
	public static void waitForAllElementToBeVisible(WebDriver driver, WebElement locator) {
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOfAllElements(locator));
	}

	public static void ScrollDownToMid(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 300)");
	}

	public static void waitForAllElementToBeVisible(WebDriver driver, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(60))
				.until(ExpectedConditions.visibilityOfAllElements(driver.findElement(locator)));
	}

	// Explicit Wait for Number of Elements To Be Visible.
	public static void waitForNumberOfElementsToBeVisible(WebDriver driver, By locator, String numberofEelements) {
		new WebDriverWait(driver, Duration.ofSeconds(30))
				.until(ExpectedConditions.numberOfElementsToBe(locator, Integer.valueOf(numberofEelements)));
	}

	/* Required */
	public static String getTextValue(WebDriver driver, By locator) {
		TestUtil.waitForElementToBeVisible(driver, driver.findElement(locator));
		return driver.findElement(locator).getText();
	}

	public static String getTextValue(WebDriver driver, WebElement locator) {
		TestUtil.waitForElementToBeVisible(driver, locator);
		return locator.getText();
	}
	
	public static String getText(WebDriver driver, By locator, int i) {	
		return elements(driver, locator).get(i).getText();
	}

	// Explicit Wait for Element To Be Present on screen.
	public static void waitForpresenceOfElementLocated(WebDriver driver, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public static void waitForpresenceOfElementLocated(WebDriver driver, By locator, int timeOutInSec) {
		new WebDriverWait(driver, Duration.ofSeconds(timeOutInSec))
				.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	// Explicit Wait for Element To Be Clickable.
	public static void waitForElementToBeClickable(WebDriver driver, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(120)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	public static void waitForElementToBeClickable(WebDriver driver, WebElement locator) {
		new WebDriverWait(driver, Duration.ofSeconds(120)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	public static boolean isDisplayed(WebDriver driver, By element) {
		try {
			TestUtil.waitForElementToBeVisible(driver, element);
			return driver.findElement(element).isDisplayed();
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	public static boolean isDisplayed(WebDriver driver, WebElement element) {
		try {
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static void pageLoadWait() {
		try {
			TimeUnit.MILLISECONDS.sleep(3000);
		} catch (InterruptedException e) {
		}
	}

	public static void pageLoadShortWait() {
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
		}
	}

	public static void pageLoadLongWait() {
		try {
			TimeUnit.MILLISECONDS.sleep(10000);
		} catch (InterruptedException e) {
		}
	}

	// To Check Element is Displayed or No.
	public static boolean isElementDisplayed(By locator, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return element.isDisplayed();
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	// To Check Element is Enabled or No.
	public static void isElementEnabled(WebElement element) {
		boolean elementEnabled = element.isEnabled();
		if (elementEnabled) {
			System.out.println("Element is Enabled");
		} else {
			System.out.println("Element is not Enabled");
		}
	}

	// To Select a value from Drop Down by using SelectByVisibleText Method.
	public static void selectValueFromDropDownByText(WebElement element, String value) {
		select = new Select(element);
		select.selectByVisibleText(value);
	}

	public static void selectValueFromDropDownByText(WebDriver driver, By element, String value) {
		select = new Select(driver.findElement(element));
		select.selectByVisibleText(value);
	}

	// To Select a value from Drop Down by using SelectByIndex Method.
	public static void selectValueFromDropDownByIndex(WebElement element, int value) {
		select = new Select(element);
		select.selectByIndex(value);
	}

	public static void selectValueFromDropDownByIndex(WebDriver driver, By element, int value) {
		select = new Select(driver.findElement(element));
		select.selectByIndex(value);
	}

	// To Select a value from Drop Down by using SelectByValue Method.
	public static void selectValueFromDropDownByValue(WebElement element, String value) {
		select = new Select(element);
		select.selectByValue(value);
	}

	public static void selectValueFromDropDownByValue(WebDriver driver, By element, String value) {
		select = new Select(findElement(driver, element));
		select.selectByValue(value);
	}

	// To Print all Values and Select a Required Value from Drop Down.
	public static void selectDropDownValue(WebDriver driver, String xpathValue, String value) {
		List<WebElement> monthList = driver.findElements(By.xpath(xpathValue));
		for (WebElement webElement : monthList) {
			if (webElement.getText().contains(value)) {
				webElement.click();
				break;
			}
		}
	}

	public static void selectDropDownParticularValue(WebDriver driver, String xpathValue, String value) {
		List<WebElement> monthList = driver.findElements(By.xpath(xpathValue));
		for (WebElement webElement : monthList) {
			if (webElement.getText().equals(value)) {
				webElement.click();
				break;
			}
		}
	}

	public static void selectDropDownValuebylist(List<WebElement> dropdown, String value) {
		for (int i = 0; i < dropdown.size(); i++) {
			if (dropdown.get(i).getText().equals(value)) {
				dropdown.get(i).click();
				break;
			}
		}
	}

	public static void selectDropDownValuebylist(WebDriver driver, By dropdown, String value) {
		for (int i = 0; i < driver.findElements(dropdown).size(); i++) {
			if (driver.findElements(dropdown).get(i).getText().equalsIgnoreCase(value)) {
				driver.findElements(dropdown).get(i).click();
				break;
			}
		}
	}

	public static void selectDropDownValuebyIndex(WebDriver driver, By dropdown, int index) {
		for (int i = 0; i < driver.findElements(dropdown).size(); i++) {
			if ((i + 1) == index) {
				driver.findElements(dropdown).get(i).click();
				break;
			}
		}
	}

	public static WebElement getTheTextOfOptionSelectedFromDropdown(WebElement element) {
		select = new Select(element);
		return select.getFirstSelectedOption();
	}

	public static WebElement getTheTextOfOptionSelectedFromDropdown(WebDriver driver, By element) {
		Select select = new Select(driver.findElement(element));
		return select.getFirstSelectedOption();
	}

	public static List<WebElement> getAllOptionsFromDropdown(WebDriver driver, By element) {
		Select select = new Select(TestUtil.findElement(driver, element));
		return select.getOptions();
	}

	// To Validate Drop Down Values.
	public static List<String> dropDownValuesValidation(WebElement element) {
		Select select = new Select(element);
		List<WebElement> dropDownValues = select.getOptions();
		List<String> toolsDropDownValues = new ArrayList<String>();
		for (WebElement listOfDropDownValues : dropDownValues) {
			toolsDropDownValues.add(listOfDropDownValues.getText());
		}
		return toolsDropDownValues;
	}

	public static List<String> dropDownValuesValidation(WebDriver driver, By element) {
		Select select = new Select(driver.findElement(element));
		List<WebElement> dropDownValues = select.getOptions();
		List<String> toolsDropDownValues = new ArrayList<String>();
		for (WebElement listOfDropDownValues : dropDownValues) {
			toolsDropDownValues.add(listOfDropDownValues.getText());
		}
		return toolsDropDownValues;
	}

	// To Select Radio Button.
	public void selectRadioButton(List<WebElement> element, String value) {
		for (WebElement elements : element) {
			if (elements.getText().equalsIgnoreCase(value)) {
				elements.click();
				break;
			}
		}
	}

	// To Accept Alert Pop-Up.
	public static void acceptAlertPopup() throws InterruptedException {
		try {
			alert = getDriver().switchTo().alert();
			Thread.sleep(2000);
			alert.accept();
		} catch (Exception e) {
			System.out.println("Something Went Wrong ==>> Please Check ::: " + e.getMessage());
		}
	}

	// To Dismiss Alert Pop-Up.
	public static void dismissAlertPopup() throws InterruptedException {
		try {
			alert = getDriver().switchTo().alert();
			Thread.sleep(2000);
			alert.dismiss();
		} catch (Exception e) {
			System.out.println("Something Went Wrong ==>> Please Check ::: " + e.getMessage());
		}
	}

	// To Match Value with List of Elements and Click on it.
	public void clickOnMatchingValue(List<WebElement> listOfElements, String valueToBeMatched) {
		for (WebElement element : listOfElements) {
			if (element.getText().equalsIgnoreCase(valueToBeMatched)) {
				element.click();
				return;
			}
		}
	}

	// To Click on Element using Actions Class.
	public static void clickOnElementUsingActions(WebElement element) {
		actions = new Actions(getDriver());
		actions.moveToElement(element).click().perform();
	}

	// To Mouse Hover and Click or Select an Element using Actions Class.
	public static void moveToElement(WebDriver driver, WebElement element) {
		actions = new Actions(driver);
		actions.moveToElement(element).build().perform();
	}

	// To Mouse Hover and Click or Select an Element using Actions Class.
	public static void moveToElement(WebDriver driver, By element) {
		actions = new Actions(driver);
		actions.moveToElement(driver.findElement(element)).build().perform();
	}

	// To Perform Drag and Drop action using Actions Class - 1.
	public static void dragAndDrop_1(WebDriver driver, WebElement sourceElement, WebElement destinationElement) {
		actions = new Actions(driver);
		actions.dragAndDrop(sourceElement, destinationElement).pause(ofSeconds(2)).release().build().perform();
	}

	// To Perform Drag and Drop action using Actions Class - 2.
	public static void dragAndDrop_2(WebDriver driver, WebElement sourceElement, WebElement destinationElement) {
		actions = new Actions(driver);
		actions.clickAndHold(sourceElement).pause(ofSeconds(2)).moveToElement(destinationElement).pause(ofSeconds(2))
				.release().build().perform();
	}

	// To Perform Right Click action using Actions Class.
	public static void rightClick(WebDriver driver, By locator) {
		actions = new Actions(driver);
		actions.contextClick(element(driver, locator)).build().perform();
	}

	// To perform Double Click action using Actions Class.
	public static void doubleClick(WebDriver driver, By locator) {
		actions = new Actions(driver);
		actions.doubleClick(element(driver, locator)).build().perform();
	}

	// Extent Report - 1.
	public static String getSystemDate() {
		DateFormat dateFormat = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	// Extent Report - 2.
	public static String getScreenshot(WebDriver driver, String fileNAme) throws IOException {
		String dateName = new SimpleDateFormat("_ddMMyyyy_HHmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/TestScreenshots/" + fileNAme + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	// Set Date For Log4J.
	public static void setDateForLog4j() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		System.setProperty("current_date", dateFormat.format(new Date()));
	}

	public static String randomStringGenerator() {
		UUID randomUUID = UUID.randomUUID();
		String randomString = randomUUID.toString().replaceAll("-", "");
		return randomString.substring(0, 8);
	}

	// Generates a random n-digit number
	public static int randomNumberGenerator(int n) {
		Random no = new Random();
		int origin = (int) Math.pow(10, n - 1);
		int boundMinusOrigin = (int) (Math.pow(10, n) - Math.pow(10, n - 1) - 1);
		return (origin + no.nextInt(boundMinusOrigin));
	}

	public static void pageScrollDown(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, document.body.scrollHeight)");
	}

	public static void pageScrollDownToMid(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 250)");
	}

	public static void scrollDownToMidPage(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 400)");
	}

	public static void pageScrollUp(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, -document.body.scrollHeight)");
	}

	public static void ScrollToTop(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}

	public static void pageScrollLeft(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(-500, 0)");
	}

	public static void pageScrollright(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(500, 0)");
	}

	public static String getscreenBase64(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}

	public static void verifyDownloadedFile(String fileName) {
		File[] totalFiles = folder.listFiles();
		for (File file : totalFiles) {
			Assert.assertTrue(file.getName().matches(fileName));
			file.delete();
		}
	}

	public static void deleteDownloadfolder() throws IOException {
		for (File file : folder.listFiles()) {
			file.delete();
		}
		folder.delete();
	}

	public void closeBottomPopUp() throws AWTException {
		Robot robot = new Robot();
		// press key Alt+S
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_S);
		// relase key Alt+S
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_S);
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void closeWindow() throws AWTException {
		Robot robot = new Robot();
		// press key Alt+F4
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_F4);
		// relase key Alt+F4
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_F4);
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void openViewDownLoads() throws AWTException {
		Robot robot = new Robot();
		// press key CTRL+J
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_J);
		// relase key CTRL+J
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_J);
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void clearViewDownLoads() throws AWTException {
		Robot robot = new Robot();
		// press key Alt+L
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_L);
		// relase key Alt+L
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_L);
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void closeViewDownLoads() throws AWTException {
		Robot robot = new Robot();
		// press key Alt+C
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(100);
		robot.keyPress(KeyEvent.VK_C);
		// relase key Alt+C
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_C);
		robot.delay(100);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void clickOnEnter() throws AWTException {
		Robot robot = new Robot();
		// press key Alt+C
		robot.keyPress(KeyEvent.VK_ENTER);
	}

	public static void clickTab() {
		Actions act = new Actions(getDriver());
		act.sendKeys(Keys.TAB).build().perform();
	}

	public void ResumePageLoading(int seconds) throws InterruptedException {
		Thread.sleep(seconds);
	}
	// This method open the report automatically.

	public static void openReport() {
		File htmlFile = new File(FrameworkConstant.EDIT_EXTENTREPORT_PATH);
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method for generic file path for upload file
	public static void fileUpload(WebDriver driver, String filePath, By element) {
		File file = new File(filePath);
		driver.findElement(element).sendKeys(file.getAbsolutePath());
	}

	public static void fileUpload(WebDriver driver, String filePath, WebElement element) {
		File file = new File(filePath);
		sendKeys(driver, element, file.getAbsolutePath());
	}

	// This method for close print window//
	public static void closePrintWindow() throws AWTException {
		Robot robotObject = new Robot();
		robotObject.keyPress(KeyEvent.VK_ESCAPE);
		robotObject.keyRelease(KeyEvent.VK_ESCAPE);
	}

	public static void closeNextTab() {
		ArrayList<String> switchTabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(switchTabs.get(1));
		getDriver().close();
		getDriver().switchTo().window(switchTabs.get(0));
	}

	public static File getLatestFilefromDir(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
		return lastModifiedFile;
	}

	public static String getDownloadPath() {
		String home = System.getProperty("user.home");
		downloadPath = home + "/Downloads";
		return downloadPath;
	}

	public static void isPageTitle(String expcted, String actual) {
		Assert.assertEquals(actual, expcted, "Page Title Mismatch");
	}

	public static void isPageUrl(String expcted) {
		Assert.assertEquals(getDriver().getCurrentUrl(), prop.getProperty("URL") + expcted,
				"Verify the page URL matches the expected");
	}

	public static void resultTableVali(List<WebElement> resultTableHeader, List<WebElement> resultTableRows) {
		TestUtil.waitForElementToBeVisible(getDriver(), resultTableHeader.get(0));
		List<String> allHeaderNames = new ArrayList<String>();
		for (WebElement header : resultTableHeader) {
			String headerName = header.getText();
			allHeaderNames.add(headerName);
		}
		List<LinkedHashMap<String, String>> allTableData = new ArrayList<LinkedHashMap<String, String>>();

		for (int i = 1; i <= resultTableRows.size(); i++) {
			List<WebElement> allColumnsEle = resultTableRows.get(i - 1).findElements(By.tagName("td"));
			LinkedHashMap<String, String> eachRowData = new LinkedHashMap<>();
			for (int j = 0; j < allColumnsEle.size(); j++) {

				String cellValue = allColumnsEle.get(j).getText();
				String hed = allHeaderNames.get(j);
				eachRowData.put(hed, cellValue);
			}
			allTableData.add(eachRowData);
		}
	}

	public static String decodeString(String text) {
		byte[] decodedString = Base64.decodeBase64(text);
		return (new String(decodedString));
	}

	public static String getFutureDate(int monthsToAdd) {
		LocalDate futureDate = LocalDate.now().plusMonths(monthsToAdd);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = futureDate.format(formatter);
		return dateString;
	}

	public static String getTodayDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = date.format(formatter);
		return formattedDate;
	}

	public static String generateTIN() {
		int charLength = 7;
		int tin = new Random().nextInt((int) Math.pow(10, charLength));
		return "98-" + String.format("%07d", tin);
	}

	public static void verifyURLContains(WebDriver driver, String expected) {
		String actual = driver.getCurrentUrl();
		Assert.assertTrue(actual.contains(expected), "verify URL contains - " + expected);
	}

	public static String randomWordGenerator(int length) {
		Random random = new Random();
		int lowerLimit = 97;
		char[] word = new char[length];
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) (lowerLimit + random.nextInt(26));
		}
		String randomString = new String(word);
		return randomString;
	}

	public static String getCurrentSystemDate() {
		return String.valueOf(LocalDate.now());
	}

	/**
	 * This method is to wait for the page to be loaded
	 */
	public static void waitForPageLoad() {

		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));

		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver wdriver) {
				return ((JavascriptExecutor) getDriver()).executeScript("return document.readyState")
						.equals("complete");
			}
		});

	}

	/**
	 * This method is to refresh the web page
	 */
	public static void refreshPage(WebDriver driver) {
		driver.navigate().refresh();
	}

	/**
	 * This method is to reload the web page with same url
	 */
	public static void reloadURL(WebDriver driver) {
		driver.get(driver.getCurrentUrl());
	}

	/**
	 * This method is to verify the display of particular element by waiting for the
	 * given seconds
	 *
	 * @param locator - xpath for the element to be verified
	 * @param timeout - int seconds - to be waited
	 * @return boolean true - if the element is displayed else - false
	 */
	public static boolean isElementDisplayed(String locator, int timeout) {
		try {
			WebElement element = getDriver().findElement(By.xpath(locator));
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * This method is to verify the display of particular element by waiting for the
	 * given seconds
	 *
	 * @param driver  - Webdriver object
	 * @param element - element to be verified
	 * @param timeout - int seconds - to be waited
	 * @return boolean true - if the element is displayed else - false
	 */
	public static boolean waitUntilElementDisplayed(WebDriver driver, By element, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(element)));
			return driver.findElement(element).isDisplayed();
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	public static boolean waitUntilElementDisplayed(WebDriver driver, WebElement element, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.isDisplayed();
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	/**
	 * This method is to wait for the given seconds until the particular element
	 * disappears
	 *
	 * @param element - web element to be focused
	 * @param timeout - int seconds - to be waited
	 * @return boolean - true if the element is not available else false
	 */
	public static boolean waitUntilElementDisappears(WebDriver driver, By element, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.invisibilityOf(findElement(driver, element)));
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	public static boolean waitUntilElementDisappears(WebDriver driver, WebElement element, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.invisibilityOf(element));
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	/**
	 * This method is to scroll until the particular element is visible
	 *
	 * @param element - element up to which the scroll to be happened
	 */
	public static void scrollTillElementVisible(WebElement element) {
		try {
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (NoSuchElementException e) {
			e.getClass();
		}
	}

	public static void scrollTillElementVisible(WebDriver driver, By element) {
		try {
			((JavascriptExecutor) getDriver()).executeScript(
					"arguments[0].scrollIntoView({behavior : 'smooth', block : 'center', inline: 'center'});",
					getDriver().findElement(element));
		} catch (NoSuchElementException e) {
			e.getClass();
		}
	}

	/**
	 * This method is to wait implicit before throwing no such element exception
	 *
	 * @param timeout - int timeout value
	 */
	public static void implicitWait(int timeout) {
		try {
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
		} catch (NoSuchElementException e) {
			e.getClass();
		}
	}

	/**
	 * This method is to get the current date and time in IST format
	 *
	 * @return String - current Date Time in IST format
	 */
	public static String getCurrentDateTimeInIST() {
		String expectedDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = new Date();
			expectedDate = sdf.format(date);
			Date date1 = sdf.parse(expectedDate);
			sdf.setTimeZone(TimeZone.getTimeZone("IST"));
			expectedDate = sdf.format(date1);
		} catch (java.text.ParseException e) {
			e.getClass();
			return null;
		}
		return expectedDate;
	}

	/**
	 * This method is to get the current date and time of OS
	 *
	 * @return String - current Date Time of OS
	 */
	public static String getCurrentDateTimeDefault() {
		String expectedDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = new Date();
			expectedDate = sdf.format(date);
			Date date1 = sdf.parse(expectedDate);
			sdf.setTimeZone(TimeZone.getDefault());
			expectedDate = sdf.format(date1);
		} catch (java.text.ParseException e) {
			e.getClass();
			return null;
		}
		return expectedDate;

	}

	/**
	 * This method is to navigate back to the previous page
	 */
	public static void navigateBack(WebDriver driver) {
		driver.navigate().back();
	}

	public static String timestamp() {
		return new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss").format(new Date());
	}

	/**
	 * This method is to get the given attribute value for the given locator
	 *
	 * @param locator       - web element or locator to be used
	 * @param attributeName - attribute name for which the value to retrieved
	 * @return String - attribute value based on the attribute name
	 */
	public static String getAttributeValue(WebDriver driver, WebElement locator, String attributeName) {
		TestUtil.waitForElementToBeVisible(driver, locator);
		return locator.getDomAttribute(attributeName);
	}

	public static String getAttributeValue(WebDriver driver, By locator, String attributeName) {
		TestUtil.waitForElementToBeVisible(driver, findElement(driver, locator));
		return findElement(driver, locator).getDomAttribute(attributeName);
	}

	/**
	 * This method is to verify the display of list web elements by waiting for the
	 * given seconds
	 *
	 * @param timeout - int seconds - to be waited
	 * @return boolean true - if the elements are displayed else - false
	 */
	public static boolean waitUntilElementsDisplayed(WebDriver driver, List<WebElement> elements, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.visibilityOfAllElements(elements));
			for (WebElement element : elements) {
				if (!element.isDisplayed()) {
					return false;
				}
			}
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * This method is to switch to the parent tab and close all child tabs
	 * 
	 * @param parentTab - parent tab
	 */
	public static void SwitchtoParentTab(String parentTab) {
		Set<String> windowHandles = getDriver().getWindowHandles();
		for (String windows : windowHandles) {
			if (!windows.equals(parentTab)) {
				getDriver().switchTo().window(windows);
				try {
					getDriver().close();
				} catch (Exception NosuchWindowException) {
				}
			}
		}
		System.out.println("Parent Tab: " + parentTab);
		getDriver().switchTo().window(parentTab);
		waitForSec(2);
	}

	/**
	 * This method is to switch to the child tab
	 * 
	 * @param parentTab - parent tab
	 */
	public static void SwitchtoChildTab(String parentTab) {
		Set<String> windowHandles = getDriver().getWindowHandles();
		for (String windows : windowHandles) {
			if (!windows.equals(parentTab)) {
				getDriver().switchTo().window(windows);
			}
		}
	}

	/**
	 * This method is to switch to the child tab based on the title when there are
	 * more than one child tabs
	 * 
	 * @param parentTab - parent tab
	 * @param title     - title of the child tab
	 */
	public static void SwitchtoChildTab(String parentTab, String title) {
		Set<String> windowHandles = getDriver().getWindowHandles();
		for (String windows : windowHandles) {
			if (!windows.equals(parentTab)) {
				getDriver().switchTo().window(windows);
				if (getDriver().getTitle().equalsIgnoreCase(title)) {
					break;
				}
			}
		}
	}

	// To Handle Multiple Windows or Switch Between Multiple Windows.
	public static void switchWindow(WebDriver driver, String firstWindow, String secondWindow) {
		Set<String> windowHandles = driver.getWindowHandles();
		for (String windows : windowHandles) {
			if (!windows.equals(firstWindow) && !windows.equals(secondWindow)) {
				driver.switchTo().window(windows);
			}
		}
	}

	public static void switchToNextTab(WebDriver driver) {
		String firstWindow = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();
		for (String windows : windowHandles) {
			if (!windows.equals(firstWindow)) {
				driver.switchTo().window(windows);
			}
		}
	}

	/**
	 * This method is to verify the url contains the expected value
	 * 
	 * @param expctedUrl - expected url
	 */
	public static void verifyURL(String expctedUrl) {
		String currentUrl = getDriver().getCurrentUrl();
		Assert.assertTrue(currentUrl.contains(expctedUrl), "Url mismatch");
	}

	/**
	 * This method is to open a new tab
	 */
	public static void openNewTab() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.open('')");
	}

	/**
	 * This method is to get the past date
	 *
	 * @param daysToLess - days to less from current date
	 * @return
	 */
	public static String getPastDate(int daysToLess) {
		LocalDate futureDate = LocalDate.now().minusDays(daysToLess);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = futureDate.format(formatter);
		return dateString;
	}

	/**
	 * This method is to verify the web element is enabled or not
	 *
	 * @param element - element to be verified
	 * @return boolean - true if the element is enabled else false
	 */
	public static boolean isEnabled(WebElement element) {
		try {
			return element.isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static boolean isEnabled(WebDriver driver, By element) {
		try {
			return driver.findElement(element).isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static boolean isSelected(WebElement element) {
		return element.isSelected();
	}

	public static boolean isSelected(WebDriver driver, By element) {
		return driver.findElement(element).isSelected();
	}

	/**
	 * This method is used to clear and send values to an input field
	 *
	 * @param element - field for which the value to be passed
	 * @param value   - value to be used
	 */
	public static void sendKeys(WebElement element, String value) {
		new WebDriverWait(getDriver(), Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(element));
		element.clear();
		element.sendKeys(value);
	}

	public static void waitForElementToBeClickable(WebDriver driver, int timeout, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	public static String getRandomNumberBetween(long start, long end) {
		Random random = new Random();
		long range = end - start + 1;
		long fraction = (long) (range * random.nextDouble());
		long randomNumber = fraction + start;
		return String.valueOf(randomNumber);
	}

	/**
	 * This method is used to find element in the page
	 *
	 * @param element - field for which the value to be passed
	 * @return element - returns webElement
	 */
	public static WebElement findElement(WebDriver driver, By element) {
		waitForElementToBeVisible(driver, element);
		return (driver.findElement(element));
	}

	/**
	 * This method is used to find elements in the page
	 *
	 * @param element - field for which the value to be passed
	 * @return elements - returns webElement
	 */
	public static List<WebElement> findElements(WebDriver driver, By element) {
		waitForAllElementToBeVisible(driver, element);
		return (driver.findElements(element));
	}

	/**
	 * This method is used to get tagName of element in the page
	 * @param element - field for which the value to be passed
	 * @return String - tagName
	 */
	public static String getTagName(WebDriver driver, By element) {
		waitForElementToBeVisible(driver, element);
		return (driver.findElement(element).getTagName());
	}

	public static void waitForElementToBeVisible(WebDriver driver, int timeoutInSec, By locator) {
		new WebDriverWait(driver, Duration.ofSeconds(timeoutInSec))
				.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
	}

	public static String dateAndTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}

	public static String generateRandomEmail(int length) {
		String alphabets = "abcdefghijklmnopqrstuvwxyz";
		String allowedChars = alphabets + "1234567890" + "_-.";
		int lenOfAllowedChars = allowedChars.length();
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(length);
		sb.append(alphabets.charAt(rand.nextInt(alphabets.length())));
		int index = 0;
		for (int i = 0; i < length - 1; i++) {
			index = rand.nextInt(lenOfAllowedChars);
			sb.append(allowedChars.charAt(index));
		}
		return sb.toString() + "@testremitra.com";
	}

	public static String generateRandomTestNames(int length) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return "Test User " + sb.toString();
	}

	public static void handleElements(List<WebElement> elements, int index) {
		if (elements.size() == 1) {
			WebElement singleElement = elements.get(0);
			singleElement.click();
		} else if (elements.size() > 1) {
			WebElement element = elements.get(index);
			element.click();
		}
	}

	public static String returnTheMessageToBeGiven(String existingMessage, String messageForCurrentScenario) {
		if (existingMessage.isEmpty())
			return messageForCurrentScenario;
		else
			return (existingMessage + "." + messageForCurrentScenario);
	}

	/**
	 * Mimics basic actions of clicking and entering value into a field. This
	 * verifies if a field can be successfully interacted with. This method can also
	 * be used to enter value into a field by clearing the existing value. This is
	 * useful when editing.
	 *
	 * @param driver
	 * @param element
	 * @param valueToEnter
	 * @return
	 */
	public static boolean verifyInputFieldByEnteringValue(WebDriver driver, By element, String valueToEnter) {
		if (!isDisplayed(getDriver(), element))
			return false;
		try {
			clickClearAndEnter(driver, element, valueToEnter);
			TestUtil.clearValue(getDriver(), element);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String extractUrlFromOnclick(String onclickAttribute) {
		String regex = "copySelfRegisterLink\\('(.*?)'\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(onclickAttribute);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	public static void clickClearAndEnter(WebDriver driver, By element, String valueToEnter) {
		clickOn(getDriver(), element);
		clearValue(getDriver(), element);
		sendKeys(driver, element, valueToEnter);
	}

	/**
	 * Mimics basic actions of clicking and selecting value from dropdown list. This
	 * verifies if a field can be successfully interacted with. This method can also
	 * be used to enter value into a field by clearing the existing value. This is
	 * useful when editing.
	 *
	 * @param driver
	 * @param element
	 * @return
	 */
	public static boolean verifyDropDownFieldBySelectingValue(WebDriver driver, By element, String valueToSelect) {
		if (!isDisplayed(getDriver(), element))
			return false;
		clickOn(getDriver(), element);
		selectValueFromDropDownByText(driver, element, valueToSelect);
		return true;
	}

	public static boolean verifyFieldsMultiple(WebDriver driver, String elementType, Map<By, String> elementValuePair) {
		if (elementType.equalsIgnoreCase("dropdown")) {
			for (Map.Entry<By, String> entry : elementValuePair.entrySet()) {
				if (!verifyDropDownFieldBySelectingValue(driver, entry.getKey(), entry.getValue()))
					return false;
			}
		} else {
			for (Map.Entry<By, String> entry : elementValuePair.entrySet()) {
				if (!verifyInputFieldByEnteringValue(driver, entry.getKey(), entry.getValue()))
					return false;
			}
		}
		return true;
	}

	public static boolean noChangeVerification(String[] before, String[] after) {
		if (before.length == after.length) {
			for (int i = 0; i < before.length; i++) {
				if (!before[i].equals(after[i]))
					return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public static String getARandomValueFromDropDown(WebDriver driver, By element) {
		List<WebElement> options = getAllOptionsFromDropdown(driver, element);
		int index = Integer.parseInt(getRandomNumberBetween(0, options.size() - 1));
		return getTextValue(driver, options.get(index));
	}

	public static String copyTextToClipboardFromFieldAndGet(WebDriver driver, By field)
			throws IOException, UnsupportedFlavorException {
		clickOn(getDriver(), field);
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("a").sendKeys("c").keyUp(Keys.CONTROL).build().perform();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		return (String) clipboard.getData(DataFlavor.stringFlavor);
	}
}
