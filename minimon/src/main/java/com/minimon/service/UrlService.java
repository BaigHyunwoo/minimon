package com.minimon.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.minimon.common.SeleniumHandler;
import com.minimon.entity.TblMonUrl;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonUrlRepository;

@Service
public class UrlService {

	@Autowired
	TblMonUrlRepository tblMonUrlRepository;

	private String className = this.getClass().toString();
	
	private String DRIVERPATH = "/setting/chromedriver.exe";

	private Logger logger = LoggerFactory.getLogger(UrlService.class);
	
	
	
	/**
	 * 
	 * 	URL 모니터링 검사 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	public Map<String, Object> checkUrls(List<TblMonUrl> urls) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {
			
			for(TblMonUrl url : urls) 	{
				Map<String, Object> logData = executeUrl(url.getUrl());
				checkData.put(url.getUrl(), errorCheckUrl(url, logData));
			}
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
         
		}
		
		return checkData;
	}
	
	

	
	/**
	 * 
	 * 	URL 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 12
	 */
	public Map<String, Object> executeUrl(String url) throws Exception {
		Map<String, Object> logData = new HashMap<String, Object>();
		EventFiringWebDriver driver = null;
		
		try {
			
			SeleniumHandler selenium = new SeleniumHandler();
			ClassPathResource cpr = new ClassPathResource(DRIVERPATH);
			driver = selenium.setUp(cpr.getFile().getPath());
	
			selenium.connectUrl(url, driver, 5);
			logData = selenium.expectionLog(
					selenium.getLog(driver), 
					driver.getCurrentUrl()
			);
			
			logger.debug(logData.toString());
			
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 12);
         
		}finally {
			
			if(driver != null) driver.quit();
			
		}
		
		return logData;
	}
	


	/**
	 * 
	 * 	URL 에러 검사
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 13
	 */
	public Map<String, Object> errorCheckUrl(TblMonUrl url, Map<String, Object> logData) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {

			/*
			 * SET DATA
			 */
			int status = Integer.parseInt(""+logData.get("status"));
			double totalLoadTime = Double.parseDouble(""+logData.get("totalLoadTime"));
			double totalPayLoad = Double.parseDouble(""+logData.get("totalPayLoad"));
			
			
			/*
			 * CHECK
			 */
			if(url.getStatus() == status) checkData.put("status", "SUCCESS");
			else checkData.put("status", "ERR");
			
			if(totalLoadTime <= getPerData(url.getLoadTime(), url.getLoadTimePer(), 1)) checkData.put("loadTime", "SUCCESS");
			else checkData.put("loadTime", "ERR");

			if(getPerData(url.getPayLoad(), url.getPayLoadPer(), 2) <= totalPayLoad 
					&& totalPayLoad <=getPerData(url.getPayLoad(), url.getPayLoadPer(), 1)) checkData.put("status", "SUCCESS");
			else checkData.put("payLoad", "ERR");

		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
         
		}
		
		return checkData;
	}

	/**
	 * 
	 * 	PERCENT 적용 데이터 반환
	 * 
	 * 	@param 	type 1 : +
	 * 			type 2 : -
	 * 
	 */
	public double getPerData(double data, int per, int type) {
		if(type == 1) {
			return data+(data/100*per);
		}
		else {
			return data-(data/100*per);
		}
		
	}
	
}
