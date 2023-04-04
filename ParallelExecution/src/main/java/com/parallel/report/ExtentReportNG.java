package com.parallel.report;

import static com.parallel.listeners.ListenerBase.authorName;
import static com.parallel.listeners.ListenerBase.getRowCountForSummary;
import static com.parallel.listeners.ListenerBase.getRowCountForTestResultsFile;
import static com.parallel.listeners.ListenerBase.getTestResultsFileName;
import static com.parallel.listeners.ListenerBase.isRegressionExecution;
import static com.parallel.listeners.ListenerBase.moduleName;
import static com.parallel.listeners.ListenerBase.reportCreated;
import static com.parallel.listeners.ListenerBase.setEmailAddress;
import static com.parallel.listeners.ListenerBase.testResultsFileName;
import static com.parallel.utils.ExcelUtils.createExcel;
import static com.parallel.utils.ExcelUtils.updateExcelSheetRowColumn;
import static com.parallel.utils.FrameworkConstant.EXTENTREPORT_PATH;
import static com.parallel.utils.FrameworkConstant.TEST_RESULT_SUMMARY;

import java.io.File;

import org.testng.ITestContext;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.parallel.utils.FrameworkConstant;

public class ExtentReportNG {

	static ExtentReports extent;

	public static ExtentReports setupExtentReport(ITestContext context) throws Exception {

		moduleName = context.getCurrentXmlTest().getParameter("module");
		authorName = context.getCurrentXmlTest().getParameter("author");
		testResultsFileName = getTestResultsFileName(context, moduleName);
		if (reportCreated == false) {
			extent = new ExtentReports();
			ExtentSparkReporter spark = new ExtentSparkReporter(EXTENTREPORT_PATH + testResultsFileName);
			spark.loadXMLConfig(new File(FrameworkConstant.EXTENT_CONFIG_PATH));
			extent.attachReporter(spark);
			reportCreated = isRegressionExecution(context) ? true : false;
			createExcel(TEST_RESULT_SUMMARY);
			updateExcelSheetRowColumn(TEST_RESULT_SUMMARY, "TestResultsFile",
					getRowCountForTestResultsFile(testResultsFileName), 0, EXTENTREPORT_PATH + testResultsFileName);
			updateExcelSheetRowColumn(TEST_RESULT_SUMMARY, "TestResultsFile",
					getRowCountForTestResultsFile(testResultsFileName), 1, testResultsFileName);
			setEmailAddress(context);
		}
		updateExcelSheetRowColumn(TEST_RESULT_SUMMARY, "Summary", getRowCountForSummary(moduleName), 0, moduleName);
		return extent;
	}

}
