package com.parallel.report;

import com.aventstack.extentreports.ExtentTest;

public class ExtentManager {

    private ExtentManager() {
    }

    private static ExtentManager instance = new ExtentManager();

    public static ExtentManager getInstance() {
        return instance;
    }
    ThreadLocal<ExtentTest> extent = new ThreadLocal<ExtentTest>();
//  private static final ThreadLocal<ExtentTest> threadLocal = new ThreadLocal<>();

    public ExtentTest getExtentTest() {
        return extent.get();
    }

    public void setExtentTest(ExtentTest test) {
        extent.set(test);
    }
    
    public void removeExtentObject() {
        extent.remove();
    }
}
