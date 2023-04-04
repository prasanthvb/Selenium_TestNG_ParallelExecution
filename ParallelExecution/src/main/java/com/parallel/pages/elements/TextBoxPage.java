package com.parallel.pages.elements;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;

import com.parallel.base.TestBase;
import com.parallel.utils.StaticVariables;
import com.parallel.utils.TestUtil;

public class TextBoxPage extends TestBase {

	By fullName = By.id("userName");
	By mail = By.id("userEmail");
	By currentAddress = By.id("currentAddress");
	By permanentAddress = By.id("permanentAddress");
	By submit = By.id("submit");
	By assertion = By.className("mb-1");

	public TextBoxPage() {

	}

	public String assertionResponse(String response) {
		int i = ArrayUtils.indexOf(StaticVariables.RESPONSES, response);
		return TestUtil.getTexts(getDriver(), assertion, i);
	}

	public void enterFullName(String credentials) {
		TestUtil.sendKeys(getDriver(), fullName, credentials);
	}

	public void enterMail(String eMail) {
		TestUtil.sendKeys(getDriver(), mail, eMail);
	}

	public void enterCAddress(String address) {
		TestUtil.sendKeys(getDriver(), currentAddress, address);
	}

	public void enterPAddress(String address) {
		TestUtil.sendKeys(getDriver(), permanentAddress, address);
	}

	public void clickSubmit() {
		TestUtil.clickOn(getDriver(), submit);
	}
}
