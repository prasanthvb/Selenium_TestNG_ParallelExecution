package com.parallel.listeners;

import static com.parallel.listeners.ListenerBase.authorName;
import static com.parallel.listeners.ListenerBase.moduleName;
import static com.parallel.listeners.ListenerBase.updateTestResultsCount;
import static com.parallel.utils.TestUtil.timestamp;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.parallel.base.TestBase;
import com.parallel.report.ExtentFactory;
import com.parallel.report.ExtentReportNG;
import com.parallel.utils.TestUtil;

public class extentTestListener extends TestBase implements ITestListener {
    protected CustomAssertion assertTestStep = new CustomAssertion();
    ExtentReports report;
    ExtentTest test;
    public static String description;

    @Override
    public void onStart(ITestContext context) {
        try {
            report = ExtentReportNG.setupExtentReport(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
        test = report.createTest(moduleName + " - " + description);
        String methodName = result.getMethod().toString();
        methodName = methodName.substring(methodName.indexOf('.') + 1, methodName.indexOf('('));
        String testClass = result.getTestClass().getName()
                .substring(result.getTestClass().getName().lastIndexOf('.') + 1);
        String title = result.getTestContext().getSuite().getName();
        test.assignCategory(moduleName);
        test.assignAuthor(authorName);
        ExtentFactory.getInstance().setExtent(test);
        System.out.println(timestamp() + " - Test Under Execution - " + title + " - " + testClass + " - " + methodName
                + " - " + description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            if (assertTestStep.getAssertMessages().size() > 0) {
                printStatus(result, "Pass");
            } else {
                ExtentFactory.getInstance().getExtent().pass(description);
            }
            updateTestResultsCount(1);
            // ExtentFactory.getInstance().removeExtentObject();
            // removeExtentObject();
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
                ExtentFactory.getInstance().getExtent().fail(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
            }
            updateTestResultsCount(2);
            // ExtentFactory.getInstance().removeExtentObject();
            // removeExtentObject();
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
                ExtentFactory.getInstance().getExtent().skip(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
            }
            updateTestResultsCount(3);
            // ExtentFactory.getInstance().removeExtentObject();
            // removeExtentObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        report.flush();
    }

//  public ExtentTest getExtentTest() {
//      return threadLocal.get();
//  }
//
//  public void setExtentTest(ExtentTest test) {
//      threadLocal.set(test);
//  }
//
    public void printStatus(ITestResult result, String status) {
        assertTestStep.reset();
    }
//
//  public void removeExtentObject() {
//      threadLocal.remove();
//  }

}
