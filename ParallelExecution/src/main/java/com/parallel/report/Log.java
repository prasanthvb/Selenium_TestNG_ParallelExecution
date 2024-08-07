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
        ExtentFactory.getInstance().getExtent().pass(message);
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
    	ExtentFactory.getInstance().getExtent().pass(message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());       
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
    	ExtentFactory.getInstance().getExtent().fail(message,
                MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(getDriver())).build());
    }

    /**
     * Report status as fail with message in log file and reporter only.
     * 
     * @param message --> Message which wanted to fail.
     */
    public static void fail(String message) {
    	ExtentFactory.getInstance().getExtent().fail(message);
        //ExtentManager.getInstance().removeExtentObject();
    }

    /**
     * Report status as info message in extent report,log file and reporter.
     * 
     * @param message --> Message which wanted to mark as info.
     */
    public static void info(String message) {
    	ExtentFactory.getInstance().getExtent().info(message);
    }

    /**
     * Report status as skip with message in extent report.
     * 
     * @param message --> Message which wanted to mark as skip.
     */
    public static void skip(String message) {
    	ExtentFactory.getInstance().getExtent().skip(message);
    }

    /**
     * Report status as warning with message in extent report,log file and reporter.
     * 
     * @param message --> Message which wanted to mark as warning.
     */
    public static void warning(String message) {
    	ExtentFactory.getInstance().getExtent().warning(message);
    }

}
