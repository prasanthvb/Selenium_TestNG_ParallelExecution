package com.parallel.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.parallel.utils.FrameworkConstant;

public class RetryAnalyzer implements IRetryAnalyzer {

	int counter = 0;
	int retryMaxLimit = FrameworkConstant.RETRY_COUNT;

	@Override
	public boolean retry(ITestResult result) {
		if (counter < retryMaxLimit) {
			counter++;
			return true;
		}
		return false;
	}

}
