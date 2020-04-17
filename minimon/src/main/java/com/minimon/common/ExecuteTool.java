package com.minimon.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.minimon.MinimonApplication;
import com.minimon.controller.MainController;
import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonResult;
import com.minimon.entity.TblMonTransaction;
import com.minimon.entity.TblMonUrl;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonApiRepository;
import com.minimon.repository.TblMonTransactionRepository;
import com.minimon.repository.TblMonUrlRepository;
import com.minimon.service.ApiService;
import com.minimon.service.EmailService;
import com.minimon.service.ResultService;
import com.minimon.service.TransactionService;
import com.minimon.service.UrlService;


/**
 * 
 * 모니터링 동작 관리
 * 
 * 
 * 
 * @author 백현우
 *
 */
@RestController
@EnableScheduling
public class ExecuteTool {

	@Autowired
	TblMonUrlRepository tblMonUrlRepository;

	@Autowired
	TblMonApiRepository tblMonApiRepository;

	@Autowired
	TblMonTransactionRepository tblMonTransactionRepository;
	
	@Autowired
	ResultService resultService;
	
	@Autowired
	UrlService urlService;
	
	@Autowired
	ApiService apiService;

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	EmailService emailService;
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);

	private String className = this.getClass().toString();
	
	
	/**
	 * 
	 * 	모니터링 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	@Scheduled(cron = "0 0/5 * * * *")
	public void execute() throws Exception {
		
		try {
			
			if (MinimonApplication.getDriverPath().length() > 1) {

				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
				
				List<TblMonApi> apis = tblMonApiRepository.findByUseable(1);
				resultList.add(apiService.checkApis(apis));
				
				List<TblMonUrl> urls = tblMonUrlRepository.findByUseable(1);
				resultList.add(urlService.checkUrls(urls));
				
	    		List<TblMonTransaction> transactions = tblMonTransactionRepository.findAll();
				resultList.add(transactionService.checkTransactions(transactions));
				
				/*
				 * status check and sending e-mail
				 */
				check(resultList);
				
				logger.info("Monitoring Execute Complete");
				
			}else {

				logger.info("Please save your webDriverPath at the main page");
				
			}
			
	
		}catch(Exception e) {
			e.printStackTrace();
	
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
	     
		}
		
	}
	

	
	/**
	 * 
	 * 	모니터링 실행 결과 전송
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 12
	 */
	@SuppressWarnings("unchecked")
	public void check(List<Map<String, Object>> resultList) throws Exception {
		
		try {
			
			for(Map<String, Object> result : resultList) {
				for(Object value : result.values()) {
					Map<String, Object> checkLog = (Map<String, Object>) value;
					if(checkLog.get("result").equals("ERR") == true) {
						TblMonResult tblMonResult = resultService.saveResult(checkLog);
						emailService.sendSimpleMessage("qorto12@naver.com", "모니터링 검사 결과", tblMonResult);
					}
				}
			}
			
			
			logger.debug("Monitoring check Complete");
	
		}catch(Exception e) {
			e.printStackTrace();
	
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - check", className, 12);
	     
		}
		
	}

}
