package com.minimon.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimon.common.CommonUtils;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.TblMonCodeData;
import com.minimon.entity.TblMonTransaction;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonTransactionRepository;

@Service
public class TransactionService {

	@Autowired
	TblMonTransactionRepository tblMonTransactionRepository;

	private String className = this.getClass().toString();
	

	private Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
	/**
	 * 
	 * 	transaction 모니터링 검사 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	public Map<String, Object> checkTransactions(List<TblMonTransaction> tblMonTransactions) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {
			
			for(TblMonTransaction transaction : tblMonTransactions) 	{
				Map<String, Object> logData = executeTransaction(transaction.getCodeDatas());
				checkData.put(""+transaction.getSeq(), errorCheckTransaction(transaction, logData));
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
	 * 	transaction 모니터링 검사 상태 검사
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 12
	 */
	public int checkStatus(Map<String, Object> logData) throws Exception {
		
		int status = 200;
		
		try {
			
			for(String key : logData.keySet()) {
				
				if(logData.get(key).equals("ERR") == true) status = 500;
				
			}
			
		}catch(Exception e) {
			
			status = 500;
			
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
         
		}
		
		return status;
	}
	
	

	/**
	 * 
	 * 	transaction 에러 검사
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 13
	 */
	public Map<String, Object> errorCheckTransaction(TblMonTransaction transaction, Map<String, Object> logData) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {

			String result = "SUCCESS";
			int status = Integer.parseInt(""+logData.get("status"));
			double loadTime = Double.parseDouble(""+logData.get("loadTime"));
			
			
			/*
			 * CHECK
			 */
			if(transaction.getStatus() == status) checkData.put("status", "SUCCESS");
			else {
				checkData.put("status", "ERR");
				result = "ERR";
			}
			
			if(loadTime <= CommonUtils.getPerData(transaction.getLoadTime(), transaction.getLoadTimePer(), 1)) checkData.put("loadTime", "SUCCESS");
			else {
				checkData.put("loadTime", "ERR");
				result = "ERR";
			}

			
			/*
			 * SET PARAM
			 */
			checkData.put("logData",logData);
			checkData.put("check_loadTime",loadTime);
			checkData.put("check_status",status);
			checkData.put("origin_loadTime",transaction.getLoadTime());
			checkData.put("origin_status",transaction.getStatus());
			checkData.put("seq", transaction.getSeq());
			checkData.put("result", result);
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 13);
         
		}
		
		return checkData;
	}
	

	


	/**
	 * 
	 * 	transaction Code 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 14
	 */
	public Map<String, Object> executeTransaction(List<TblMonCodeData> codeDatas) throws Exception {
		Map<String, Object> logData = new HashMap<String, Object>();
		EventFiringWebDriver driver = null;
		
		try {
			
			SeleniumHandler selenium = new SeleniumHandler();
			driver = selenium.setUp();

            long startTime = System.currentTimeMillis();
			for(int i=0; i< codeDatas.size(); i++) {
				
				TblMonCodeData tblMonCodeData = codeDatas.get(i);
				logData.put(""+i, selenium.executeAction(selenium, driver, tblMonCodeData.getAction(), tblMonCodeData.getSelector_type(), tblMonCodeData.getSelector_value(), tblMonCodeData.getValue()));
				
			}
            long endTime = System.currentTimeMillis();
            
            
            logData.put("loadTime", endTime-startTime);
            logData.put("status", checkStatus(logData));
			

			logger.debug(logData.toString());
			
		} catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 14);
         
		} finally {
			
			if(driver != null) driver.quit();
			
		}
		
		return logData;
	}
	

	


	/**
	 * 
	 * 	transaction Code 분석
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 15
	 */
	public TblMonCodeData getCodeData(String line) throws Exception {
		TblMonCodeData tblMonCodeData = null;
		
		try {
			
			String action = getCodeAction(line);
			if(action != null) {
				
				String selector_type = getCodeSelectorType(line);
				String selector_value = getCodeSelectorValue(line, action);
				String value = getCodeValue(line, action, selector_type);
				
				tblMonCodeData = new TblMonCodeData();
				tblMonCodeData.setAction(action);
				tblMonCodeData.setSelector_type(selector_type);
				tblMonCodeData.setSelector_value(selector_value);
				tblMonCodeData.setValue(value);
			}
			
		} catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 15);
         
		}
		
		return tblMonCodeData;
		
	}
	
	

	public String getCodeAction(String line) {

		if(line.indexOf("driver.get") > 0) {
			
			return "get";
			
		}else if(line.indexOf("setSize") > 0) {
			
			return "size";
			
		}else if(line.indexOf("getWindowHandles") > 0) {

			return "window_handles";
			
		}else if(line.indexOf("waitForWindow") > 0) {
			
			return "wait";
			
		}else if(line.indexOf("switchTo") > 0) {
			
			return "switch";
			
		}else if(line.indexOf("click") > 0) {

			return "click";
			
		}else if(line.indexOf("submit") > 0) {

			return "submit";
			
		}else if(line.indexOf("sendKeys") > 0) {

			return "sendKeys";
			
		}

		return null;
	}
	
	public String getCodeSelectorType(String line) {
		
		if(line.indexOf("By.id") > 0) {
			
			return "By.id";
			
		}else if(line.indexOf("By.cssSelector") > 0) {
			
			return "By.cssSelector";
			
		}else if(line.indexOf("By.linkText") > 0) {

			return "By.linkText";
			
		}else if(line.indexOf("By.className") > 0) {
			
			return "By.className";
			
		}else if(line.indexOf("By.name") > 0) {
			
			return "By.name";
			
		}else if(line.indexOf("By.tagName") > 0) {

			return "By.tagName";
			
		}else if(line.indexOf("By.xpath") > 0) {

			return "By.xpath";
			
		}else if(line.indexOf("By.partialLinkText") > 0) {

			return "By.partialLinkText";
			
		}
		
		return null;
	}
	
	public String getValueByObject(String type, String line, String stObj, String edObj) {

		int stObjLen = stObj.length();
		
		if(type.equals("first") == true) {

			return line.substring(line.indexOf(stObj)+stObjLen, line.indexOf(edObj, line.indexOf(stObj)));
			
		}else {

			return line.substring(line.lastIndexOf(stObj)+stObjLen, line.indexOf(edObj, line.lastIndexOf(stObj)));
			
		}
	}

	public String getCodeSelectorValue(String line, String action) {
		
		
		if(action.equals("click") == true || action.equals("submit") == true || action.equals("sendKeys") == true) {
			
			return getValueByObject("first", line, "(\"", "\")");
			
		}
		
		return null;
	}
	
	public String getCodeValue(String line, String action, String selector) {

		if(action.equals("get") == true) {
			
			return getValueByObject("last", line, "(\"", "\")");
			
		}else if(action.equals("wait") == true) {
			
			return getValueByObject("first", line, "(\"", "\",");
			
		}else if(action.equals("switch") == true) {

			return getValueByObject("first", line, "(\"", "\")");
			
		}else if(action.equals("sendKeys") == true) {

			if(line.indexOf("Keys.ENTER") > 0) {
				
				return "Keys.ENTER";
				
			}else if(line.indexOf("Keys.BACK_SPACE") > 0) {
				
				return "Keys.BACK_SPACE";
				
			}else if(line.indexOf("Keys.DELETE") > 0) {
				
				return "Keys.DELETE";
				
			}else if(line.indexOf("Keys.SPACE") > 0) {
				
				return "Keys.SPACE";
				
			}else if(line.indexOf("Keys.ESCAPE") > 0) {
				
				return "Keys.ESCAPE";
				
			}else {

				return getValueByObject("first", line, "sendKeys(\"", "\");");
				
			}
			
		}
		
		return null;
	}
	
}