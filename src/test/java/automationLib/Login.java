package automationLib;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import utils.SeleniumUtilities;
import utils.Utilities;

public class Login {

	static Logger log = Logger.getLogger(Login.class);

	public Login() {
		log.info("Initializing Web Elements...");
		PageFactory.initElements(new AjaxElementLocatorFactory(Driver.getPgDriver(), 90), this);
		this.comnutils = new Utilities();
	}

	SeleniumUtilities utils = new SeleniumUtilities();
	Utilities comnutils;

	@FindBy(name = "UserIdentifier")
	WebElement txtUserName;

	private boolean setUserName(String strUserName) {
		log.info("Enter user name = '" + strUserName + "' ...");
		return utils.enterTextinAnelemnt(txtUserName, strUserName, " Login ", " Text User Name ");
	}

	@FindBy(name = "Password")
	WebElement txtPassword;

	private boolean setPassword(String strPassword) {
		log.info("Enter Password ...");
		return utils.enterTextinAnelemnt(txtPassword, strPassword, " Login ", " Text Password ");
	}

	@FindBy(id = "sub")
	WebElement btnLogin;

	private boolean clickLogin() { // Click on login button
		log.info("Click Login ...");
		return utils.clickAnelemnt(btnLogin, " Login ", " Button Login ");
	}

	private boolean isLoginPage() {
		// TODO create logic that login page is displayed
		return true;
	}

	private boolean loginToPage(String usrname, String password) {
		log.info("Login with the user = '" + usrname + "' ...");
		/*
		 * if (openUrl()) if (isLoginPage()) if (setUserName(usrname)) if
		 * (setPassword(password)) if (clickLogin()) if (isLoginSuccessful()) return
		 * true;
		 * 
		 * log.info("Return FALSE");
		 */
		return false;

	}

	private boolean isLoginSuccessful() {
		// TODO create logic for validation login successful
		log.info("Is login successful...");
		return true;
	}

	public boolean loginToPage() {

		log.info("Login to Page ...");

		loginToPage(comnutils.getPropertyvalue("username"), comnutils.getPropertyvalue("password"));
		return isLoginSuccessful();

	}

	private boolean openUrl(String args) {

		String url = args;

		log.info("Open URL = '" + url + "' ...");

		try {
			Driver.getPgDriver().navigate().to(url);
			return utils.waitforpageload();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception = " + e.toString());
			return false;
		}

	}

	public boolean loginToPage(String[] args) {
		log.info("Login to Page with string arguments ...");
		log.info("User Name = '" + args[0]);
		return loginToPage(args[0], args[1]);
	}

	public boolean Test() {
		System.out.println("Success");
		return true;

	}

	// Ashish

	@FindBy(xpath = "//a[contains(text(),'Retail User Login')]")
	WebElement lblRetailUserLogin;

	@FindBy(xpath = "//input[contains(@name,'AuthenticationFG.USER_PRINCIPAL')]")
	WebElement txtUserID;

	@FindBy(xpath = "//input[contains(@name,'AuthenticationFG.ACCESS_CODE')]")
	WebElement txtpswd;

	@FindBy(xpath = "//input[contains(@name,'AuthenticationFG.VERIFICATION_CODE')]")
	WebElement txtVerificationCode;

	@FindBy(xpath = "//label[contains(@id,'IMAGECAPTCHA')]")
	WebElement lblCaptcha;

	@FindBy(xpath = "//input[contains(@id,'VALIDATE_CREDENTIALS')]")
	WebElement btnSubmit;

	@FindBy(xpath = "//span[contains(@class,'simpletext')][2]")
	WebElement lblVerificationQuestion;

	@FindBy(xpath = "//input[contains(@name,'AuthenticationFG.CHOSENANSWER_ARRAY_CHALLENGE[0]')]")
	WebElement txtVerificationQuestion;

	@FindBy(xpath = "//input[contains(@name,'Action.RSA_VALIDATE_ANSWERS')]")
	WebElement btnContinue;

	public boolean clickRetailLogin() {
		if (utils.waitForElementToBeVisible(lblRetailUserLogin, 10))
			if (utils.waitForElementToBeClickable(lblRetailUserLogin, 10))
				if (utils.clickAnelemnt(lblRetailUserLogin, "clickRetailLogin", "clickRetailLogin"))
					return true;
		return false;

	}

