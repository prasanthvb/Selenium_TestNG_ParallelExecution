package com.parallel.pages.elements;

import org.openqa.selenium.By;

import com.parallel.base.TestBase;
import com.parallel.utils.TestUtil;

public class RadioButtonPage extends TestBase {

	By response = By.className("mt-3");
	By buttons = By.xpath("//*[contains(@class,'custom-control-inline')]");

	public RadioButtonPage() {
	}

	public void clickButton(int i) {
		TestUtil.clickWithIndex(getDriver(),buttons, i);
	}

	public String responseRadioButton() {
		return TestUtil.getTextValue(getDriver(),response);
	}
}
