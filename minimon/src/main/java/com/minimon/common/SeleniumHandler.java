package com.minimon.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	
	
    /**
     * 
     *  Selenium loadTime Checker
     *  AbstractWebDriverEventListener Class 를 return 받는다.
     *  
     *  
     *  @author 백현우
     *  
     *  @throws MyException 모든 Exception을 ExceptionHandler에서 처리 에러코드 11
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
	 *  @param driverPath 드라이버의 위치
	 *  
	 *  
	 *  @return driver 구동
	 * 	@throws 에러코드 11
	 * 
	 */
	public EventFiringWebDriver setUp(String driverPath) throws Exception { 
		WebDriver driver2 = null;
		
		EventFiringWebDriver driver = null;
		
		String driverName = "webdriver.chrome.driver";
		
		try {
			
			// 크롬 드라이버 파일 경로설정 
			System.setProperty(driverName, driverPath); 
			
			// 옵션 설정
			ChromeOptions options = new ChromeOptions();
			LoggingPreferences logPrefs = new LoggingPreferences();
			
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			options.setHeadless(true);
			options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-browser-side-navigation");
			options.setExperimentalOption("useAutomationExtension", false);
			options.setCapability(ChromeOptions.CAPABILITY, options);
			
			// 설정한 옵션으로 드라이버 호출
			driver2 = new ChromeDriver(options);
			driver = new EventFiringWebDriver(driver2);
			
			logger.debug("WebDriver - 연결 완료");
			

		}catch(Exception e) {
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
		
		double totalLoadTime = -1;
		
		try {
			
            WebDriverEventListenerClass event = new WebDriverEventListenerClass();
            
            driver.register(event);
			
			driver.navigate().to(url);
			
			totalLoadTime = event.returnLoadTime(); 
			
		}catch(TimeoutException e1) {
			
			totalLoadTime = -1;
			
			logger.info(url + " : Error - TimeOut");
			
		}catch(Exception e) {
			
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
	public LogEntries getLog(String url, EventFiringWebDriver driver) throws Exception {
		LogEntries logs = null;
		
		try {

			logs =  driver.manage().logs().get("performance");
			

			logger.debug("WebDriver - Log 호출 완료");
			
			
		}catch(TimeoutException ex) {
			
			logger.debug(url + " : LOG CALL Timeout : "+url);
			
			return logs;
			
		}catch(Exception e) {

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
	 *  
	 *  @param 	현재 주소
	 *  
	 *  
	 *  @return 소스 호출 결과 반환
	 * 	@throws MyException 모든 Exception을 ExceptionHandler에서 처리 에러코드 13
	 * 
	 */
	public String setSource(WebDriver driver) throws Exception {
		try {

			return driver.getPageSource();
			
		}catch(Exception e) {

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 13);
			
		}
	}
	

	/**
	 * 
	 *  UTC 형태의 데이터를 GMT 밀리세컨즈로 변환
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  @param 	input 가공 데이터
	 *  
	 *  @return 가공 결과 반환
	 *  
	 */
	@SuppressWarnings("deprecation")
	public static long convertUTCtoGMT(Double input) {
	    String format = "yyyy/MM/dd HH:mm:ss.SSS";
	    
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    
	    // GMT 시간
	    String retime = sdf.format(input*1000);
	    
	    // SPLIT time, mills
	    String[] str = retime.split("\\.");
	    
	    // get Date of split time exclude mills
	    Date date = new Date(str[0]);
	    
	    // convert mills
	    long mills = date.getTime();
	    
	    // include mills
	    mills = mills+Long.parseLong(str[1]);
	    
	    return mills;
	}
	
}
