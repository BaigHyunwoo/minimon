package com.minimon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
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
		SeleniumHandler seleniumHandler = new SeleniumHandler();
		driver = seleniumHandler.setUp();
	    js = (JavascriptExecutor) driver;
	  }
	  @After
	  public void tearDown() {
	    driver.quit();
	  }
	  @Test
	  public void test1() {
		    driver.get("https://www.naver.com/");
		    driver.manage().window().setSize(new Dimension(1936, 1056));
		    driver.findElement(By.cssSelector(".mn_dic > .an_icon")).click();
		    driver.findElement(By.id("ac_input")).click();
		    driver.findElement(By.id("ac_input")).sendKeys("야나두");
		    driver.findElement(By.id("ac_input")).sendKeys(Keys.ENTER);
	  }
}
