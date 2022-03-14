package com.bitrix.base;


import java.io.File;

import org.bitrix.datadriven.DataDrivenScript;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.bitrix.utility.BitrixLogger;
import com.bitrix.utility.PropertyFileConfigurations;

public class Baseclassparallel {
	
    public WebDriver                browserDriver;
    protected PropertyFileConfigurations config;
    protected DataDrivenScript         DrivenScript;
    public  String path;
    public  String imagePath;
    
    {
    	path = System.getProperty("user.dir");
    	imagePath = path + File.separator + "TestData" + File.separator + "images";
    }
    
    @BeforeSuite
    public void preProcessor(ITestContext context) {
        ISuite suite = context.getSuite();
        browserDriver = (WebDriver) context.getAttribute("driverForBrowser");
        DrivenScript = new DataDrivenScript();
        config = (PropertyFileConfigurations) suite
                .getAttribute("configurations");
        BitrixLogger.setClass(this);
        BitrixLogger.startTestCase(this.getClass().getSimpleName());
    }
    
    
    @AfterSuite
    public void postProcessor() {
    	BitrixLogger.endTestCase(this.getClass().getSimpleName());
    }
    
    public WebDriver getBrowserDriver() {
        return browserDriver;
    }
    
    public PropertyFileConfigurations getConfig() {
        return config;
    }
    
    public DataDrivenScript getExcelDataDriven() {
        return DrivenScript;
    }
    
    
    
}
