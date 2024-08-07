package com.parallel.listeners;

import org.apache.commons.configuration.ConfigurationException;
import org.testng.ITestContext;

import com.parallel.utils.FrameworkConstant;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.parallel.base.TestBase.fetchPropertyValue;
import static com.parallel.base.TestBase.setPropertyValue;
import static com.parallel.utils.ExcelUtils.*;
import static com.parallel.utils.MYSQLUtil.*;
import static com.parallel.utils.TestUtil.*;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;

public class ListenerBase {

	  public static boolean reportCreated = false;
	    public static String moduleName;
	    public static String userRole;
	    public static String authorName;
	    public static String testType;
	    public static String environment;
	    public static String testResultsFileName;
	    public static String testRunId = "", moduleNameUnderExecution = "";
	    public static String testExecutionStartTime, moduleExecutionStartTime,testCaseId,startTime;
	    public static int testCaseCounter;

	    public static String getTestResultsFileName(ITestContext context, String moduleName) {
	        if (isRegressionExecution(context)) {
	            return "ExtentReport_Regression.html";
	        } else {
	            reportCreated = false;
	            return "ExtentReport_" + moduleName + ".html";
	        }
	    }
	    public static boolean isRegressionExecution(ITestContext context) {
	        try{
	            if (context.getSuite().getParameter("executionType").equalsIgnoreCase("regression")) {
	                return true;
	            }
	            return false;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    public static int getRowCountForSummary(String moduleName) throws ConfigurationException, IOException {
	        try{
	            if(!fetchPropertyValue("moduleName", FrameworkConstant.PROPERTYFILE_PATH).equals(moduleName)){
	                setPropertyValue("moduleName",moduleName,FrameworkConstant.PROPERTYFILE_PATH);
	                setPropertyValue("moduleNameRowCountForExcel", String.valueOf(Integer.parseInt(fetchPropertyValue("moduleNameRowCountForExcel", FrameworkConstant.PROPERTYFILE_PATH).toString())+1)
	                        ,FrameworkConstant.PROPERTYFILE_PATH);
	            }
	        } catch (Exception e) {
	            setPropertyValue("moduleName",moduleName,FrameworkConstant.PROPERTYFILE_PATH);
	            setPropertyValue("moduleNameRowCountForExcel","1",FrameworkConstant.PROPERTYFILE_PATH);
	        }
	        return Integer.parseInt(fetchPropertyValue("moduleNameRowCountForExcel",FrameworkConstant.PROPERTYFILE_PATH).toString());
	    }

	    public static int getRowCountForTestResultsFile(String testResultsFileName) throws Exception {
	        try{
	            if(!fetchPropertyValue("testResultsFileName",FrameworkConstant.PROPERTYFILE_PATH).equals(testResultsFileName)){
	                setPropertyValue("testResultsFileName", testResultsFileName, FrameworkConstant.PROPERTYFILE_PATH);
	                setPropertyValue("testResultsFileRowCountForExcel", String.valueOf(Integer.parseInt(fetchPropertyValue("testResultsFileRowCountForExcel", FrameworkConstant.PROPERTYFILE_PATH).toString())+1),FrameworkConstant.PROPERTYFILE_PATH);
	            }
	        } catch (Exception e) {
	            setPropertyValue("testResultsFileName",testResultsFileName,FrameworkConstant.PROPERTYFILE_PATH);
	            setPropertyValue("testResultsFileRowCountForExcel","1",FrameworkConstant.PROPERTYFILE_PATH);
	        }
	        return Integer.parseInt(fetchPropertyValue("testResultsFileRowCountForExcel",FrameworkConstant.PROPERTYFILE_PATH).toString());
	    }

	    public static void setEmailAddress(ITestContext context) throws Exception {
	        try{
	            ArrayList emailAddressArray = getColumnValuesInArray(TEST_RESULT_SUMMARY,"EmailAddress",0,0);
	            String [] emailAddresses;
	            if(context.getSuite().getParameter("sendemail").toString().contains(",")){
	                emailAddresses = context.getSuite().getParameter("sendemail").toString().split(",");
	            } else {
	                emailAddresses = new String[] {context.getSuite().getParameter("sendemail").toString()};
	            }
	            for(String emailAddress : emailAddresses){
	                if(!emailAddressArray.contains(emailAddress)){
	                    updateExcelSheetRowColumn(TEST_RESULT_SUMMARY,"EmailAddress",getRowCountForEmailAddress(),0,emailAddress);
	                }
	            }
	        } catch (Exception e) {
	        }
	    }

	    public static int getRowCountForEmailAddress() throws Exception {
	        setPropertyValue("emailAddressRowCountForExcel", String.valueOf(Integer.parseInt(fetchPropertyValue("emailAddressRowCountForExcel", FrameworkConstant.PROPERTYFILE_PATH).toString())+1),FrameworkConstant.PROPERTYFILE_PATH);
	        return Integer.parseInt(fetchPropertyValue("emailAddressRowCountForExcel",FrameworkConstant.PROPERTYFILE_PATH).toString());
	    }

	    public static void updateTestResultsCount(int columnNumber, int count) {
	        try {
	            updateTestSummary(TEST_RESULT_SUMMARY,"Summary",getRowCountForSummary(moduleName),columnNumber, count);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public static void setDbValuesOnStart(String moduleName,String userRole) throws Exception {
	        Map mapTestSummary = new HashMap();
	        Map mapModuleSummary = new HashMap();
	        Map mapTestDetails = getTestDetails(moduleName);
	        if(testRunId.equals("")){
	            testRunId = getRandomNumberBetween(100000000L, 999999999L)+getSystemDate();
	            setPropertyValue("testRunId",testRunId,FrameworkConstant.PROPERTYFILE_PATH);
	            testExecutionStartTime = String.valueOf(new Timestamp(System.currentTimeMillis()));
	            setPropertyValue("testExecutionStartTime", testExecutionStartTime, FrameworkConstant.PROPERTYFILE_PATH);
	            mapTestSummary.put("TEST_RUN_ID",testRunId);
	            mapTestSummary.put("APP_NAME","Testing");
	            mapTestSummary.put("RUN_ENVIRONMENT","QA");
	            mapTestSummary.put("TEST_TYPE","Regression");
	            mapTestSummary.put("TOTAL_TESTCASES_EXECUTED","0");
	            mapTestSummary.put("TOTAL_TESTCASES_PASSED","0");
	            mapTestSummary.put("TOTAL_TESTCASES_FAILED","0");
	            mapTestSummary.put("TOTAL_TESTCASES_SKIPPED","0");
	            mapTestSummary.put("PASS_PERCENTAGE","0");
	            mapTestSummary.put("EXECUTED_BY",System.getProperty("user.name").toString());
	            mapTestSummary.put("START_TIME",testExecutionStartTime);
	            mapTestSummary.put("END_TIME",testExecutionStartTime);
	            insertExecutionSummary(mapTestSummary);
	        }
	        moduleExecutionStartTime = String.valueOf(new Timestamp(System.currentTimeMillis()));
	        mapModuleSummary.put("TEST_RUN_ID",testRunId);
	        mapModuleSummary.put("MODULE_ID",mapTestDetails.get("module_id").toString());
	        mapModuleSummary.put("TOTAL_TESTCASES_EXECUTED","0");
	        mapModuleSummary.put("TOTAL_TESTCASES_PASSED","0");
	        mapModuleSummary.put("TOTAL_TESTCASES_FAILED","0");
	        mapModuleSummary.put("TOTAL_TESTCASES_SKIPPED","0");
	        mapModuleSummary.put("PASS_PERCENTAGE","0");
	        mapModuleSummary.put("EXECUTED_BY",System.getProperty("user.name").toString());
	        mapModuleSummary.put("USER_ROLE",userRole);
	        mapModuleSummary.put("START_TIME", moduleExecutionStartTime);
	        mapModuleSummary.put("END_TIME", moduleExecutionStartTime);
	        insertModuleSummary(mapModuleSummary);
	        testCaseId = mapTestDetails.get("test_case_id").toString();
	        //testCaseId = getTestCaseCode(moduleName);
	    }

	    public static void insertTestCaseSummaryOnStart(String description, String moduleName) throws Exception {
	        testCaseCounter = moduleName.equalsIgnoreCase(moduleNameUnderExecution) ? testCaseCounter+1 : 1;
	        Map mapTestDetails = getTestDetails(moduleName,description);
	        startTime = String.valueOf(new Timestamp(System.currentTimeMillis()));
	        moduleNameUnderExecution = moduleName;
	        try {
	            Map mapTestCaseSummary = new HashMap();
	            mapTestCaseSummary.put("TEST_RUN_ID",testRunId);
	            mapTestCaseSummary.put("TEST_CASE_ID",testCaseId+"_"+String.format("%03d", testCaseCounter));
	            mapTestCaseSummary.put("TEST_CASE_NAME",description);
	            mapTestCaseSummary.put("TEST_CASE_STATUS","NA");
	            mapTestCaseSummary.put("EXCEPTION_ERROR_MESSAGE","NA");
	            mapTestCaseSummary.put("MODULE_ID",mapTestDetails.get("module_id").toString());
	            mapTestCaseSummary.put("PAGE_ID",mapTestDetails.get("page_id").toString());
	            mapTestCaseSummary.put("APP_NAME","Testing");
	            mapTestCaseSummary.put("RUN_ENVIRONMENT","QA");
	            mapTestCaseSummary.put("TEST_TYPE","Regression");
	            mapTestCaseSummary.put("USER_ROLE",userRole);
	            mapTestCaseSummary.put("EXECUTED_BY",authorName);
	            mapTestCaseSummary.put("START_TIME",startTime);
	            mapTestCaseSummary.put("END_TIME",startTime);
	            insertQaTestCaseSummary(mapTestCaseSummary);
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public static void updateDbSummaryTablesOnFinish(String testRunId, String moduleName) throws Exception {
	        Map mapTestDetails = getTestDetails(moduleName);
	        String moduleSummaryQuery = "SELECT COUNT(*) as COUNT, TEST_CASE_STATUS from TESTCASE_SUMMARY\n" +
	                "WHERE TEST_RUN_ID = '"+testRunId+"' \n" +
	                "AND MODULE_ID = '"+mapTestDetails.get("module_id").toString()+"' \n" +
	                "GROUP by TEST_CASE_STATUS \n" +
	                "ORDER by TEST_CASE_STATUS";
	        String testSummaryQuery = "SELECT COUNT(*) as COUNT, TEST_CASE_STATUS from TESTCASE_SUMMARY\n" +
	                "WHERE TEST_RUN_ID = '"+testRunId+"' \n" +
	                "GROUP by TEST_CASE_STATUS \n" +
	                "ORDER by TEST_CASE_STATUS";
	        updateModuleSummaryTable(getExecutionSummary(moduleSummaryQuery),testRunId,mapTestDetails.get("module_id").toString());
	        updateExecutionSummaryTable(getExecutionSummary(testSummaryQuery),testRunId);
	    }

	    private static HashMap getExecutionSummary(String query) throws Exception {
	        List<Map<String, String>> resultSummary = getResultSetInListMap(query);
	        HashMap mapSummary = new HashMap();
	        mapSummary.put("TOTAL_TESTCASES_PASSED","0");
	        mapSummary.put("TOTAL_TESTCASES_FAILED","0");
	        mapSummary.put("TOTAL_TESTCASES_SKIPPED","0");
	        int totalExecutedCount = 0;
	        for(Map map : resultSummary){
	            switch (map.get("TEST_CASE_STATUS").toString()) {
	                case "Pass" :
	                    mapSummary.replace("TOTAL_TESTCASES_PASSED",map.get("COUNT").toString());
	                    break;
	                case "Fail" :
	                    mapSummary.replace("TOTAL_TESTCASES_FAILED",map.get("COUNT").toString());
	                    break;
	                case "Skipped" :
	                    mapSummary.replace("TOTAL_TESTCASES_SKIPPED",map.get("COUNT").toString());
	                    break;
	            }
	            totalExecutedCount = totalExecutedCount+Integer.parseInt(map.get("COUNT").toString());
	        }
	        mapSummary.put("TOTAL_TESTCASES_EXECUTED",String.valueOf(totalExecutedCount));
	        return mapSummary;
	    }

}
