package org.j2.faxqa.efax.efax_us.myaccount.pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.j2.faxqa.efax.common.BasePage;
import org.j2.faxqa.efax.common.TLDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigationBar extends BasePage {

	private WebDriver driver;
	private Logger logger;
	WebDriverWait wait;

	public NavigationBar() {
		this.driver = TLDriverFactory.getTLDriver();
		this.logger = LogManager.getLogger();
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, 30);
		logger.info(driver.getTitle() + " - [" + driver.getCurrentUrl() + "]");
	}

	static final String sendFaxesTab = "sendfaxestab";
	static final String accountDetailsTab = "updateaccttab";
	static final String viewFaxesTab = "viewfaxestab";
	static final String ReportsTab = "reportstab";
	
	@FindBy(id = sendFaxesTab)
	private WebElement sendFaxesTabWebElement;

	@FindBy(id = accountDetailsTab)
	private WebElement accountDetailsTabWebElement;

	@FindBy(id = viewFaxesTab)
	private WebElement viewFaxesTabWebElement;

	@FindBy(id = ReportsTab)
	private WebElement ReportsTabWebElement;

	public void clickSendFaxesTab() {
		wait.until(ExpectedConditions.elementToBeClickable(sendFaxesTabWebElement));
		sendFaxesTabWebElement.click();
	}

	public void clickAccountDetailsTab() {
		wait.until(ExpectedConditions.elementToBeClickable(accountDetailsTabWebElement));
		accountDetailsTabWebElement.click();
	}

	public void clickViewFaxesTab() {
		wait.until(ExpectedConditions.elementToBeClickable(viewFaxesTabWebElement));
		viewFaxesTabWebElement.click();
	}

	public void clickReportsTab() {
		wait.until(ExpectedConditions.elementToBeClickable(ReportsTabWebElement));
		ReportsTabWebElement.click();
	}

}