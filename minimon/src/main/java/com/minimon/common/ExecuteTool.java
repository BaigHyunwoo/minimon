package com.minimon.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import com.minimon.controller.MainController;
import com.minimon.entity.TblMonUrl;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonUrlRepository;
import com.minimon.service.EmailService;
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
	UrlService urlService;

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
	//@Scheduled(cron = "0 * * * * *")
	public void execute() throws Exception {
		try {
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			/*
			 * API
			 */
			
			/*
			 * URL
			 */
			List<TblMonUrl> urls = tblMonUrlRepository.findByUseable(1);
			resultList.add(urlService.checkUrls(urls));
			
			/*
			 * Trasacntion
			 */
			
			/*
			 * status check and sending e-mail
			 */
			check(resultList);
			
			logger.debug("Monitoring Execute Complete");
	
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
						emailService.sendSimpleMessage("qorto12@naver.com", "모니터링 검사 결과", checkLog.toString());
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
