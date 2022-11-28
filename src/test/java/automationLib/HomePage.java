package automationLib;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import utils.SeleniumUtilities;
import utils.Utilities;

public class HomePage {

	static Logger log = Logger.getLogger(Login.class);

	public HomePage() {
		log.info("Initializing Web Elements...");
		PageFactory.initElements(new AjaxElementLocatorFactory(Driver.getPgDriver(), 90), this);
		this.comnutils = new Utilities();
	}

	SeleniumUtilities utils = new SeleniumUtilities();
	Utilities comnutils;

	@FindBy(xpath = "//a[text()='Accounts']")
	WebElement lnkAccounts;

	@FindBy(xpath = "//a[text()='Balance & Transaction Info']")
	WebElement lnkBalanceAndTransactionInfo;

	@FindBy(xpath = "//a[text()='Accounts Summary']")
	WebElement lnkAccountsSummary;

	@FindBy(xpath = "//h1[contains(text(),'Account Summary')]")
	WebElement lblAccountSummary;

	public boolean openAccountSummary() {
		if (utils.waitforpageload())
			if (utils.waitForElementToBeVisible(lnkAccounts, 90))
				if (utils.waitForElementToBeClickable(lnkAccounts, 90))
					if (utils.clickAnelemnt(lnkAccounts, "lnkAccounts", "lnkAccounts"))
						if (utils.waitForElementToBeVisible(lnkBalanceAndTransactionInfo, 90))
							if (utils.waitForElementToBeClickable(lnkBalanceAndTransactionInfo, 90))
								if (utils.clickAnelemnt(lnkBalanceAndTransactionInfo, "lnkBalanceAndTransactionInfo",
										"lnkBalanceAndTransactionInfo"))
									if (utils.waitForElementToBeVisible(lnkAccountsSummary, 90))
										if (utils.waitForElementToBeClickable(lnkAccountsSummary, 90))
											if (utils.clickAnelemnt(lnkAccountsSummary, "lnkAccountsSummary",
													"lnkAccountsSummary"))
												return true;
		return false;
	}

	public boolean validateAccountSummaryHeader() {
		if (utils.waitForElementToBeVisible(lblAccountSummary, 90))
			if (utils.waitForElementToBeClickable(lblAccountSummary, 90))
				if (utils.clickAnelemnt(lblAccountSummary, "lblAccountSummary", "lblAccountSummary"))
					return true;
		return false;
	}

	public String getBalanceValue(String args) {
		WebElement we = Driver.pgDriver.findElement(
				By.xpath("//a[contains(text(),'" + args + "')]/../..//td//span[contains(@id,'actBalOutput')]"));
		if (utils.waitForElementToBeVisible(we, 90))
			if (utils.waitForElementToBeClickable(we, 90)) {
				System.out.println(we.getText());
				return we.getText();
			}
		return we.getText();
	}

	public boolean openAcctSummaryAndGetBal(String[] args) {
		if (!openAccountSummary())
			return false;
		if (!validateAccountSummaryHeader())
			return false;
		String str = getBalanceValue(args[0]);
		System.out.println("str value: " + str);
		SendMail objSendMail = new SendMail();
		objSendMail.sendMail(args[0], str);
		return true;

	}

}
