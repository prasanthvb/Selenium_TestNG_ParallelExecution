package com.parallel.pages.home;

import org.openqa.selenium.By;

import com.parallel.base.TestBase;
import com.parallel.utils.StaticVariables;
import com.parallel.utils.TestUtil;

public class HomePage extends TestBase {

	By clickHomePageItem = By.className("card-up");

	public HomePage() {
	}

	public void chooseHomePageMenu(String homeMenu) {
		int indexOfElement = TestUtil.arrayListToInt(StaticVariables.HOME_MENU_ITEMS, homeMenu);
		TestUtil.scrollTillElementVisible(getDriver(), clickHomePageItem);
		TestUtil.clickWithIndex(getDriver(), clickHomePageItem, indexOfElement);
	}
	
	public void chooseElementPageMenu(String homeMenu) {
		int indexOfElement = TestUtil.arrayListToInt(StaticVariables.HOME_MENU_ITEMS, homeMenu);
		TestUtil.scrollTillElementVisible(getDriver(), clickHomePageItem);
		TestUtil.clickWithIndex(getDriver(), clickHomePageItem, indexOfElement);
	}

}
