package com.parallel.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryAnalyzerListener implements IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class Class, Constructor Constructor,
			Method Method) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

}
