package com.utility;

import org.openqa.selenium.WebElement;

public interface Library {

	public void custom_sendkeys(WebElement element, String value, String fieldname);

	public void Custom_click(WebElement element, String fieldname);

	public void msg(WebElement ele, String filedname);

	public void Message(String message);

	public void Delete_message(WebElement ele, String Field_name);

	public void Scroll_down_page_Action_Full(String fieldname);

	public void Scroll_down_page_Action_Half(String fieldname);

	public void scrollByText(String menuText);

	public void doubleFingerScroll(String fieldname);
}
