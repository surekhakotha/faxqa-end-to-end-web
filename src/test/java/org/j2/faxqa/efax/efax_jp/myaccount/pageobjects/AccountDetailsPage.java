package org.j2.faxqa.efax.efax_jp.myaccount.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;

//import com.google.common.*;
//import com.google.common.io.Files;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.*;
import org.j2.faxqa.efax.common.BasePage;
import org.j2.faxqa.efax.common.TLDriverFactory;

public class AccountDetailsPage extends BasePage {
	private WebDriver driver;
	private Logger logger;
	WebDriverWait wait;

	public AccountDetailsPage() {
		this.driver = TLDriverFactory.getTLDriver();
		this.logger = LogManager.getLogger();
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, 30);
		logger.info(driver.getTitle() + " - [" + driver.getCurrentUrl() + "]");
	}

	@FindBy(id = "tabs-prefs")
	private WebElement tabprefs;

	@FindBy(id = "tabs-billing")
	private WebElement tabbilling;

	@FindBy(id = "tabs-usage")
	private WebElement tabusage;

	@FindBy(id = "tabs-profile")
	private WebElement tabprofile;

	@FindBy(xpath = "//div[contains(text(),'送信ファックス オプション:')]/..//a")
	private WebElement sendfaxoptionsedit;

	@FindBy(id = "txt_sendCSID")
	private WebElement sendCSID;

	@FindBy(xpath = "//form[@id='form_sendPrefs']//input[@alt='Update']")
	private WebElement update;

	@FindBy(id = "myaccthometab")
	private WebElement myaccthometab;

	@FindBy(id = "chk_deliverFaxReceipts")
	private WebElement deliverFaxReceipts;

	@FindBy(id = "sel_defaultEmailAddress")
	private WebElement defaultEmailAddress;

	@FindBy(id = "//*[contains(text(),'受信ファックス履歴:')]/..//a")
	private WebElement lnkUsageActivityLogReceive;

	@FindBy(xpath = "//*[contains(text(),'送信ファックス履歴:')]/..//a")
	private WebElement lnkUsageActivityLogSent;

	@FindBy(id = "receive_usageGrid")
	private WebElement receive_usageGrid;

	@FindBy(id = "send_usageGrid")
	private WebElement send_usageGrid;

	@FindBy(id = "btn_sendLog")
	private List<WebElement> btn_receiveLog;

	@FindBy(id = "btn_sendLog")
	private WebElement btn_sendLog;

	@FindBy(id = "date_sendToDate")
	private WebElement date_sendToDate;

	@FindBy(id = "date_receiveToDate")
	private WebElement date_receiveToDate;

	private void setSendDate() {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date(System.currentTimeMillis()));
		((JavascriptExecutor) this.driver).executeScript("document.getElementById('date_sendToDate').value = '" + today + "';", date_sendToDate);
	}

	private void setReceiveDate() {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date(System.currentTimeMillis()));
		((JavascriptExecutor) this.driver).executeScript("document.getElementById('date_receiveToDate').value = '" + today + "';", date_receiveToDate);
	}

	public void updatesendCSID(String sender) {
		wait.until(ExpectedConditions.elementToBeClickable(sendfaxoptionsedit));
		sendfaxoptionsedit.click();
		sendCSID.clear();
		sendCSID.sendKeys(sender);
		setdeliverFaxReceipts(false);
		setdefaultEmailAddress();
		update.click();
		logger.info("CSID is set to " + sender);
		wait.until(ExpectedConditions.elementToBeClickable(myaccthometab));
		myaccthometab.click();
	}

	private void setdeliverFaxReceipts(boolean check) {
		if (check && !deliverFaxReceipts.isSelected())
			deliverFaxReceipts.click();
		else if (!check && deliverFaxReceipts.isSelected())
			deliverFaxReceipts.click();
		logger.info("coverpage option enabled.");
	}

	private void setdefaultEmailAddress() {
		Select receipt = new Select(defaultEmailAddress);
		receipt.selectByIndex(1);
	}

	public void clickUsageTab() {
		tabusage.click();
	}

	public void clickSendActivityDetails() {
		lnkUsageActivityLogSent.click();
	}

	public void clickReceiveActivityDetails() {
		lnkUsageActivityLogReceive.click();
	}

	public void clickReceiveGo() {
		wait.until(ExpectedConditions.elementToBeClickable(btn_receiveLog.get(1)));
		btn_receiveLog.get(1).click();
	}

	public void clickSendGo() {
		wait.until(ExpectedConditions.elementToBeClickable(btn_sendLog));
		btn_sendLog.click();
	}


	public boolean isReceiveActivityLogFound(String senderid, int timeout) throws InterruptedException {
		clickReceiveGo();

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='load_receive_usageGrid' and text()='Loading...']")));
		Instant waittime = Instant.now().plusSeconds(timeout);
		logger.info("Expected Receive Activity Log Timeout set to " + timeout + " seconds.");
		WebElement log = null;
		while (log == null && waittime.isAfter(Instant.now())) {
			logger.info("Waiting for the log record...");
			Thread.sleep(30000);
			clickReceiveGo();
			log = getReceiveRecord(senderid);
		}

		if (log != null) {
			printReceiveActivityLog(log);
			return true;
		} else {
			logger.warn("Wait '" + timeout + "' seconds timed-out, expected log not found.");
			return false;
		}

	}

	public void printReceiveActivityLog(WebElement log) {
		logger.info("Expected fax received successfully.");
		logger.info("Date = " + log.findElements(By.tagName("td")).get(0).getText());
		logger.info("Pages = " + log.findElements(By.tagName("td")).get(1).getText());
		logger.info("Type = " + log.findElements(By.tagName("td")).get(2).getText());
		logger.info("Duration = " + log.findElements(By.tagName("td")).get(3).getText());
		logger.info("From = " + log.findElements(By.tagName("td")).get(4).getText());
	}

	private WebElement getReceiveRecord(String senderid)
	{
		if (driver.findElements(By.xpath("//*[@id='receive_usageGrid']//td[contains(text(),'" + senderid + "')]/..")).size() > 0)
			return driver.findElement(By.xpath("//*[@id='receive_usageGrid']//td[contains(text(),'" + senderid + "')]/.."));
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private WebElement getSendRecord(String senderid)
	{
		if (driver.findElements(By.xpath("//*[@id='send_usageGrid']//td[contains(text(),'" + senderid + "')]/..")).size() > 0)
			return driver.findElement(By.xpath("//*[@id='send_usageGrid']//td[contains(text(),'" + senderid + "')]/.."));
		return null;
	}
	
	public boolean isSendActivityLogFound(String senderid, int timeout) throws InterruptedException {
		clickUsageTab();
		clickSendActivityDetails();
		setSendDate();
		clickSendGo();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='load_send_usageGrid' and text()='Loading...']")));
		Instant waittime = Instant.now().plusSeconds(timeout);
		logger.info("Expected Send Activity Log Timeout set to " + timeout + " seconds.");
		WebElement log = null;
		while (log == null && waittime.isAfter(Instant.now())) {
			logger.info("Waiting for the log record...");
			Thread.sleep(30000);
			clickSendGo();
			log = getSendRecord(senderid);
		}

		if (log != null) {
			printSendActivityLog1(log);
			return true;
		} else {
			logger.warn("Wait '" + timeout + "' seconds timed-out, expected log not found.");
			return false;
		}

	}

	public void printSendActivityLog1(WebElement log) {
		logger.info("Expected fax received successfully.");
		logger.info("Date = " + log.findElements(By.tagName("td")).get(0).getText());
		logger.info("To = " + log.findElements(By.tagName("td")).get(1).getText());
		logger.info("Pages = " + log.findElements(By.tagName("td")).get(2).getText());
		logger.info("Charge = " + log.findElements(By.tagName("td")).get(3).getText());
		logger.info("Subject = " + log.findElements(By.tagName("td")).get(4).getText());
		logger.info("Status = " + log.findElements(By.tagName("td")).get(5).getText());
	}

	public void switchToReceiveLogs() {
		driver.findElement(By.xpath("//a[text()='受信履歴を見る']")).click();
	}
}