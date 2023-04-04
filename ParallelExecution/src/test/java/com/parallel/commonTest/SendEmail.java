package com.parallel.commonTest;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;
import static com.parallel.utils.ExcelUtils.getSheetDataInListMap;
import static com.parallel.utils.SentEmailUtil.sendEmail;

public class SendEmail {
	 public static List<Map> testSummary;
	    public static List<Map> testResultFiles;

	    @BeforeSuite
	    public void setUp() throws Exception {
	        testSummary = getSheetDataInListMap(TEST_RESULT_SUMMARY,"Summary");
	        testResultFiles = getSheetDataInListMap(TEST_RESULT_SUMMARY,"TestResultsFile");
	    }

	    @Test(priority = 1,description = "Send Email Post Test Execution")
	    public void LoggedinUserTest() throws InterruptedException {
	        try{
	            sendEmail(testSummary,testResultFiles);
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	    }

	    @AfterSuite
	    public void tearDown() throws Exception {
	        File file = new File(TEST_RESULT_SUMMARY);
	        System.gc();
	        file.deleteOnExit();
	    }
}
