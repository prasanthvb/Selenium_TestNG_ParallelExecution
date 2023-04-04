package com.parallel.test.ElementsTest;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.parallel.base.TestBase;
import com.parallel.pages.button.ButtonsPage;
import com.parallel.pages.elements.CheckBoxPage;
import com.parallel.pages.elements.RadioButtonPage;
import com.parallel.pages.elements.TextBoxPage;
import com.parallel.pages.home.HomePage;
import com.parallel.report.ExtentReport;
import com.parallel.report.Log;
import com.parallel.utils.TestUtil;

public class CheckBoxTest extends TestBase{
	HomePage homepage;
	ButtonsPage buttonspage;
	CheckBoxPage checkBoxPage;

	// Here we are following Listener with Log file and create report in class level

	@BeforeClass
	@Parameters({ "browser", "url" })
	public void setup(String browser, String url) {
		initialization(browser, url);
		homepage = new HomePage();
		buttonspage = new ButtonsPage();
		checkBoxPage = new CheckBoxPage();
	}

	@Test(priority = 1)
	public void testCheckBoxExpansion() {
		ExtentReport.createTest("Tools QA checkbox Page Validation");
		try {
		homepage.chooseHomePageMenu("Elements");
		buttonspage.chooseElementsMenu("Check Box");
        checkBoxPage.expandHome();
        // Expands Desktop,Documents or Downloads
        checkBoxPage.expandUnderHome("Desktop");
        checkBoxPage.expandUnderHome("Documents");
        checkBoxPage.expandUnderHome("Downloads");
        // expand  for Work Space & Office under Documents
        checkBoxPage.expandUnderDocuments("WorkSpace");
        checkBoxPage.expandUnderDocuments("Office");
        TestUtil.waitForSec(3);
        //collapse all =1, expands=0
        checkBoxPage.expandAndCollapseAll(1);
        TestUtil.waitForSec(3);
        checkBoxPage.expandAndCollapseAll(0);
		boolean contains = getDriver().getCurrentUrl().contains("checkbox");
		Assert.assertTrue(contains, "User is not in checkbox page");
		Log.pass("Check Box selections are verified");
		} catch (Exception e) {
			Log.fail("Check Box verification failed" + e, true);
		}
    }
	
	@AfterClass
	public void teardown() {
		super.tearDown();
	}
}
