package com.parallel.test.ElementsTest;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.parallel.base.TestBase;
import com.parallel.pages.button.ButtonsPage;
import com.parallel.pages.elements.RadioButtonPage;
import com.parallel.pages.elements.TextBoxPage;
import com.parallel.pages.home.HomePage;
import com.parallel.report.ExtentReport;
import com.parallel.report.Log;
import com.parallel.utils.TestUtil;

public class RadioButtonTest extends TestBase {

	HomePage homepage;
	ButtonsPage buttonspage;
	RadioButtonPage radioButtonPage;

	// Here we are following Listener with Log file and create report in class level

	@BeforeClass
	@Parameters({ "browser", "url" })
	public void setup(String browser, String url) {
		initialization(browser, url);
		homepage = new HomePage();
		buttonspage = new ButtonsPage();
		radioButtonPage = new RadioButtonPage();
	}

	@Test(priority = -1)
	public void testSelectedYesButton() {
		ExtentReport.createTest("Tools QA radiobutton Page Validation");
		try {
			homepage.chooseHomePageMenu("Elements");
			buttonspage.chooseElementsMenu("Radio Button");
			boolean contains = getDriver().getCurrentUrl().contains("radio-button");
			Assert.assertTrue(contains, "User is not in TextBox page");
			TestUtil.waitForSec(5);
			radioButtonPage.clickButton(0);
			Assert.assertEquals(radioButtonPage.responseRadioButton(), "You have selected Yes");
			Log.pass("User selected Yes radio button");
		} catch (Exception e) {
			Log.fail("Yes radio button not selected" + e, true);
		}
	}

	@Test
	public void testSelectedImpressiveButton() {
		try {
			radioButtonPage.clickButton(1);
			Assert.assertEquals(radioButtonPage.responseRadioButton(), "You have selected Impressive"	);
			Log.pass("User selected Impressive radio button");
		} catch (Exception e) {
			Log.fail("Impressive radio button not selected" + e, true);
		}
	}
	
	@AfterClass
	public void teardown() {
		super.tearDown();
	}

}
