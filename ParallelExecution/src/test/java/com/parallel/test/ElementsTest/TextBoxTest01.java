package com.parallel.test.ElementsTest;

import java.util.Locale;

import org.json.simple.parser.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.parallel.base.TestBase;
import com.parallel.pages.button.ButtonsPage;
import com.parallel.pages.elements.TextBoxPage;
import com.parallel.pages.home.HomePage;
import com.parallel.utils.JsonReader;
import com.parallel.utils.TestUtil;

public class TextBoxTest01 extends TestBase {

	static String TestDatafile = "Data";
	static String Testdata = "TextBoxTest";
	HomePage homepage;
	ButtonsPage buttonspage;
	TextBoxPage textBoxPage;
	
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
	homepage.chooseHomePageMenu("Elements");
	buttonspage.chooseElementsMenu("Text Box");
    TestUtil.waitForSec(4);
    boolean contains = getDriver().getCurrentUrl().contains("text-box");
    assertTestStep.assertTrue(contains, "User is in TextBox page");
	 }

	@Test(priority = 1, description = "To verify Text box page response")
	public void enterText() throws ParseException {
		String f1 = JsonReader.getJSONobj(TestDatafile, Testdata, "fullName");
		String eMail = JsonReader.getJSONobj(TestDatafile, Testdata, "username");
		String cAdd = JsonReader.getJSONobj(TestDatafile, Testdata, "cityName");
		String pAdd = JsonReader.getJSONobj(TestDatafile, Testdata, "fullAddress");
		String domain = "@test.com";

		textBoxPage.enterFullName(f1);
		textBoxPage.enterMail(eMail.toLowerCase(Locale.ROOT)+domain);
		textBoxPage.enterCAddress(cAdd);textBoxPage.enterPAddress(pAdd);
		TestUtil.scrollToBottom(getDriver());
		textBoxPage.clickSubmit();

		TestUtil.waitForSec(4);

		assertTestStep.assertEquals(textBoxPage.assertionResponse("Name"),"Name:"+f1, "Verified Name in response");
		assertTestStep.assertEquals(textBoxPage.assertionResponse("Email"),"Email:"+eMail+domain, "Verified Email in response");
		assertTestStep.assertEquals(textBoxPage.assertionResponse("Current Address"),"Current Address :"+cAdd, "Verified Current Address in response");
		assertTestStep.assertEquals(textBoxPage.assertionResponse("Permanent Address"),"Permananet Address :"+pAdd,"Verified Permananet Address in response");
	}

	@AfterClass
	public void teardown() {
		super.tearDown();
	}
	
}
