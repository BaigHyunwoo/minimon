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
		    WebDriverWait wait = new WebDriverWait(driver, 30);

		    driver.get("https://www.yanadoo.co.kr/english/basic/");
		    driver.manage().window().setSize(new Dimension(1936, 1056));
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".menuBtn img"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("평생수강"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("지금 신청하기"))).click();
		    driver.switchTo().frame("loginPop");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_sub_id"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_sub_id"))).sendKeys("qorto12");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_main_pw"))).sendKeys("Alsdk972!@");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_main_pw"))).sendKeys(Keys.ENTER);
		    driver.switchTo().defaultContent();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pop-coupon-blue .cookie > span"))).click();
		    waitHtml(driver);
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chk_bx2"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("r_cp1"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("r_cp1"))).sendKeys("010");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("r_cp2"))).sendKeys("2407");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("r_cp3"))).sendKeys("1563");
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tb-order:nth-child(2) > .chk_bx2 > em"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".active:nth-child(3)"))).click();
		    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("text-field-line-3"))).click();
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
