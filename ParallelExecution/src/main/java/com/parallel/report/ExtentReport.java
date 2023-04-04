package com.parallel.report;

import static com.parallel.base.TestBase.setPropertyValue;

import java.io.File;
import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.parallel.utils.FrameworkConstant;

import org.apache.commons.configuration.ConfigurationException;

public class ExtentReport {
    public ExtentReport() {
    }
    static ExtentReports extent;

    public static void initReports(String testResultsFileName) throws IOException, ConfigurationException {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstant.EXTENTREPORT_PATH+ testResultsFileName);
        spark.loadXMLConfig(new File(FrameworkConstant.EXTENT_CONFIG_PATH));
        extent.attachReporter(spark);
        extent.setSystemInfo("Executed on OS: ", System.getProperty("os.name"));
        extent.setSystemInfo("Executed by User: ", System.getProperty("user.name"));
        setPropertyValue("testResultsFileName", FrameworkConstant.EXTENTREPORT_PATH+ testResultsFileName, FrameworkConstant.PROPERTYFILE_PATH);
    }

    public static void flush() {
        extent.flush();
    }

    public static void createTest(String testCaseName) {
        ExtentTest test = extent.createTest(testCaseName);
        ExtentManager.getInstance().setExtentTest(test);
    }

}
