package com.auto.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ActionDriver{

	// initialize the app logs
	public static Logger APP_LOGS = Logger.getLogger("devpinoyLogger");
	
	public static Properties CONFIG;
	public static Properties OBJECT;
	
	protected WebDriver _driver;

	public ActionDriver(WebDriver driver) {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.Jdk14Logger");
		try {
			loading_config_sys();
			loading_object_repository();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this._driver=driver;
	}
	
	// Private
	public static By getLocator(String locator) {
		By by = null;

		if (locator.startsWith("id=")) {

			locator = locator.substring(3);
			by = By.id(locator);

		} else if (locator.startsWith("name=")) {
										
			locator = locator.substring(5);
			by = By.name(locator);

		} else if (locator.startsWith("css=")) {

			locator = locator.substring(4);
			by = By.cssSelector(locator);

		} else if (locator.startsWith("link=")) {

			locator = locator.substring(5);
			by = By.linkText(locator);

		} else if (locator.startsWith("xpath=")) {
			locator = locator.substring(5);
			by = By.xpath(locator);
		} else {
			APP_LOGS.debug("[>>>> ERROR <<<] Executing: |This xpath format does not support| |");
		}

		return by;
	}

	public WebElement getWebElement(String locator) {
		return _driver.findElement(getLocator(locator));
	}

	// Action
	public void open(String url) {
		_driver = getDriver(CONFIG.getProperty("selenium.browser"));
		_driver.get(url);
		_driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		_driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
	}
	
	public void sendKey(String locator, String value) {
		APP_LOGS.debug("[info] Executing: | sendKey | " + locator + " | " + value + " |");
		try {
			WebElement elements = getWebElement(locator);
			elements.sendKeys(value);
		} catch (WebDriverException e) {e.getMessage();}
	}

	public void click(String locator) {
		APP_LOGS.debug("[info] Executing: | click | " + locator + " |");
		try {
			WebElement elements = getWebElement(locator);
			elements.click();
		} catch (WebDriverException e) {e.getMessage();}
	}
	
	public void waitForText(String locator, String text) {
		APP_LOGS.debug("[info] Executing: |waitForElementPresent | " + locator + " |");
		try {
			WebDriverWait wait = new WebDriverWait(_driver, Long.valueOf(CONFIG.getProperty("selenium.timeout")));
			wait.until(ExpectedConditions.textToBePresentInElementLocated(getLocator(locator), text));
		} catch (WebDriverException e) {
			APP_LOGS.debug("[>>>> ERROR <<<] Timed out after " + CONFIG.getProperty("selenium.timeout") + " \n" + e.getMessage());
		}
	}
	
	public void close() {
		APP_LOGS.debug("[info] Executing: |close browser|\n");
		_driver.quit();
		
	}

	public String getText(String locator) {
		APP_LOGS.debug("[info] Executing: |getText | " + locator + " |");
		String value = "";
		try {
			value = _driver.findElement(getLocator(locator)).getText();
		} catch (WebDriverException e) {e.getMessage();}
		return value;
	}
	
	public void clear(String locator) {
		_driver.findElement(getLocator(locator)).clear();
	}

	public void type(String locator, String value) {
		clear(locator);
		sendKey(locator, value);
	}

	private WebDriver getDriver(String browserType) {

		WebDriver driver = null;
	
		try {
			switch (browserType) {

			case "Mozilla":
				
				 driver = new FirefoxDriver();

				break;

			case "Explorer":

				System.setProperty("webdriver.ie.driver", CONFIG.getProperty("webdriver.ie"));

				DesiredCapabilities capab = DesiredCapabilities.internetExplorer();
				capab.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);

				driver = new InternetExplorerDriver(capab);
				break;
			}

		} catch (WebDriverException e) {
		}

		return driver;
	}
	
	//============================
	private static void loading_config_sys() throws IOException {
		FileInputStream settingFile = new FileInputStream(System.getProperty("user.dir") + "\\setting.properties");
		CONFIG = new Properties();
		CONFIG.load(settingFile);
	}

	private static void loading_object_repository() throws IOException {
		
		FileInputStream fs = null;
		OBJECT = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		URL url = classLoader.getResource("com//gmail//object");
		
		try {
			File folder = new File(url.toURI());
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					fs = new FileInputStream(file);
					OBJECT.load(fs);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}