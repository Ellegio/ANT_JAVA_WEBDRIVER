package com.gmail.login;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.gmail.pages.LoginPage;

public class LoginTest {

	WebDriver driver;
	LoginPage loginPage = new LoginPage(driver);

	@BeforeMethod
	public void before() {
		loginPage.visit();
	}

	@Test(description = "Login with the email or password is incorrect.")
	public void LoginWithInvalidData() {

		loginPage.enter_email("aaa@gmail.com");
		loginPage.enter_password("abc");
		loginPage.click_on_login_button();

		Assert.assertEquals(loginPage.get_errPwd_message(),
				"The email or password you entered is incorrect. ?");
	}

	@Test(description = "Login with the email is blank")
	public void LoginWithEmptyEmail() {

		loginPage.enter_email("");
		loginPage.enter_password("abc");
		loginPage.click_on_login_button();

		Assert.assertEquals(loginPage.get_errEmail_message(),
				"Enter your email address.");
	}

	@Test(description = "Login with the password is blank")
	public void LoginWithEmptyPassword() {

		loginPage.enter_email("aaa@gmail.com");
		loginPage.enter_password("");
		loginPage.click_on_login_button();
		
		Assert.assertEquals(loginPage.get_errPwd_message(),
				"Enter your password.");
	}

	@AfterMethod
	public void after() {
		loginPage.close_browser();
	}

}
