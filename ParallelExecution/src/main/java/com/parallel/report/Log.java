package com.parallel.report;

import static com.parallel.listeners.ListenerBase.*;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.parallel.base.TestBase;
import com.parallel.utils.TestUtil;
/**
 * Provides type of log such as pass, fail, error, etc. in the Extent report,
 * log file and reporter log.
 */

public final class Log extends TestBase{

    public static ExtentTest test;

    public Log() {
        // private to avoid initialization
    }

    /**
     * Report status as pass with message in log file and reporter only.
     * 
     * @param message --> Message which wanted to pass.
     */
    public static void pass(String message) {
        ExtentManager.getInstance().getExtentTest().pass(message);
        updateTestResultsCount(1);
    }

    /**
     * Report status as pass with message and screenshot in extent report, log file
     * and reporter.
     * 
     * @param message              --> Message which wanted to pass.
     * @param isScreenShotRequired --> Attach the screenshot if true, otherwise no
     *                             screenshot attachment.
     */
    public static void pass(String message, boolean isScreenShotRequired) {
        ExtentManager.getInstance().getExtentTest().pass(message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
        updateTestResultsCount(1);
    }

    /**
     * Report status as fail with message and screenshot in extent report, log file
     * and reporter.
     * 
     * @param message              --> Message which wanted to fail.
     * @param isScreenShotRequired --> Attach the screenshot if true, otherwise no
     *                             screenshot attachment.
     */
    public static void fail(String message, boolean isScreenShotRequired) {
        ExtentManager.getInstance().getExtentTest().fail(message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
        updateTestResultsCount(2);
    }

    /**
     * Report status as fail with message in log file and reporter only.
     * 
     * @param message --> Message which wanted to fail.
     */
    public static void fail(String message) {
        ExtentManager.getInstance().getExtentTest().fail(message);
        updateTestResultsCount(2);
        //ExtentManager.getInstance().removeExtentObject();
    }

    /**
     * Report status as info message in extent report,log file and reporter.
     * 
     * @param message --> Message which wanted to mark as info.
     */
    public static void info(String message) {
        ExtentManager.getInstance().getExtentTest().info(message);
    }

    /**
     * Report status as skip with message in extent report.
     * 
     * @param message --> Message which wanted to mark as skip.
     */
    public static void skip(String message) {
        ExtentManager.getInstance().getExtentTest().skip(message);
        updateTestResultsCount(3);
    }

    /**
     * Report status as warning with message in extent report,log file and reporter.
     * 
     * @param message --> Message which wanted to mark as warning.
     */
    public static void warning(String message) {
        ExtentManager.getInstance().getExtentTest().warning(message);
    }

}
