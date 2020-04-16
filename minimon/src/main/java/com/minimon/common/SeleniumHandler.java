package com.minimon.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.minimon.exceptionHandler.MyException;


/**
 * 
 * 셀레니움 관리 
 * 
 * 
 * 
 * @author 백현우
 *
 */
public class SeleniumHandler {
	
	private String className = this.getClass().toString();

	private static final Logger logger = LoggerFactory.getLogger(SeleniumHandler.class.getName());

	private double totalLoadTime = -1;

	private double totalPayLoad = 0.0;
	
	private int status = 200;
	
	private EventFiringWebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;
	
    public int getStatus() {
		return status;
	}

	public double getTotalPayLoad() {
		return totalPayLoad;
	}

	public double getTotalLoadTime() {
		return totalLoadTime;
	}


	@SuppressWarnings("unchecked")
	public String waitForWindow(int timeout) {
	  try {
	    Thread.sleep(timeout);
	  } catch (InterruptedException e) {
	    e.printStackTrace();
	  }
	  Set<String> whNow = driver.getWindowHandles();
	  Set<String> whThen = (Set<String>) vars.get("window_handles");
	  if (whNow.size() > whThen.size()) {
	    whNow.removeAll(whThen);
	  }
	  return whNow.iterator().next();
	}
	
	/**
     * 
     *  Selenium loadTime Checker
     *  AbstractWebDriverEventListener Class 를 return 받는다.
     *  
     *  
     *  @author 백현우
     *  
     */
    public class WebDriverEventListenerClass extends AbstractWebDriverEventListener {
    	
        long startTime, endTime;
        
        public void beforeNavigateTo(String arg0, WebDriver arg1) {
            startTime = System.currentTimeMillis();
        }

        public void afterNavigateTo(String arg0, WebDriver arg1) {
            endTime = System.currentTimeMillis();
        }
        
        public int returnLoadTime() {
            int loadTime = (int) (endTime-startTime);
            return loadTime;
        }
    }
    
    /**
     * 
	 *  Selenium Web driver 기본 세팅 
	 *  셀리니움을 사용하기 위하여 기본 셋팅을 하고
	 *  주어진 URL에 따라 드라이버를 구동한다
	 *  직접 Driver Path를 받아오는 경우를 처리
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  
	 *  @return driver 구동
	 * 	@throws 에러코드 11
	 * 
	 */
	public EventFiringWebDriver setUp() throws Exception { 
		
		EventFiringWebDriver driver = null;
		String driverName = "webdriver.chrome.driver";
		
		try {
			
			// 기존 프로세스 킬
		    Runtime.getRuntime().exec("taskkill /F /IM chromedriver");
		        
			// 크롬 드라이버 파일 경로설정 
			ClassPathResource cpr = new ClassPathResource("/setting/chromedriver.exe");
			System.setProperty(driverName, cpr.getFile().getPath()); 
			
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("w3c", false);
//			options.addArguments("headless");
			options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			driver = new EventFiringWebDriver(new ChromeDriver(options));
		    js = (JavascriptExecutor) driver;
		    vars = new HashMap<String, Object>();
		    
			logger.debug("WebDriver - 연결 완료");
			

		}catch(Exception e) {
			e.printStackTrace();
			
			if(driver != null) driver.quit();
			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 11);
		}
		
