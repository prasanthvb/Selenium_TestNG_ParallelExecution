package com.parallel.pages.elements;


import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.By;

import com.parallel.base.TestBase;
import com.parallel.utils.StaticVariables;
import com.parallel.utils.TestUtil;

public class CheckBoxPage extends TestBase {

	By home = By.xpath("//*[@class=\"rct-text\"]/button");
	By underHome = By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li/span/button");
	By underDocuments = By.xpath("//*[@id=\"tree-node\"]/ol/li/ol/li[2]/ol/li/span/button");
	By expandCollapseButtons = By.xpath("//*[@id=\"tree-node\"]/div/button/*/*");
	By checkbox = By.className("rct-checkbox");
	By response = By.className("text-success");
	By message = By.id("result");

	public CheckBoxPage() {

	}

	public void expandHome() {
		TestUtil.clickOn(getDriver(), home);
	}

	public void expandUnderHome(String expandHome) {
		int i = TestUtil.arrayListToInt(StaticVariables.EXPAND_HOME, expandHome);
		TestUtil.scrollTillElementVisible(getDriver(), underHome);
		TestUtil.clickWithIndex(getDriver(), underHome, i);
	}

	public void expandUnderDocuments(String documentsSubmenu) {
		int i = ArrayUtils.indexOf(StaticVariables.EXPAND_DOCUMENTS, documentsSubmenu);
		TestUtil.scrollTillElementVisible(getDriver(), underDocuments);
		TestUtil.clickWithIndex(getDriver(), underDocuments, i);
	}

	public void expandAndCollapseAll(int i) {
		TestUtil.clickActionsWithIndex(getDriver(), expandCollapseButtons, i);
	}

	public void checkBoxElements(String enterCheckboxName) {
		int i = TestUtil.arrayListToInt(StaticVariables.CHECK_BOX_ITEMS, enterCheckboxName);
		TestUtil.scrollTillElementVisible(getDriver(), checkbox);
		TestUtil.clickWithIndex(getDriver(), checkbox, i);
	}

	public String getResponse(int i) {
		return TestUtil.getText(getDriver(), response, i);
	}

	public String result() {
		return TestUtil.getTextValue(getDriver(), message);
	}

}
