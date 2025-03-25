package com.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.qameta.allure.Allure;

public class Listener extends Baseutility implements ITestListener {
	ExtentReports extent = getreports();
	ThreadLocal<ExtentTest> extent_test = new ThreadLocal<ExtentTest>();

	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName() + " = " + result.getMethod().getMethodName());
		extent_test.set(test);
	}

	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, "Test Case Pass");
		extent_test.set(test);
	}

	public void onTestFailure(ITestResult result) {
		test.log(Status.FAIL, "Test Case Fail");
		try {
			test.addScreenCaptureFromPath(getCapture(result.getName()));
			get_Screen_shot(result.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		extent_test.set(test);
	}

	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, "Test Case Skip");
		try {
			test.addScreenCaptureFromPath(getCapture(result.getName()));
			get_Screen_shot(result.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		extent_test.set(test);
	}

	public void onFinish(ITestContext context) {
		extent.flush();
	}

	public String getCapture(String screenshot_name) {
		String destination = System.getProperty("user.dir") + "/failed_Tests_ScreenShots/" + screenshot_name
				+ date_and_Time("dd-MM-yyyy_hh mm ss") + ".png";
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			File finaldestination = new File(destination);
			FileUtils.copyFile(source, finaldestination);
			if (finaldestination.exists()) {
				System.out.println("Screenshot saved at: " + finaldestination.getAbsolutePath());
			} else {
				System.out.println("Screenshot not saved.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destination;
	}

	public String get_Screen_shot(String screenshot_name) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			String source = ts.getScreenshotAs(OutputType.BASE64);
			Allure.addAttachment(screenshot_name,
					new ByteArrayInputStream(java.util.Base64.getDecoder().decode(source)));
			System.out.println("Screenshot attached to Allure report.");

		} catch (Exception e) {

			Message("Screenshot attached to Allure report." + e.getMessage());
			System.out.println("Screenshot not attached due to an error.");
		}
		return screenshot_name;
	}

	public String date_and_Time(String format) {
		String value = "";
		try {
			Date db = new Date();
			DateFormat df = new SimpleDateFormat(format);
			value = df.format(db);
		} catch (Exception e) {
			System.out.println("Problem in date and time: " + e);
		}
		return value;
	}

}
