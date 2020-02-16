package com.minimon.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.minimon.controller.MainController;
import com.minimon.exceptionHandler.MyException;


/**
 * 
 * 모니터링 동작 관리
 * 
 * 
 * 
 * @author 백현우
 *
 */
public class ExecuteTool {

	private String className = this.getClass().toString();
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	
	/**
	 * 
	 * 	모니터링 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	@Scheduled(fixedDelay=100)
	public void execute() throws Exception {

		try{
        
			
			logger.debug("Monitoring Execute Complete");
           
         
		}catch(Exception e) {


			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - " + e.getStackTrace()[0].getMethodName() , className, 11);
         
		}
      
	}
}
