package com.minimon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.minimon.common.SeleniumHandler;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MinimonApplicationTests {
	 private WebDriver driver;
	  JavascriptExecutor js;
	  @Before
	  public void setUp() throws Exception {
		  SeleniumHandler sel = new SeleniumHandler();
	    driver = sel.setUp();
	    js = (JavascriptExecutor) driver;
	  }
	  @After
	  public void tearDown() {
	    driver.quit();
	  }
	  @Test
	  public void paymentsTest() throws InterruptedException {
		    WebDriverWait wait = new WebDriverWait(driver,30);
		    driver.get("https://www.yanadoo.co.kr/english/basic/");
		    driver.manage().window().setSize(new Dimension(1936, 1056));
		    driver.findElement(By.linkText("로그인")).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_sub_id"))).click();
		    driver.findElement(By.id("login_sub_id")).sendKeys("qorto12");
		    driver.findElement(By.id("login_main_pw")).sendKeys("Alsdk972!@");
		    driver.findElement(By.cssSelector(".section > input")).click();
	  }
}
