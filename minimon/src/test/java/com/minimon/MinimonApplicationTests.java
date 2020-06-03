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
import org.openqa.selenium.support.events.EventFiringWebDriver;
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
		    driver.get("https://www.yanadoo.co.kr/english/basic/");
		  	driver.quit();
	  }
	  

		public void waitHtml(WebDriver driver) throws InterruptedException {

			while(0 < 1) {
				Thread.sleep(100);
				JavascriptExecutor js = (JavascriptExecutor)driver;
				if(js.executeScript("return document.readyState").toString().equals("complete") == true) {
					break;
				}
			}
		}
}
