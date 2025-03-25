package com.pageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.utility.Baseutility;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginPage extends Baseutility {

	public LoginPage() {
		PageFactory.initElements(driver, this);
	}

	@AndroidFindBy(xpath = "//android.widget.TextView[@resource-id ='com.customerapp.hero:id/btn_lbl']")
	private WebElement process_require_notification;

	public WebElement process_require_notification() {
		return process_require_notification;
	}

	@AndroidFindBy(xpath = "//android.widget.Button[@resource-id ='com.android.permissioncontroller:id/permission_allow_button']")
	private WebElement Allow;

	public WebElement Allow() {
		return Allow;
	}

	@AndroidFindBy(xpath = "//android.widget.Button[@text='OK']")
	private WebElement ok;

	public WebElement ok() {
		return ok;
	}

}
