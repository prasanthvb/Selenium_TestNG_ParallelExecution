package com.parallel.utils;


import java.io.File;
import java.time.Duration;
import java.util.Random;

/**
 *
 * Represents framework specific property/constant values.
 *
 */
public final class FrameworkConstant {

    // Project root directory
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String RESOURCE_PATH = PROJECT_PATH + File.separator + "src" + File.separator + "main"
            + File.separator + "java" + File.separator + "com" + File.separator + "parallel" + File.separator
            + "resources" + File.separator;

    public static final String CSP_EXTENTION = PROJECT_PATH + File.separator +"Referenced Libraries"+File.separator+"chrome-csp-disable-master.crx";

    // Property file
    public static final String PROPERTYFILE_PATH = RESOURCE_PATH + "config.properties";

    // JSON Path
    public static final String TestDataJSON_PATH = RESOURCE_PATH + File.separator + "testdata" + File.separator;
    public static final String MYSQLJSON_OUTPUT_PATH = PROJECT_PATH + File.separator  + "TestResult"+File.separator+"MYSQLResult.json";
    public static final String MYSQLJSON_INPUT_PATH = RESOURCE_PATH + "MySQLData.json";
    // Wait timing
    public static final Duration WAIT_TIME = Duration.ofMillis(13000);
    public static final int EXPLICIT_MAXWAIT = 10;
    public static final int EXPLICIT_MINWAIT = 2;
    public static final long PAGE_LOAD_TIMEOUT = 86;
    public static final long IMPLICIT_WAIT = 20;

    public static final int SHORT_WAIT = 3000;
    public static final int MEDIUM_WAIT = 6000;
    public static final int LONG_WAIT = 10000;

    // Screenshot paths
    public static final String SCREENSHOT_PATH = PROJECT_PATH + File.separator + "screenShots" + File.separator;

    public static final String testCaseScreenShotPath(String testCaseName) {
        return SCREENSHOT_PATH + testCaseName + File.separator + System.currentTimeMillis() + new Random().nextInt(20)
                + ".png";
    }

    public static final String testCaseScreenShotPath(String testCaseName, String selectedPath) {
        return selectedPath + File.separator + "screenShots" + File.separator + testCaseName + File.separator
                + System.currentTimeMillis() + new Random().nextInt(20) + ".png";
    }

    // Extent reporting
    public static final String EXTENTREPORT_PATH = PROJECT_PATH + File.separator + "ExtentResults"
            + File.separator;
    public static final String EDIT_EXTENTREPORT_PATH = EXTENTREPORT_PATH + "ExtentReport.html";
    public static final String EXTENT_CONFIG_PATH = RESOURCE_PATH + "extentreport.xml";
    public static final String TEST_RESULT_SUMMARY = EXTENTREPORT_PATH+"TestResultsSummary.xlsx";

    public static final String newExtentReportPath(String currentDate) {
        return EXTENTREPORT_PATH + "ExtentReport-" + currentDate + ".html";
    }

    public static final String REPORT_TITLE = "Test Report";
    public static final String AUTHOR = "Prasanth V B";
    public static final String ENVIRONMENT = "Test";

    public static final String YES = "yes";

}

