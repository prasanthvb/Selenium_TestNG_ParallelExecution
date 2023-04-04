package com.parallel.pages.button;

import org.openqa.selenium.By;

import com.parallel.base.TestBase;
import com.parallel.utils.StaticVariables;
import com.parallel.utils.TestUtil;

public class ButtonsPage extends TestBase {

	By ad = By.id("close-fixedban");
	By clickElementsItems = By.xpath("//*[contains(@class,\"collapse show\")]/ul/li");
	By clickMeButton = By.xpath("//*[@class=\"mt-4\"][2]/button");
	By responseDynamicClick = By.id("dynamicClickMessage");
	By doubleClickMe = By.id("doubleClickBtn");
	By getDoubleClickMeMessage = By.id("doubleClickMessage");
	By rightClick = By.id("rightClickBtn");
	By rightClickResponse = By.id("rightClickMessage");

	public ButtonsPage() {

	}

	public void clickXAd() {
		TestUtil.clickOn(getDriver(), ad);
	}

	public void clickClickMeButton() {
		TestUtil.clickOn(getDriver(), clickMeButton);
	}

	public String responseClickMe() {
		return TestUtil.getTextValue(getDriver(),responseDynamicClick);
	}

	public void doubleClickMeButton() {
		TestUtil.doubleClick(getDriver(), doubleClickMe);
	}

	public String doubleClickResponse() {
		return TestUtil.getTextValue(getDriver(),getDoubleClickMeMessage);
	}

	public void rightClickButton() {
		TestUtil.rightClick(getDriver(),rightClick);
	}

	public String getRightClickResponse() {
		return TestUtil.getTextValue(getDriver(),rightClickResponse);
	}

	public void chooseElementsMenu(String elementsMenuList) {
		int i = TestUtil.arrayListToInt(StaticVariables.ELEMENTS_MENU, elementsMenuList);
		TestUtil.scrollTillElementVisible(getDriver(), clickElementsItems);
		TestUtil.clickActionsWithIndex(getDriver(),clickElementsItems, i);
	}
}
