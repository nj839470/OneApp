package com.utility;

import java.io.FileInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;

public class Baseutility implements ConfigureDataProvider, ExtentReportsGenerator, Library {

	public static AndroidDriver driver;
	public static Logger log;
	public static ExtentSparkReporter report;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Listener lis;
	public static WebDriverWait wait;
	String enveronment = config_getdata("env");
	String confipath = System.getProperty("user.dir") + "/Config_Data/config.properties";
	public static String userName = "nitesh.jhaintellicredence";
	public static String accessKey = "LT_pdTpLmaeK7kbQOAdzZ6B28TN3ZQe0AMG5sByOIKzxWq0ngT";

	@BeforeClass
	public void OPEN_AND_INSTALL_APP() {
		String deviceName = config_getdata("Platform_name");
		if (deviceName.equalsIgnoreCase("emulator")) {
			try {
				UiAutomator2Options options = new UiAutomator2Options();
				options.setCapability("appium:automationName", "uiautomator2");
				options.setCapability("platformName", "Android");
				options.setCapability("appium:deviceName", "Pixel_6_API_31");
				options.setCapability("appium:udid", "emulator-5554");
				options.setCapability("appium:avdLaunchTimeout", 90000);
				options.setCapability("appium:app", System.getProperty("user.dir") + "\\apk\\app-debug.apk");
				options.setCapability("appium:ensureWebviewsHavePages", true);
				options.setCapability("appium:nativeWebScreenshot", true);
				options.setCapability("appium:newCommandTimeout", 9600);

				driver = new AndroidDriver(new URL(config_getdata("IpAddress")), options);
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));

				log = LogManager.getLogger("One_App");
				lis = new Listener();
			} catch (Exception e) {
				log.error("Failed to open and install app: " + e.getMessage(), e);
			}
		} else if (deviceName.equalsIgnoreCase("lambda")) {
			try {
				DesiredCapabilities capabilities = new DesiredCapabilities();
				HashMap<String, Object> ltOptions = new HashMap<String, Object>();
				ltOptions.put("w3c", true);
				ltOptions.put("platformName", "android");
				ltOptions.put("deviceName", "Galaxy S23");
				if (enveronment.equalsIgnoreCase("prod")) {
					ltOptions.put("app", "lt://APP1016045521739865906757131"); // lt://APP10160611311739771445927194
																				// lt://APP1016045521739865906757131
				} else {
					ltOptions.put("app", "lt://APP10160502331742815991662736");
				}
				ltOptions.put("autoGrantPermissions", true);
				ltOptions.put("isRealMobile", true);
				ltOptions.put("appiumVersion", "2.0.0");
				ltOptions.put("project", "One_App");
				ltOptions.put("build", "9.2 version");
				ltOptions.put("name", "Test");
				capabilities.setCapability("newCommandTimeout", 300);
				capabilities.setCapability("lt:options", ltOptions);

				driver = new AndroidDriver(
						new URL("https://" + userName + ":" + accessKey + "@mobile-hub.lambdatest.com/wd/hub"),
						capabilities);
				driver.activateApp("com.customerapp.hero");
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
				log = LogManager.getLogger("One_App");
				lis = new Listener();
			} catch (Exception e) {
				e.printStackTrace();
				Message(e.getMessage());
			}
		} else if (deviceName.equalsIgnoreCase("realdevice")) {
			UiAutomator2Options db = new UiAutomator2Options();
			db.setCapability("appium:automationName", "uiautomator2");
			db.setCapability("platformName", "Android");
			db.setCapability("appium:deviceName", "OPPO A59 5G");
			db.setCapability("appium:udid", "45WSHQNNNZMJ8DTO");
			db.setCapability("appPackage", "com.customerapp.hero");
			db.setCapability("appActivity", "com.customerapp.hero.views.activity.HmcDashboard");
			db.setCapability("appium:noReset", true); // Prevents clearing app data
			db.setCapability("appium:ignoreHiddenApiPolicyError", true);
			db.setCapability("appium:ensureWebviewsHavePages", true);
			db.setCapability("appium:nativeWebScreenshot", true);
			db.setCapability("appium:newCommandTimeout", 6600);

			try {
				driver = new AndroidDriver(new URL(config_getdata("IpAddress")), db);
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
				log = LogManager.getLogger("One_App");
				lis = new Listener();
			} catch (Exception e) {
				System.out.println("Session creation failed: " + e.getMessage());
			}

		}

	}

	@Override
	public void custom_sendkeys(WebElement element, String value, String fieldname) {
		try {
			if (element.isEnabled() || element.isDisplayed() == true) {
				wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				wait.until(ExpectedConditions.visibilityOf(element));
				element.click();
				element.sendKeys(value);
				test.log(Status.PASS, fieldname + " send successfully =" + value);
				log.info(fieldname + " send successfully");
				Allure.addAttachment("Log", fieldname + " send successfully with value: " + value);
			}
		} catch (Exception e) {
			if (lis != null) {
				test.log(Status.FAIL, fieldname + "= is not able to send = "
						+ test.addScreenCaptureFromPath(lis.getCapture(fieldname)));
				lis.get_Screen_shot(fieldname);
			} else {
				test.log(Status.FAIL, fieldname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(fieldname + " is not able to send: " + e.getMessage());
			Allure.addAttachment("Error Log", fieldname + " failed to send with error: " + e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@Override
	public void Custom_click(WebElement element, String fieldname) {
		try {
			if (element.isDisplayed() || element.isEnabled() == true) {
				wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				wait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
				test.log(Status.PASS, "Successfully click on = " + fieldname);
				log.info(fieldname + " is clickable");
				Allure.addAttachment("Log", " Successfully click on = " + fieldname);
			}
		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(fieldname);
				test.log(Status.FAIL,
						fieldname + "= Unable to Click = " + test.addScreenCaptureFromPath(lis.getCapture(fieldname)));
			} else {
				test.log(Status.FAIL, fieldname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(fieldname + " is not clickable: " + e.getMessage());
			Allure.addAttachment("Error Log", fieldname + " Unable to Click with error: " + e.getMessage());
			throw new RuntimeException(e);

		}

	}

	@Override
	public void msg(WebElement ele, String filedname) {
		try {
			if (ele.isDisplayed()) {
				wait = new WebDriverWait(driver, Duration.ofSeconds(20));
				wait.until(ExpectedConditions.visibilityOf(ele));
				String mes = ele.getText();
				test.log(Status.PASS, filedname + mes);
				log.info(filedname + mes);
				Allure.addAttachment("Log", filedname + mes);
			}
		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(filedname);
				test.log(Status.FAIL,
						filedname + " is not readable = " + test.addScreenCaptureFromPath(lis.getCapture(filedname)));
			} else {
				test.log(Status.FAIL, filedname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(filedname + " is not readable: " + e.getMessage());
			Allure.addAttachment("Error Log", filedname + " is not readable with error " + e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@Override
	public void Message(String message) {
		test.log(Status.PASS, message);
		log.info(message);
		Allure.addAttachment("Log", message);
	}

	@Override
	public void Delete_message(WebElement ele, String Field_name) {
		try {
			if (ele.isDisplayed() || ele.isEnabled() == true) {
				ele.clear();
				test.log(Status.PASS, Field_name + " is cleared");
				log.info(Field_name + "is cleared");
				Allure.addAttachment("Log", Field_name + " is cleared");
			}
		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(Field_name);
				test.log(Status.FAIL, Field_name + " is not able to clear = "
						+ test.addScreenCaptureFromPath(lis.getCapture(Field_name)));
			} else {
				test.log(Status.FAIL, Field_name + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(Field_name + " is not able to clear: " + e.getMessage());
			Allure.addAttachment("Error Log", Field_name + " is not able to clear with error " + e.getMessage());

		}

	}

	@Override
	public ExtentReports getreports() {
		String currenttime = new SimpleDateFormat("dd.MM.YYYY.HH.mm.ss").format(new Date());
		String path = System.getProperty("user.dir") + "/Report/Test-Report -" + currenttime + ".html";
		report = new ExtentSparkReporter(path);
		report.config().setDocumentTitle("One_App Test Report");
		report.config().setReportName("One_App");
		report.config().setTheme(Theme.DARK);
		extent = new ExtentReports();
		extent.attachReporter(report);
		extent.setSystemInfo("Project Name", "One App");
		extent.setSystemInfo("Laptop", "MacBook Pro intel core i7");
		extent.setSystemInfo("QA", "Nitesh Kumar");
		extent.setSystemInfo("Operating system", "Sonoma 14.5");
		extent.setSystemInfo("BrowserName", "Android Studio");
		return extent;
	}

	@Override
	public String config_getdata(String key) {
		String value = "";
		try {
			FileInputStream fis = new FileInputStream(confipath);
			Properties prop = new Properties();
			prop.load(fis);
			value = prop.getProperty(key);

		} catch (Exception e) {
			System.out.println("Problem in read data from property file" + e);
		}
		return value;
	}

	public void Scroll_down_page_Action_Half(String fieldname) {
		try {
			Dimension dim = driver.manage().window().getSize();
			int startX = dim.width / 2;
			int startY = (int) (dim.height / 2);
			int endY = (int) (dim.height * 0.25);
			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence scroll = new Sequence(finger, 0);
			scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
			scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scroll.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
			scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

			driver.perform(Collections.singletonList(scroll));

			test.log(Status.PASS, "Successfully scrolled down. Action = " + fieldname);
			log.info("Successfully scrolled down. Action: " + fieldname);
			Allure.addAttachment("Log", "Scroll action success: " + fieldname);

		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(fieldname);
				test.log(Status.FAIL, fieldname + "= Unable To Scroll page Action = "
						+ test.addScreenCaptureFromPath(lis.getCapture(fieldname)));
			} else {
				test.log(Status.FAIL, fieldname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(fieldname + " Unable To Scroll page Action: " + e.getMessage());
			Allure.addAttachment("Error Log",
					fieldname + " Unable To Scroll page action with error: " + e.getMessage());
		}
	}

	public void Scroll_down_page_Action_Full(String fieldname) {
		try {
			Dimension dim = driver.manage().window().getSize();
			int startX = dim.width / 2;
			int startY = (int) (dim.height * 0.8);
			int endY = (int) (dim.height * 0.25);

			PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
			Sequence scroll = new Sequence(finger, 0);
			scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
			scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
			scroll.addAction(
					finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
			scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

			driver.perform(Collections.singletonList(scroll));

			test.log(Status.PASS, "Successfully scrolled down. Action = " + fieldname);
			log.info("Successfully scrolled down. Action: " + fieldname);
			Allure.addAttachment("Log", "Scroll action success: " + fieldname);

		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(fieldname);
				test.log(Status.FAIL, fieldname + "= Unable To Scroll page Action = "
						+ test.addScreenCaptureFromPath(lis.getCapture(fieldname)));
			} else {
				test.log(Status.FAIL, fieldname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(fieldname + " Unable To Scroll page Action: " + e.getMessage());
			Allure.addAttachment("Error Log",
					fieldname + " Unable To Scroll page action with error: " + e.getMessage());
		}
	}

	public void doubleFingerScroll(String fieldname) {
		try {
			Dimension dim = driver.manage().window().getSize();
			int startX1 = (int) (dim.width / 2);
			int startY1 = (int) (dim.height / 2);
			int endX1 = (int) (dim.width * 0);
			int endY1 = (int) (dim.height * 0);
			int startX2 = (int) (dim.width / 2);
			int startY2 = (int) (dim.height / 2);
			int endX2 = (int) (dim.width * 0);
			int endY2 = (int) (dim.height * 0);
			TouchAction finger1 = new TouchAction(driver).press(PointOption.point(startX1, startY1))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
					.moveTo(PointOption.point(endX1, endY1)).release();

			TouchAction finger2 = new TouchAction(driver).press(PointOption.point(startX2, startY2))
					.waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
					.moveTo(PointOption.point(endX2, endY2)).release();

			MultiTouchAction multiTouch = new MultiTouchAction(driver);
			multiTouch.add(finger1).add(finger2);
			multiTouch.perform();
		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(fieldname);
				test.log(Status.FAIL, fieldname + "= Unable To Scroll page Action = "
						+ test.addScreenCaptureFromPath(lis.getCapture(fieldname)));
			} else {
				test.log(Status.FAIL, fieldname + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(fieldname + " Unable To Scroll page Action: " + e.getMessage());
			Allure.addAttachment("Error Log",
					fieldname + " Unable To Scroll page action with error: " + e.getMessage());
		}
	}

	public void scrollByText(String menuText) {
		try {
			driver.findElement(MobileBy.AndroidUIAutomator(
					"new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\""
							+ menuText + "\").instance(0));"));
			log.info("Successfully scroll by text " + menuText);
			test.log(Status.PASS, "Successfully scroll by text " + menuText);
			Allure.addAttachment("Log", " Successfully scroll by text " + menuText);
		} catch (Exception e) {
			if (lis != null) {
				lis.get_Screen_shot(menuText);
				test.log(Status.FAIL, menuText + "= unable To scroll by text "
						+ test.addScreenCaptureFromPath(lis.getCapture(menuText)));
			} else {
				test.log(Status.FAIL, menuText + "= Unable to capture screenshot, 'lis' is null.");
			}
			test.log(Status.FAIL, e);
			log.error(menuText + "Unable to scroll by text: " + e.getMessage());
			Allure.addAttachment("Error Log", menuText + " Unable to scroll by text: " + e.getMessage());
		}
	}

}
