package com.parallel.test.ElementsTest;

import java.util.Locale;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.parallel.base.TestBase;
import com.parallel.pages.button.ButtonsPage;
import com.parallel.pages.elements.TextBoxPage;
import com.parallel.pages.home.HomePage;
import com.parallel.report.ExtentReport;
import com.parallel.report.Log;
import com.parallel.utils.JsonReader;
import com.parallel.utils.TestUtil;

public class TextBoxTest02 extends TestBase {

	static String TestDatafile = "Data";
	static String Testdata = "TextBoxTest";
	HomePage homepage;
	ButtonsPage buttonspage;
	TextBoxPage textBoxPage;

	
	// Here we are following Listener with Log file and create report in class level
	
	
	@BeforeClass
	@Parameters({ "browser", "url" })
	public void setup(String browser, String url) {
		initialization(browser, url);
		homepage = new HomePage();
		buttonspage = new ButtonsPage();
		textBoxPage = new TextBoxPage();
	}

	@Test(priority = 0, description = "To verify Text box page")
	public void navigateToElemenPage() {
		ExtentReport.createTest("Tools QA Testbox Page Validation");
		try {
			homepage.chooseHomePageMenu("Elements");
			buttonspage.chooseElementsMenu("Text Box");
			TestUtil.waitForSec(4);
			boolean contains = getDriver().getCurrentUrl().contains("text-box");
			Assert.assertTrue(contains, "User is not in TextBox page");
			Log.pass("User is in TextBox page");
		} catch (Exception e) {
			Log.fail("User is not in Text box page " + e, true);
		}
	}

	@Test(priority = 1, description = "To verify Text box page response")
	public void enterText() throws ParseException {
		try {
			String f1 = JsonReader.getJSONobj(TestDatafile, Testdata, "fullName");
			String eMail = JsonReader.getJSONobj(TestDatafile, Testdata, "username");
			String cAdd = JsonReader.getJSONobj(TestDatafile, Testdata, "cityName");
			String pAdd = JsonReader.getJSONobj(TestDatafile, Testdata, "fullAddress");
			String domain = "@test.com";

			textBoxPage.enterFullName(f1);
			textBoxPage.enterMail(eMail.toLowerCase(Locale.ROOT) + domain);
			textBoxPage.enterCAddress(cAdd);
			textBoxPage.enterPAddress(pAdd);
			TestUtil.scrollToBottom(getDriver());
			textBoxPage.clickSubmit();

			TestUtil.waitForSec(4);

			Assert.assertEquals(textBoxPage.assertionResponse("Name"), "Name:" + f1,
					"Name in response is not matching");
			Assert.assertEquals(textBoxPage.assertionResponse("Email"), "Email:" + eMail + domain,
					"Email in response is not matching");
			Assert.assertEquals(textBoxPage.assertionResponse("Current Address"), "Current Address :" + cAdd,
					"Current Address in response is not matching");
			Assert.assertEquals(textBoxPage.assertionResponse("Permanent Address"), "Permananet Address :" + pAdd,
					"Permananet Address in response is not matching");
			Log.pass("Text box page response is verified successfully");
		} catch (Exception e) {
			Log.fail("Text box page response is verification failed", true);
		}
	}

	@AfterClass
	public void teardown() {
		super.tearDown();
	}
}
