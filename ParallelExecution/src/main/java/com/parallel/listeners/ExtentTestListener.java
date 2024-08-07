package com.parallel.listeners;

import static com.parallel.listeners.ListenerBase.authorName;
import static com.parallel.listeners.ListenerBase.moduleName;
import static com.parallel.listeners.ListenerBase.updateTestResultsCount;

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

public class ExtentTestListener extends TestBase implements ITestListener {
   
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
    	try {
        description = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
        test = report.createTest(moduleName + " - " + description);
        String methodName = result.getMethod().toString();
        methodName = methodName.substring(methodName.indexOf('.') + 1, methodName.indexOf('('));
        String testClass = result.getTestClass().getName()
                .substring(result.getTestClass().getName().lastIndexOf('.') + 1);
        String title = result.getTestContext().getSuite().getName();
        ExtentFactory.getInstance().setExtent(test);
        ExtentFactory.getInstance().getExtent().assignCategory(moduleName);
        ExtentFactory.getInstance().getExtent().assignAuthor(authorName);
    	}catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
        	 ExtentFactory.getInstance().getExtent().log(Status.PASS, "Test Passed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
//            if (assertTestStep.getAssertMessages().size() > 0) {
//                printStatus(result, "Fail");
//            } else {
                ExtentFactory.getInstance().getExtent().fail(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
                ExtentFactory.getInstance().getExtent().log(Status.FAIL, "Test Failed");
//            }
//            updateTestResultsCount(2);
            // ExtentFactory.getInstance().removeExtentObject();
            // removeExtentObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
//            if (assertTestStep.getAssertMessages().size() > 0) {
//                printStatus(result, "Skipped");
//            } else {
                ExtentFactory.getInstance().getExtent().skip(result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
                ExtentFactory.getInstance().getExtent().log(Status.SKIP, "Test Skipped");
//            }
//            updateTestResultsCount(3);
            // ExtentFactory.getInstance().removeExtentObject();
            // removeExtentObject();
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
    	}catch (Exception e) {
    		 throw new RuntimeException(e);
		}
        
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
