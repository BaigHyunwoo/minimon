package com.minimon.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.json.JSONObject;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
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

import com.minimon.exceptionHandler.MyException;
import com.minimon.exceptionHandler.MyExceptionHandler;


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
	 *  @param 	현재 주소
	 *  
	 *  
	 * 	@throws 에러코드 13
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
	@SuppressWarnings("unchecked")
	public Map<String, Object> analyzeLog(LogEntries logs, String url, String currentURL, double totalLoadTime) throws MyException {
		String method = new Object(){}.getClass().getEnclosingMethod().getName();

		double totalPayLoad = 0.0;
		
		double ResourceloadTime = 0.0;
		
		// insert될 Map의 첫번째
		Map<String, Object> originUrlData = new HashMap<String, Object>();

		// 리소스의 insert될 Data의 하위 : URL 별 Map
	    Map<String, Object> resourcesData = new HashMap<String, Object>();
		
		try {

			
			/* 
			 * RESOURCE LOOP & GET RESOURCES DATA
			 */
			int resourceCnt = 0;
			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext(); resourceCnt++)
				resourcesData.put(""+resourceCnt, getResourceDatas(getResourceMessage(it.next()), resourceCnt));
				
	            // totalPayLoad += payLoad;
			
			/*
			 * if(status >= 200 && status < 400) {
			 * 
			 * 
			 * 
			 * long[] lltime = new long[requestTimeArr.size()];
			 * 
			 * for(int i=0; i<requestTimeArr.size(); i++) { lltime[i] =
			 * requestTimeArr.get(i); }
			 * 
			 * long[] rrtime = new long[loadTimeArrTime.size()]; for(int i=0;
			 * i<loadTimeArrTime.size(); i++) { rrtime[i] = loadTimeArrTime.get(i); }
			 * 
			 * Arrays.sort(lltime); Arrays.sort(rrtime);
			 * 
			 * 
			 * long startTime = lltime[0]; long endTime = rrtime[rrtime.length-1];
			 * 
			 * originUrlData.put("startTime", startTime); originUrlData.put("endTime",
			 * endTime);
			 * 
			 * originUrlData.put("resourceLoadTime", endTime-startTime);
			 * 
			 * }
			 * 
			 * 
			 * resourcesData = getTimeoutResource(resourcesData, logs, url, cnt);
			 * 
			 * if(status >= 200 && status < 400) {
			 * 
			 * 
			 * totalPayLoad = Math.ceil(totalPayLoad); }else { status = 404;
			 * 
			 * totalLoadTime = -2;
			 * 
			 * totalPayLoad = -2; }
			 * 
			 * 
			 * originUrlData.put("url", url); originUrlData.put("header", header);
			 * originUrlData.put("status", status); originUrlData.put("totalLoadTime",
			 * totalLoadTime); originUrlData.put("totalPayLoad", totalPayLoad);
			 * originUrlData.put("source", source); originUrlData.put("resourcesData",
			 * resourcesData);
			 */
			logger.debug("WebDriver - Log 분석 완료");
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
					+ " - TYPE = [Function]/  Function - " 
					+  e.getStackTrace()[0].getMethodName(), className, 15);
			
		}			
		return originUrlData;
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
	public Map<String, Object> getResourceDatas(JSONObject message, int resourceCnt) throws Exception {
        Map<String, Object> detailMap = new HashMap<String, Object>();
		
		try {

			/*
			 * DATA CONVERT & CHECK
			 */
	        String methodName = message.getString("method");

			if (methodName == null && methodName.equals("Network.responseReceived") == false) return detailMap; 
				
			
			/*
			 * SET DATAS
			 */
			JSONObject params = message.getJSONObject("params");

			JSONObject response = params.getJSONObject("response");
			
			JSONObject headersObj = response.getJSONObject("headers");
			
	        Map<String,Object> headers = new CaseInsensitiveMap<String,Object>();
	        
	        headers.putAll(headersObj.toMap());
	        


	        /*
	         * GET PAYLOAD
	         */
			double payLoad = headers.containsKey("content-length") ? Double.parseDouble(headers.get("content-length").toString()) : 0;

	        /*
	         * GET TYPE
	         */
			String type = headers.containsKey("content-type") ? headers.get("content-length").toString() : "";
			

	        /*
	         * GET STATUS
	         */
	        int detailStatus = response.getInt("status");
			

	        /*
	         * RESOURCE URL
	         */
			String resourceUrl = response.getString("url");
		
			if(!response.isNull("timing")) {
				
				/*
				 * SET DATA PARAMS : timing
				 */
				JSONObject timing = response.getJSONObject("timing");
			
				Double requestTime = timing.getDouble("requestTime");
				
				Double timestamp = params.getDouble("timestamp");
				
		        detailMap.put("timestamp", CommonUtils.convertUTCtoGMT(timestamp));

		        
				Double loTimed = 0.0;
				
				for (Iterator<String> itt = timing.keys(); itt.hasNext();) {
					
					String key = itt.next();
					
					if(timing.getDouble(key) > 0) 
						loTimed += timing.getDouble(key);
					
			        detailMap.put(key, timing.getDouble(key));
				}
				
				Long lol = CommonUtils.convertUTCtoGMT(requestTime);

				loTimed = Math.ceil(loTimed-requestTime);
				
		        detailMap.put("loadTime", loTimed.longValue());

		        detailMap.put("requestTime", lol);

		        detailMap.put("endTime", lol+loTimed.longValue());
				/*
				 * if (currentURL.equals(messageUrl)) { status = response.getInt("status");
				 * header = response.getJSONObject("headers")+"";
				 * 
				 * }
				 */
		        
			}
			
			
			/*
			 * SAVE DATA
			 */
			detailMap.put("payLoad", payLoad);

			detailMap.put("type", type);
	        
	        detailMap.put("detailStatus", detailStatus);
	        
	        detailMap.put("url", resourceUrl);
			
			detailMap.put("cnt", resourceCnt);
			
			return detailMap;
			
		}catch(Exception e) {

			throw new MyException("CLASS : " + className + " - METHOD : " + new Object(){}.getClass().getEnclosingMethod().getName() 
									+ " - TYPE = [Function]/  Function - " 
									+  e.getStackTrace()[0].getMethodName(), className, 16);
			
		}
	}
	
	
	
}