		return driver;
	}
    

	
	/**
	 * 
	 *  Selenium Web driver를 이용하여 URL 접근  
	 *  주어진 URL에 따라 드라이버를 구동
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  @param url 접근 할 URL
	 *  
	 */
	public double connectUrl(String url, EventFiringWebDriver driver, int timeout) throws Exception {

		// 타임아웃 셋팅
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS); 
		
		
		try {
			
            WebDriverEventListenerClass event = new WebDriverEventListenerClass();
            
            driver.register(event);
			driver.navigate().to(url);
			totalLoadTime = event.returnLoadTime(); 
			
			logger.debug("totalLoadTime : "+ totalLoadTime);
			
		}catch(TimeoutException e1) {
			e1.printStackTrace();
			
			totalLoadTime = -1;
			logger.info(url + " : Error - TimeOut");
			
		}catch(Exception e) {
			e.printStackTrace();
			
			totalLoadTime = -2;
			logger.debug("Error - Unknown ERROR");
			
		}
		
		return totalLoadTime;
	}

	
	/**
	 *  
	 *  Selenium Web driver를 이용하여 페이지 접근 후 로거 호출 
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  @param url 이동할 URL 주소 
	 *  @param driver 실제 구동되는 드라이버
	 *  
	 *  
	 *  @return 주어진 URL의 로거 반환
	 * 	@throws 에러코드 12
	 * 
	 */
	public LogEntries getLog(EventFiringWebDriver driver) throws Exception {
		LogEntries logs = null;
		
		try {

			logs =  driver.manage().logs().get(LogType.PERFORMANCE);

			logger.debug("WebDriver - Log 호출 완료");
			
		}catch(TimeoutException ex) {
			
			return logs;
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 12);
		}
		
		return logs;
	}
    
	

	/**
	 * 
	 *  현재 페이지의 소스 호출 
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  @param 	현재 주소
	 *  
	 *  
	 * 	@throws 에러코드 13
	 * 
	 */
	public String getSource(WebDriver driver) throws Exception {
		try {

			return driver.getPageSource();
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 13);
			
		}
	}
	

	

	/**
	 * 
	 *  리소스의 Message 추출 
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 * 	@throws 에러코드 14
	 * 
	 */
	public JSONObject getResourceMessage(LogEntry entry) throws Exception {
		
		try {

			JSONObject json = new JSONObject(entry.getMessage());
			return json.getJSONObject("message");
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 14);
			
		}
	}
	

	/**
	 * 
	 * 
	 *  log 분석 
	 *  
	 *  
	 *  @author 	백현우
	 *  
	 *  @param 		log 	분석할 log
	 *  
	 * 	@throws 	에러코드 15
	 * 
	 */
	public Map<String, Object> expectionLog(LogEntries logs, String currentURL) throws MyException {
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		try {

			
			/* 
			 * RESOURCE LOOP & GET RESOURCES DATA
			 */
			int resourceCnt = 0;
			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext(); resourceCnt++) {
				setLogData(getResourceMessage(it.next()), resourceCnt, currentURL);
			}

			returnData.put("url", currentURL);
			returnData.put("status", this.status);
			returnData.put("totalPayLoad", this.totalPayLoad);
			returnData.put("totalLoadTime", this.totalLoadTime);
			
			logger.debug("WebDriver - Log 분석 완료");
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
					+ " - TYPE = [Function]/  Function - " 
					+  e.getStackTrace()[0].getMethodName(), className, 15);
			
		}			
		return returnData;
	}


	

	/**
	 * 
	 *  리소스의 DATA 분석 및 반환
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 * 	@throws 에러코드 16
	 * 
	 */
	public void setLogData(JSONObject message, int resourceCnt, String currentURL) throws Exception {
		
		try {

			/*
			 * DATA CONVERT & CHECK
			 */
	        String methodName = message.getString("method");
	        
	        /*
	         * Log Exists Check 
	         */
			if (methodName != null && methodName.equals("Network.responseReceived") == false) return; 
				
			/*
			 * GET DATAS
			 */
			JSONObject params = message.getJSONObject("params");

			JSONObject response = params.getJSONObject("response");
			
			JSONObject headersObj = response.getJSONObject("headers");
			
	        Map<String,Object> headers = new CaseInsensitiveMap<String,Object>();
	        
	        headers.putAll(headersObj.toMap());

	        /*
	         * SET PAYLOAD
	         */
			this.totalPayLoad += headers.containsKey("content-length") ? Double.parseDouble(headers.get("content-length").toString()) : 0;
			
			/*
			 * SET STATUS
			 */
			this.status = currentURL.equals((String)response.get("url")) == true ? response.getInt("status") : this.status;
	        
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 16);
			
		}
	}
	


	/**
	 * 
	 *  Selenium 액션 실행
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 * 	@throws 에러코드 17
	 * 
	 */
	public String executeAction(SeleniumHandler selenium, EventFiringWebDriver driver, String action, String selector_type, String selector_value, String value) throws Exception{
		
		String result = "";
		
		WebElement element = null;
		
		try {
			
			if(action.equals("get") == true) {

				driver.get(value);
				
			}else if(action.equals("size") == true){
				
			    driver.manage().window().maximize();
			    
			}else if(action.equals("window_handles") == true) {
				
			    vars.put("window_handles", driver.getWindowHandles());
			    
			}else if(action.equals("wait") == true) {
				
			    vars.put(selector_value, waitForWindow(5000));
			    
			}else if(action.equals("switch") == true) {
				
			    driver.switchTo().window(vars.get(selector_value).toString());
			    
			}else if(action.equals("click") == true) {
				
				element = getSelector(driver, selector_type, selector_value);
				element.click();
			    
			}else if(action.equals("sendKeys") == true) {

				element = getSelector(driver, selector_type, selector_value);
				element.sendKeys(value);
				
			}else if(action.equals("submit") == true) {

				element = getSelector(driver, selector_type, selector_value);
				element.submit();
			    
			}
			
			result = "SUCCESS";
			
			
		} catch(Exception e) {
			e.printStackTrace();

			result = e.getMessage();
			
			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 17);
		}
		
		return result;
	}
	
	public WebElement getSelector(EventFiringWebDriver driver, String selector_type, String selector_value) {

		WebElement webElement = null;

		System.out.println(selector_type+" : "+selector_value);
		
		if(selector_type.equals("By.id") == true) {
			
			webElement = driver.findElement(By.id(selector_value));
			
		}else if(selector_type.equals("By.cssSelector") == true) {

			webElement = driver.findElement(By.cssSelector(selector_value));
			
		}else if(selector_type.equals("By.linkText") == true) {
			
			webElement = driver.findElement(By.linkText(selector_value));
			
		}else if(selector_type.equals("By.className") == true) {
			
			webElement = driver.findElement(By.className(selector_value));
			
		}else if(selector_type.equals("By.name") == true) {

			webElement = driver.findElement(By.name(selector_value));
			
		}else if(selector_type.equals("By.tagName") == true) {

			webElement = driver.findElement(By.tagName(selector_value));
			
		}else if(selector_type.equals("By.xpath") == true) {

			webElement = driver.findElement(By.xpath(selector_value));
			
		}else if(selector_type.equals("By.partialLinkText") == true) {

			webElement = driver.findElement(By.partialLinkText(selector_value));
			
		}
		
		return webElement;
	}
	
}
