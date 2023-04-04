package com.parallel.listeners;

import static com.parallel.listeners.ListenerBase.getRowCountForSummary;
import static com.parallel.listeners.ListenerBase.getRowCountForTestResultsFile;
import static com.parallel.listeners.ListenerBase.getTestResultsFileName;
import static com.parallel.listeners.ListenerBase.moduleName;
import static com.parallel.listeners.ListenerBase.setEmailAddress;
import static com.parallel.listeners.ListenerBase.testResultsFileName;
import static com.parallel.utils.ExcelUtils.createExcel;
import static com.parallel.utils.ExcelUtils.updateExcelSheetRowColumn;
import static com.parallel.utils.FrameworkConstant.EXTENTREPORT_PATH;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;

import java.util.Arrays;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.ExtentTest;
import com.parallel.report.ExtentReport;
import com.parallel.report.Log;

public class ListenerClass implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
//      ExtentManager.getInstance().getExtentTest().log(Status.PASS, "Test Case: "+result.getMethod().getMethodName()+ " is Passed.");
//      ExtentManager.getInstance().removeExtentObject();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Log.info(result.getName() + " is failed");
        Log.fail(result.getThrowable().getMessage());
        Log.info(Arrays.toString(result.getThrowable().getStackTrace()));
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            moduleName = context.getCurrentXmlTest().getParameter("module");
            testResultsFileName = getTestResultsFileName(context,moduleName);
            ExtentReport.initReports(testResultsFileName);
            createExcel(TEST_RESULT_SUMMARY);
            updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"TestResultsFile", getRowCountForTestResultsFile(testResultsFileName),0,EXTENTREPORT_PATH + testResultsFileName);
            updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"TestResultsFile", getRowCountForTestResultsFile(testResultsFileName),1,testResultsFileName);
            updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"Summary",getRowCountForSummary(moduleName),0,moduleName);
            setEmailAddress(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @AfterSuite
    public void onFinish(ITestContext context) {
        ExtentReport.flush();
    }

}
