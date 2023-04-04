package com.parallel.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.parallel.base.TestBase;
import com.parallel.report.ExtentFactory;
import com.parallel.utils.FrameworkConstant;
import com.parallel.utils.TestUtil;

import static com.parallel.listeners.ListenerBase.*;
import static com.parallel.utils.ExcelUtils.createExcel;
import static com.parallel.utils.ExcelUtils.updateExcelSheetRowColumn;
import static com.parallel.utils.FrameworkConstant.EXTENTREPORT_PATH;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;
import static com.parallel.utils.MYSQLUtil.updateTestCaseSummaryTable;
import static com.parallel.utils.TestUtil.timestamp;

import java.io.File;

public class extentTestListenerDB extends TestBase implements ITestListener{	
	public static ExtentReports extent;
    public static ExtentTest test;
    public static String description;
    private static final ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<>();

    @Override	
    public void onStart(ITestContext context) {
        try {
            moduleName = context.getCurrentXmlTest().getParameter("module");
            authorName = context.getCurrentXmlTest().getParameter("author");
            userRole = context.getCurrentXmlTest().getParameter("userRole");
            testResultsFileName = getTestResultsFileName(context,moduleName);
            if (reportCreated == false){
                extent = new ExtentReports();
                ExtentSparkReporter spark = new ExtentSparkReporter(EXTENTREPORT_PATH +userRole+"_"+testResultsFileName);
                spark.loadXMLConfig(new File(FrameworkConstant.EXTENT_CONFIG_PATH));
                extent.attachReporter(spark);
                reportCreated = isRegressionExecution(context) ? true : false;
                createExcel(TEST_RESULT_SUMMARY);
                updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"TestResultsFile", getRowCountForTestResultsFile(testResultsFileName),0,EXTENTREPORT_PATH + testResultsFileName);
                updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"TestResultsFile", getRowCountForTestResultsFile(testResultsFileName),1,testResultsFileName);
                setEmailAddress(context);
                ListenerBase.setDbValuesOnStart(moduleName,userRole);
            }
            updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"Summary",getRowCountForSummary(moduleName),0,moduleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
            test = extent.createTest(moduleName + " - " + description);
            String methodName = result.getMethod().toString();
            methodName = methodName.substring(methodName.indexOf('.')+1 , methodName.indexOf('('));
            String testClass = result.getTestClass().getName().substring(
                    result.getTestClass().getName().lastIndexOf('.') + 1);
            String title = result.getTestContext().getSuite().getName();
            test.assignCategory(moduleName);
            test.assignAuthor(authorName);
            ExtentFactory.getInstance().setExtent(test);
            //setExtentTest(test);
            ListenerBase.insertTestCaseSummaryOnStart(description,moduleName,userRole);
            System.out.println(timestamp()+" - "+testRunId+" - Test Under Execution - "+title+" - "+ testClass + " - "+methodName+" - "+description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            if (assertTestStep.getAssertMessages().size() > 0) {
                printStatus(result, "Pass");
            } else {
                ExtentFactory.getInstance().getExtent().pass(description);
                //getExtentTest().pass(description);
            }
            updateTestResultsCount(1);
            updateTestCaseSummaryTable("Pass",testCaseId,testRunId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (assertTestStep.getAssertMessages().size() > 0) {
                printStatus(result, "Fail");
            } else {
//                getExtentTest().fail(result.getThrowable().getMessage(),
//                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
                ExtentFactory.getInstance().getExtent().fail(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
            
            }
            updateTestResultsCount(2);
            updateTestCaseSummaryTable("Fail",testCaseId,testRunId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            if (assertTestStep.getAssertMessages().size() > 0) {
                printStatus(result, "Skipped");
            } else {
//                getExtentTest().skip(result.getThrowable().getMessage(),
//                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
                ExtentFactory.getInstance().getExtent().skip(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());    
            }
            updateTestResultsCount(3);
            updateTestCaseSummaryTable("Skipped",testCaseId,testRunId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            extent.flush();
            ListenerBase.updateDbSummaryTablesOnFinish(testRunId,moduleName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static ExtentTest getExtentTest() {
//        Thread.currentThread().getId();
//        return threadLocal.get();
//    }
//
//    static void setExtentTest(ExtentTest test) {
//        threadLocal.set(test);
//    }


    private void printStatus(ITestResult result, String status) {
        assertTestStep.reset();
    }

}
