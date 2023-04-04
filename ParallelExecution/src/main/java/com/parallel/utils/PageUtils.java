package com.parallel.utils;


import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.parallel.base.TestBase;
import com.parallel.report.Log;

public class PageUtils extends TestBase {

    By btnSearch = By.xpath("//button[contains(text(), 'Search')] | //a[@id='runReport'] | //button[@id='runReport']");

    By btnPrevInPagination = By.xpath("//span[contains(text(), 'Prev')] | //a[contains(text(), 'Prev')]");

    By btnNextInPagination = By.xpath("//span[contains(text(), 'Next')] | //a[contains(text(), 'Next')]");

    By btnPrevInPaginationForSecondTab = By
            .xpath("(//span[contains(text(), 'Prev')] | //a[contains(text(), 'Prev')])[last()]");

    By btnNextInPaginationForSecondTab = By
            .xpath("(//span[contains(text(), 'Next')] | //a[contains(text(), 'Next')])[last()]");

    By ajaxLoader = By.xpath("//div[@class='ajax_loader']");

    By iframeCloseHelpMessage = By.xpath("//iframe[@title='Close message']");

    By btnCloseHelpMessage = By.xpath("//button[@aria-label='Close message']");

    By lstTableResultValues = By.xpath("//tr/td[text()]");

    By drpdownItemsPerPage = By.xpath("//label[text()='Items per page']/following-sibling::select");

    By lstResultsInFirstColumn = By.xpath("//tbody//tr//td[string-length(@class)>1][1]");

    By txtRecordsShowing = By.xpath("//div[contains(text(), 'Showing')]/span[2]");

    By sortingElements = By.xpath("//th[contains(@class, 'sortable')]");

    String drpdownOptionsForItemsPerPage = "//label[text()='Items per page']/following-sibling::select/option";

    private static final List<String> ITEMS_PER_PAGE = Arrays.asList("10", "50", "100");
    private static final String ATTRIBUTE_CLASS = "class";

    /**
     * This method is to get the web element for the page to be selected
     * 
     * @param pageNum - int page number to be selected
     * 
     * @return - Web element for the page link
     */
    public WebElement lnkToSelectPage(int pageNum) {
        return getDriver()
                .findElement(By.xpath("(//ul[contains(@class, 'pagination')]/li/a[text()='" + pageNum + "'])[1]"));
    }

    /**
     * This method is to get the web element for the page to be selected
     * 
     * @param pageNum - int page number to be selected
     * 
     * @return - Web element for the page link
     */
    public WebElement lnkToSelectPageForSecondTab(int pageNum) {
        return getDriver().findElement(
                By.xpath("(//ul[contains(@class, 'pagination')]/li/a[contains(text(), '" + pageNum + "')])[last()]"));
    }

    /**
     * This method is to get the web element for the page number which is active
     * 
     * @param pageNum - page number to be verified
     * 
     * @return - Web element for the active page
     */
    public WebElement elePageNumToBeVerified(int pageNum) {
        return getDriver().findElement(By
                .xpath("//ul[contains(@class, 'pagination')]/li/span[contains(text(), '" + pageNum + "')]/parent::li"));
    }

    /**
     * This method is to get the web element for the page number which is active
     * 
     * @param pageNum - page number to be verified
     * 
     * @return - Web element for the active page
     */
    public WebElement elePageNumToBeVerifiedForSecondTab(int pageNum) {
        return getDriver().findElement(By.xpath("(//ul[contains(@class, 'pagination')]/li/span[contains(text(), '"
                + pageNum + "')]/parent::li)[last()]"));
    }

    /**
     * This method is to find the web element for the button like Submit/Search
     * 
     * @param buttonName - button name
     * 
     * @return - Button web element
     */
    public WebElement eleButton(String buttonName) {
        return getDriver().findElement(By.xpath("//button[contains(text(), '" + buttonName + "')]"));
    }

    /**
     * This method is to verify the pagination functionality
     * 
     * @param pageNumToBeSelected - int page number
     * 
     * @return boolean true - if the pagination is working fine else false
     */
    public boolean verifyPagination(int pageNumToBeSelected) {
        String classValue = "";
        int activePageNum = 0;
        boolean isPaginationVerified = false;

        if (TestUtil.waitUntilElementDisplayed(getDriver(),btnSearch, 2)) {
            TestUtil.javaScriptClick(getDriver(), btnSearch);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 1);
        }

        if (getDriver().findElements(lstTableResultValues).size() > 1) {

            TestUtil.pageLoadWait();
            TestUtil.scrollToBottom(getDriver());
            closeHelpButton();

            TestUtil.clickOn(getDriver(), lnkToSelectPage(pageNumToBeSelected));
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 3);

            classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerified(pageNumToBeSelected), ATTRIBUTE_CLASS);

            if (classValue.equalsIgnoreCase("active")) {
                isPaginationVerified = true;

                TestUtil.clickOn(getDriver(), btnPrevInPagination);
                TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 5);
                activePageNum = pageNumToBeSelected - 1;
                classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerified(activePageNum), ATTRIBUTE_CLASS);

                if (classValue.equalsIgnoreCase("active"))
                    isPaginationVerified = true;
                else {
                    isPaginationVerified = false;
                    return isPaginationVerified;
                }

                TestUtil.clickOn(getDriver(), btnNextInPagination);
                TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 5);
                activePageNum = activePageNum + 1;
                classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerified(activePageNum), ATTRIBUTE_CLASS);

                if (activePageNum == pageNumToBeSelected && classValue.equalsIgnoreCase("active"))
                    isPaginationVerified = true;
                else {
                    isPaginationVerified = false;
                    return isPaginationVerified;
                }

            }

            Assert.assertTrue(isPaginationVerified, "The pagination is not working as expected");
        } else {
            String value = TestUtil.getTextValue(getDriver(),lstTableResultValues);
            Log.pass(value);
        }
        TestUtil.pageScrollUp(getDriver());
        return isPaginationVerified;
    }

    /**
     * This method is to verify the pagination functionality for second tab page
     * 
     * @param pageNumToBeSelected - int page number
     * 
     * @return boolean true - if the pagination is working fine else false
     */
    public boolean verifyPaginationForSecondTab(int pageNumToBeSelected) {
        String classValue = "";
        int activePageNum = 0;
        boolean isPaginationVerified = false;

        if (TestUtil.waitUntilElementDisplayed(getDriver(),btnSearch, 2)) {
            TestUtil.javaScriptClick(getDriver(), btnSearch);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 1);
        }

        TestUtil.scrollToBottom(getDriver());

        closeHelpButton();

        TestUtil.clickOn(getDriver(), lnkToSelectPageForSecondTab(pageNumToBeSelected));
        TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 1);

        classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerifiedForSecondTab(pageNumToBeSelected),
                ATTRIBUTE_CLASS);

        if (classValue.equalsIgnoreCase("active")) {
            isPaginationVerified = true;

            TestUtil.clickOn(getDriver(), btnPrevInPaginationForSecondTab);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 2);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 2);
            activePageNum = pageNumToBeSelected - 1;
            classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerified(activePageNum), ATTRIBUTE_CLASS);

            if (classValue.equalsIgnoreCase("active"))
                isPaginationVerified = true;
            else {
                isPaginationVerified = false;
                return isPaginationVerified;
            }

            TestUtil.clickOn(getDriver(), btnNextInPaginationForSecondTab);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 2);
            activePageNum = activePageNum + 1;
            classValue = TestUtil.getAttributeValue(getDriver(),elePageNumToBeVerified(activePageNum), ATTRIBUTE_CLASS);

            if (activePageNum == pageNumToBeSelected && classValue.equalsIgnoreCase("active"))
                isPaginationVerified = true;
            else {
                isPaginationVerified = false;
                return isPaginationVerified;
            }
        }

        Assert.assertTrue(isPaginationVerified, "The pagination is not working as expected");
        return isPaginationVerified;
    }

    /**
     * This method is to close the Help message displaying at the bottom
     */
    public void closeHelpButton() {
        if (TestUtil.isDisplayed(getDriver(),iframeCloseHelpMessage)) {
            getDriver().switchTo().frame(TestUtil.findElement(getDriver(),iframeCloseHelpMessage));
            TestUtil.clickOn(getDriver(), btnCloseHelpMessage);
            getDriver().switchTo().defaultContent();
        }
    }

    /**
     * This method is to verify the Items per page drop down functionality
     */
    public void itemsPerPageVerification() {
        String actRecordCount = "", outOfRecordValue = "";
        String defaultValue = "10";

        for (String expRecordCount : ITEMS_PER_PAGE) {
            TestUtil.selectDropDownValue(getDriver(),drpdownOptionsForItemsPerPage, expRecordCount);
            TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 5);

            actRecordCount = String.valueOf(getDriver().findElements(lstResultsInFirstColumn).size());

            outOfRecordValue = TestUtil.getTextValue(getDriver(),txtRecordsShowing);

            Assert.assertEquals(actRecordCount, expRecordCount,
                    "The Items per page functionality is not working as expected for " + expRecordCount + " items");

            Assert.assertEquals(outOfRecordValue, expRecordCount,
                    "The number of records value in the footer is not displaying as expected");
        }
        TestUtil.selectDropDownValue(getDriver(),drpdownOptionsForItemsPerPage, defaultValue);
        TestUtil.waitUntilElementDisappears(getDriver(),ajaxLoader, 5);
    }

    public void verifySorting() {
        String classValue = "", fieldName = "";
        for (WebElement sortingElement : getDriver().findElements(sortingElements)) {
            TestUtil.clickOn(getDriver(), sortingElement);
            TestUtil.pageLoadShortWait();
            classValue = TestUtil.getAttributeValue(getDriver(),sortingElement, ATTRIBUTE_CLASS);
            fieldName = TestUtil.getTextValue(getDriver(),sortingElement);

        }
    }
}

