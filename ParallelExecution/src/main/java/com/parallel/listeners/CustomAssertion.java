package com.parallel.listeners;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.parallel.base.TestBase;
import com.parallel.report.ExtentFactory;
import com.parallel.utils.TestUtil;

public class CustomAssertion extends Assertion {

	 public static List<String> assert_messages = new ArrayList<>();
	    public static int stepsCounter = 1;
	    public static String testDescription = "";
	    public static ExtentTest extentNode;

	    @Override
	    public void onBeforeAssert(IAssert<?> a) {
	        try {
	        	ExtentTestListener extentListner = new ExtentTestListener();
	          if(!testDescription.equals(extentListner.description)){
	              extentNode = extentListner.test.createNode(extentListner.description);
	                testDescription = extentListner.description;
	            }
	        } catch (Exception e) {
//	            if(!testDescription.equals(description)){
//	                extentNode = test.createNode(description);
//	                testDescription = description;
//	            }        
	        }
	    }

	    @Override
	    public void onAfterAssert(IAssert<?> a) {}

	    @Override
	    public void onAssertSuccess(IAssert<?> assertCommand) {
	        if (assertCommand.getMessage().contains("takescreenshot")){
	            ExtentFactory.getInstance().getExtent().pass( "\n <br>" + "Assertion : " + stepsCounter + " - Status: PASS - " + assertCommand.getMessage().replace(" #takescreenshot#","") + "\n<br>" + "\n <br>" + "",
	                    MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
	        } else {
	            ExtentFactory.getInstance().getExtent().pass( "\n <br>" + "Assertion : " + stepsCounter + " - Status: PASS - " + assertCommand.getMessage() + "\n<br>" + "\n <br>" + "");
	        }
	        assert_messages.add("Assertion : " + stepsCounter + "   Status: PASS   " + assertCommand.getMessage() + "\n<br>");
	        stepsCounter ++;
	    }

	    @Override
	    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        ex.printStackTrace(pw);
	        ExtentFactory.getInstance().getExtent().fail("\n <br>"+"Assertion : " + stepsCounter + "   Status: FAIL   " + assertCommand.getMessage() + "\n<br>" + sw.toString() + "<br>"+"\n <br>"+"",
	                MediaEntityBuilder.createScreenCaptureFromBase64String(TestUtil.getscreenBase64(TestBase.getDriver())).build());
	        assert_messages.add("Assertion : " + stepsCounter + "   Status: FAIL   " + assertCommand.getMessage() + "\n<br>" + sw.toString() + "<br>");
	        stepsCounter ++;
	    }

	    public List<String> getAssertMessages() {
	        return assert_messages;
	    }
	    public void reset() {
	        assert_messages.clear();
	        stepsCounter = 1;
	    }

}
