package PageTest;

import com.pageObject.LoginPage;
import com.utility.Baseutility;

public class LoginPageTest extends Baseutility {

	public LoginPage ob;

	public void TC008_invalid_mobile_no() {
		Message("************************Login page test**************************");
		ob = new LoginPage();
		try {
			if (ob.process_require_notification().isDisplayed())
				Custom_click(ob.process_require_notification(), "Process require notification");
			Custom_click(ob.Allow(), " Allow");
			Custom_click(ob.ok(), "OK");
		} catch (Exception e) {
			Message("not able to handle");
		}
	}

}
