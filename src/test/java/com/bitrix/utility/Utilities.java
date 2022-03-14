package com.bitrix.utility;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Utilities {
	
	public static void tempMethodForThreadSleep(int millis) {
		try {
			BitrixLogger.info("Thread.sleep for " + millis + " milliseconds.");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			BitrixLogger.error("Interrupted Exception : ", e);
			Thread.currentThread().interrupt();
		}
	}
	
	
	public static String captureScreenshotWithRobot(String testMethodName, String path) {
		try {
			Robot robotClassObject = new Robot();
			Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage tmp = robotClassObject.createScreenCapture(screenSize);

			path = new Utilities().buildScreenshotFilePath(path, testMethodName);
			File screenshotFile = new File(path);
			FileUtils.touch(screenshotFile);
			// To copy temp image in to permanent file
			ImageIO.write(tmp, "png", screenshotFile);
		} catch (Exception e) {
			//BitrixLogger.fatal("Screenshot using Robot Failed :", e);
		}

		return path;
	}
	
	private String buildScreenshotFilePath(String path, String testMethodName) {
		path = System.getProperty("user.dir") + "\\" + path + getCurrentDate() + "\\temp";
		String fileName = testMethodName + "_" + UUID.randomUUID() + ".png";
		return path + fileName;
	}
	
	public String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public static void javaScriptClick(WebDriver driver, WebElement btnAddImage) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", btnAddImage);
		BitrixLogger.info("Clicked on element by using java script executor");
	}

}
