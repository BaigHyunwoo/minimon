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
	//@Scheduled(fixedDelay=5000)
	public void execute() throws Exception {
		try {
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			
			/*
			 * API
			 */
			
			/*
			 * URL
			 */
			List<TblMonUrl> urls = tblMonUrlRepository.findAll();
			resultList.add(urlService.checkUrls(urls));
			
			/*
			 * Trasacntion
			 */
			
			/*
			 * status check and sending e-mail
			 */
			
			logger.debug("Monitoring Execute Complete");
	
		}catch(Exception e) {
			e.printStackTrace();
	
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
	     
		}
		
	}

}
