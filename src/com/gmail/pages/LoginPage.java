package com.gmail.pages;

import org.openqa.selenium.WebDriver;

import com.auto.webdriver.ActionDriver;

public class LoginPage extends ActionDriver {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public void visit() {
		open(CONFIG.getProperty("selenium.host"));
	}

	public void enter_email(String email) {
		type(OBJECT.getProperty("user.email"), email);
	}

	public void enter_password(String pwd) {
		type(OBJECT.getProperty("user.pwd"), pwd);
	}

	public void click_on_login_button() {
		click(OBJECT.getProperty("user.submitBtn"));
	}

	public String get_errEmail_message() {
		return getText(OBJECT.getProperty("msg.errEmail"));
	}
	
	public String get_errPwd_message() {
		return getText(OBJECT.getProperty("msg.errPwd"));
	}
	
	public void close_browser() {
		close();
	}
	
}