	public boolean enterUserID(String args) {
		int n = 0;
		boolean flag = false;
		while (n < 3) {
			try {
				if (utils.waitForElementToBeVisible(txtUserID, 10))
					if (utils.waitForElementToBeClickable(txtUserID, 10))
						if (utils.enterTextinAnelemnt(txtUserID, args, "txtUserID", "txtUserID", 10)) {
							System.out.println(txtUserID.getText());
							System.out.println(txtUserID.getAttribute("value"));
							if (txtUserID.getAttribute("value").equalsIgnoreCase(args)) {
								n = 3;
								flag = true;
							}
						}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			n++;
		}
		if (n == 4 && flag == true)
			return true;
		return false;

	}

	public boolean enterPassword(String args) {
		int n = 0;
		boolean flag = false;
		while (n < 3) {
			try {
				if (utils.waitForElementToBeVisible(txtpswd, 10))
					if (utils.waitForElementToBeClickable(txtpswd, 10))
						if (utils.enterTextinAnelemnt(txtpswd, args, "txtpswd", "txtpswd", 10))
							if (txtpswd.getAttribute("value").equalsIgnoreCase(args)) {
								n = 3;
								flag = true;
							}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			n++;
		}
		if (n == 4 && flag == true)
			return true;
		return false;
	}

	public boolean enterVerificationCode(String args) {
		int n = 0;
		boolean flag = false;
		while (n < 3) {
			try {
				if (utils.waitForElementToBeVisible(txtVerificationCode, 10))
					if (utils.waitForElementToBeClickable(txtVerificationCode, 10))
						if (utils.enterTextinAnelemnt(txtVerificationCode, args, "txtVerificationCode",
								"txtVerificationCode", 10))
							if (txtVerificationCode.getAttribute("value").equalsIgnoreCase(args)) {
								n = 3;
								flag = true;
							}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			n++;
		}
		if (n == 4 && flag == true)
			return true;
		return false;
	}

	public boolean clickLoginAfterCaptcha() {
		if (utils.waitForElementToBeVisible(btnSubmit, 10))
			if (utils.waitForElementToBeClickable(btnSubmit, 10))
				if (utils.clickAnelemnt(btnSubmit, "btnSubmit", "btnSubmit"))
					return true;
		return false;
	}

	public boolean getCaptcha() {
		if (utils.waitForElementToBeVisible(lblCaptcha, 10))
			if (utils.waitForElementToBeClickable(lblCaptcha, 10)) {
				String str = lblCaptcha.getText();
				System.out.println("str value: " + str);
				if (str.contains("smallest")) {
					System.out.println(str.length());
					System.out.println(str.indexOf("value "));
					String str2 = str.substring(str.indexOf("value ") + 6, str.length());
					String splitStr = str2.replace("?", "");
					System.out.println(splitStr);
					String[] arr = splitStr.split(",");
					// int[] numbers = new int[arr.length];

					int[] values = Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
					for (int k = 0; k < values.length; k++)
						System.out.println(values[k]);

					int minValue = values[0];

					for (int i = 0; i < values.length; i++) {
						if (values[i] < minValue)
							minValue = values[i];
					}
					System.out.println("minValue :" + minValue);
					if (enterVerificationCode(String.valueOf(minValue)))
						return true;
					return false;
				} else if (str.contains("result")) {
					if (str.contains("+")) {
						str.length();
						System.out.println(str.length());
						System.out.println(str.indexOf("result of "));
						String str2 = str.substring(str.indexOf("result of") + 10, str.length());
						System.out.println(str2);
						String splitStr = str2.replace("?", "");
						System.out.println(splitStr);
						String[] arr = splitStr.split("\\+");
						// int[] numbers = new int[arr.length];
						int[] values = Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
						for (int k = 0; k < values.length; k++)
							System.out.println(values[k]);

						int sum = 0;
						int i;

						for (i = 0; i < values.length; i++)
							sum += values[i];

						System.out.println("sum :" + sum);
						if (enterVerificationCode(String.valueOf(sum)))
							return true;
						return false;
					}
				} else if (str.contains("largest") || str.contains("biggest")) {
					System.out.println(str.length());
					System.out.println(str.indexOf("value"));
					String str2 = str.substring(str.indexOf("value ") + 6, str.length());
					String splitStr = str2.replace("?", "");
					System.out.println(splitStr);
					String[] arr = splitStr.split(",");
					// int[] numbers = new int[arr.length];

					int[] values = Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
					for (int k = 0; k < values.length; k++)
						System.out.println(values[k]);

					int max = values[0];
					for (int i = 1; i < values.length; i++)
						if (values[i] > max)
							max = values[i];

					System.out.println("max :" + max);
					if (enterVerificationCode(String.valueOf(max)))
						return true;
					return false;

				}
			}
		return false;
	}

	public boolean enterVerificationQuestionValue(String args) {
		int n = 0;
		boolean flag = false;
		while (n < 3) {
			try {
				if (utils.waitForElementToBeVisible(txtVerificationQuestion, 10))
					if (utils.waitForElementToBeClickable(txtVerificationQuestion, 10))
						if (utils.enterTextinAnelemnt(txtVerificationQuestion, args, "txtVerificationQuestion",
								"txtVerificationQuestion", 10))
							if (txtVerificationQuestion.getAttribute("value").equalsIgnoreCase(args)) {
								n = 3;
								flag = true;
							}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			n++;
		}
		if (n == 4 && flag == true)
			return true;
		return false;
	}

	public boolean getVerificationQuestion() {
		if (utils.waitForElementToBeVisible(lblVerificationQuestion, 10))
			if (utils.waitForElementToBeClickable(lblVerificationQuestion, 10)) {
				String str = lblVerificationQuestion.getText();
				System.out.println("str value: " + str);
				if (str.contains("name of the first company you worked for")) {
					if (enterVerificationQuestionValue("TCS"))
						return true;
					return false;
				}
			}
		return false;
	}

	public boolean clickContinue() {
		if (utils.waitForElementToBeVisible(btnContinue, 10))
			if (utils.waitForElementToBeClickable(btnContinue, 10))
				if (utils.clickAnelemnt(btnContinue, "btnContinue", "btnContinue"))
					return true;
		return false;
	}

	// wrappper method

	public boolean openURLAndEnterloginDetailsAndSubmit(String[] args) {
		if (!openUrl(args[0]))
			return false;
		if (!clickRetailLogin())
			return false;
		if (!enterUserID(args[1]))
			return false;
		if (!enterPassword(args[2]))
			return false;
		if (!getCaptcha())
			return false;
		if (!clickLoginAfterCaptcha())
			return false;
		if(!utils.isAlertPresent())
			return false;
		/*
		 * if (!getVerificationQuestion()) return false; if (!clickContinue()) return
		 * false;
		 */
		return true;
	}

}
