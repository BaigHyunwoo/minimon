package com.minimon.common;

import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@EnableScheduling
public class ExecuteTool {

	private String className = this.getClass().toString();
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	
	private String DRIVERPATH = "/setting/chromedriver.exe";
	
	/**
	 * 
	 * 	모니터링 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	@Scheduled(fixedDelay=5000)
	public void execute() throws Exception {
		
		EventFiringWebDriver driver = null;
		
		try{
        
			// Call List
			String[] urls = {"https://www.naver.com"};
			
			SeleniumHandler selenium = new SeleniumHandler();
			ClassPathResource cpr = new ClassPathResource(DRIVERPATH);
			driver = selenium.setUp(cpr.getFile().getPath());
			
			for(String url : urls) 	{
				selenium.connectUrl(url, driver, 5);
				logger.debug(selenium.expectionLog(
						selenium.getLog(url, driver), 
						url, 
						driver.getCurrentUrl(),
						selenium.getTotalLoadTime()
				).toString());
			}
			
			logger.debug("Monitoring Execute Complete");
			
           
         
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
         
		}finally {
			
			if(driver != null) driver.quit();
			
		}
      
	}
}
