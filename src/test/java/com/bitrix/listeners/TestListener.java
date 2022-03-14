package com.bitrix.listeners;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.bitrix.utility.BitrixLogger;
import com.bitrix.utility.BrowserFactory;
import com.bitrix.utility.PropertyFileConfigurations;
import com.bitrix.utility.Utilities;

public class TestListener implements ITestListener {

	public  static WebDriver webdriverInstance;
	 static int  suitCount = 0;
	private ExtentReports extentRpt;
	private ExtentTest extentTest;
	PropertyFileConfigurations config;
	String testngBrowser= "";

	public void onTestStart(ITestResult result) {
		 extentTest  = extentRpt.createTest(result.getTestClass().getName()+"      @TestCase : " + result.getMethod().getMethodName());
	     extentTest.assignCategory(result.getTestClass().getName());  // This line is ued to add catagories.
	     extentTest.log(Status.INFO, result.getTestClass().getName()+"      @TestCase : " + result.getMethod().getMethodName() + "test is started");
	}

	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE : - " + methodName.toUpperCase()
                + " Passed" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        extentTest.pass(m);
	}

	public void onTestFailure(ITestResult result) {
		String path = config.getScreenshotPath();
        String screenshotPath =Utilities.captureScreenshotWithRobot(result.getName(), path);
        String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
        extentTest.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured : Click to see"  + "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>"  + "\n");
        String failureLog = "TEST CASE FAILED";
        Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
        extentTest.log(Status.FAIL, m);
        try {
            extentTest.fail("<font color=" + "red>" + "Snapshot below: " + extentTest.addScreenCaptureFromPath(screenshotPath));
            } catch (IOException e) {
                BitrixLogger.debug("Failed to add scrxeenshot in extent report", e);
                Assert.fail("Failed to add screenshot in extent report", e);
            }
	}

	public void onTestSkipped(ITestResult result) {
		  String methodName = result.getMethod().getMethodName();
	        String logText = "<b>" + "TEST CASE : - " + methodName.toUpperCase()
	                + " SKipped" + "</b>";
	        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
	        extentTest.skip(m);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onTestFailedWithTimeout(ITestResult result) {

	}

	public void onStart(ITestContext context) {
		ISuite suiteInstance = context.getSuite();
		extentRpt = (ExtentReports) suiteInstance.getAttribute("extentReport");
		config = (PropertyFileConfigurations) suiteInstance.getAttribute("configurations");
		 testngBrowser = context.getCurrentXmlTest().getParameter("browservalue");
		loadBrowserDriverInstance(context, testngBrowser);
	}

	public void onFinish(ITestContext context) {
		closeBrowserDriver(context);
		context.removeAttribute("driverForBrowser");
	}

	public void loadBrowserDriverInstance(ITestContext context, String testngBrowser) {
		BrowserFactory browserFactory = new BrowserFactory();

		if (config.getLocalOrRemote().equalsIgnoreCase("local")) {
			if (suitCount == 0) {
				webdriverInstance = (WebDriver) context.getAttribute("driverForBrowser");
				if (testngBrowser == null) {
					webdriverInstance = browserFactory.getLocalBrowser(config.getBrowserName());
				} else {
					webdriverInstance = browserFactory.getLocalBrowser(testngBrowser);
				}
			}
		} else if (config.getLocalOrRemote().equalsIgnoreCase("remote")) {
			try {
				if (suitCount == 0) {
					webdriverInstance = (WebDriver) context.getAttribute("driverForBrowser");
					if (testngBrowser == null) {
						webdriverInstance = browserFactory.getRemoteBrowser(config.getBrowserName());
					} else {
						webdriverInstance = browserFactory.getRemoteBrowser(testngBrowser);
					}
				}
			} catch (Exception e) {
				 BitrixLogger.error("Failed to load remote browser", e);
				Assert.fail("Failed to load browser" +e);
			}
		}
		 if (webdriverInstance != null) {
             context.setAttribute("driverForBrowser", webdriverInstance);
             webdriverInstance.get(config.getUrl());
         }else{
         	System.out.println("Webdriver instance is null");
         }
		setTimeouts(webdriverInstance.manage());
		webdriverInstance.manage().window().maximize();
		context.setAttribute("driverForBrowser", webdriverInstance);
	}

	private void setTimeouts(Options optionsInstance) {	
		setImplicitTimeOutForBrowser(config, optionsInstance);
		setPageLoadTimeOutForBrowser(config, optionsInstance);
		setScriptTimeOutForBrowser(config, optionsInstance);
		 BitrixLogger.info("Initialized Timeout settings.");
	}

	private void setImplicitTimeOutForBrowser(PropertyFileConfigurations config, Options optionsInstance) {
		optionsInstance.timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitTimeout()));
	}

	private void setPageLoadTimeOutForBrowser(PropertyFileConfigurations config, Options optionsInstance) {
		optionsInstance.timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
	}

	private void setScriptTimeOutForBrowser(PropertyFileConfigurations config, Options optionsInstance) {
		optionsInstance.timeouts().setScriptTimeout(config.getScriptTimeout(), TimeUnit.SECONDS);
	}
	
	private void closeBrowserDriver(ITestContext context) {
		WebDriver webDriverInstance = (WebDriver) context
                .getAttribute("driverForBrowser");
        webDriverInstance.quit();
        BitrixLogger.info("Closed Browser window.");
	}

}
