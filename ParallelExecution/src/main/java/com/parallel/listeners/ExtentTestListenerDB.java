package com.parallel.listeners;

import static com.parallel.listeners.ListenerBase.authorName;
import static com.parallel.listeners.ListenerBase.insertTestCaseSummaryOnStart;
import static com.parallel.listeners.ListenerBase.moduleName;
import static com.parallel.listeners.ListenerBase.testRunId;
import static com.parallel.listeners.ListenerBase.updateDbSummaryTablesOnFinish;
import static com.parallel.listeners.ListenerBase.updateTestResultsCount;
import static com.parallel.utils.MYSQLUtil.updateTestCaseSummaryTable;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.parallel.base.TestBase;
import com.parallel.report.ExtentFactory;
import com.parallel.report.ExtentReportNG;
import com.parallel.utils.TestUtil;

public class ExtentTestListenerDB extends TestBase implements ITestListener{	
	public static ExtentReports report;
    public static ExtentTest test;
    public static String description;
    private static final ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<>();

    @Override	
    public void onStart(ITestContext context) {
        try {
        	report= ExtentReportNG.setupExtentReportDB(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
            test = report.createTest(moduleName + " - " + description);
            String methodName = result.getMethod().toString();
            methodName = methodName.substring(methodName.indexOf('.')+1 , methodName.indexOf('('));
            String testClass = result.getTestClass().getName().substring(
                    result.getTestClass().getName().lastIndexOf('.') + 1);
            String title = result.getTestContext().getSuite().getName();
            ExtentFactory.getInstance().setExtent(test);
            ExtentFactory.getInstance().getExtent().assignCategory(moduleName);
            ExtentFactory.getInstance().getExtent().assignAuthor(authorName);
            
            insertTestCaseSummaryOnStart(description, moduleName);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
        	description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
//            if (assertTestStep.getAssertMessages().size() > 0) {
//                printStatus(result, "Pass");
//            } else {
                ExtentFactory.getInstance().getExtent().log(Status.PASS, "Test Passed");
                //getExtentTest().pass(description);
//            }
//            updateTestResultsCount(1);
            updateTestCaseSummaryTable("Pass",description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
        	description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
//            if (assertTestStep.getAssertMessages().size() > 0) {
//                printStatus(result, "Fail");
//            } else {
//                getExtentTest().fail(result.getThrowable().getMessage(),
//                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
                ExtentFactory.getInstance().getExtent().fail(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
            
//            }
                ExtentFactory.getInstance().getExtent().log(Status.FAIL, "Test Failed");
//            updateTestResultsCount(2);
            updateTestCaseSummaryTable("Fail",description, result.getThrowable());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
        	description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
//            if (assertTestStep.getAssertMessages().size() > 0) {
//                printStatus(result, "Skipped");
//            } else {
//                getExtentTest().skip(result.getThrowable().getMessage(),
//                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
                ExtentFactory.getInstance().getExtent().skip(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());    
//            }
//            updateTestResultsCount(3);
                ExtentFactory.getInstance().getExtent().log(Status.SKIP, "Test Skipped");
            updateTestCaseSummaryTable("Skipped",description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
        	updateTestResultsCount(1, context.getPassedTests().getAllResults().size());
    		updateTestResultsCount(2, context.getFailedTests().getAllResults().size());
    		updateTestResultsCount(3, context.getSkippedTests().getAllResults().size());
    		report.flush();
            updateDbSummaryTablesOnFinish(testRunId,moduleName);
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
