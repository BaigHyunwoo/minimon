package com.minimon.service;

import com.minimon.MinimonApplication;
import com.minimon.entity.TblMonResult;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class ResultService {

	@Autowired
	EmailService emailService;

	@Autowired
	SmsService smsService;

	@Autowired
	TblMonResultRepository tblMonResultRepository;

	private String className = this.getClass().toString();

	public TblMonResult saveResult(Map<String, Object> result) throws Exception {

		TblMonResult tblMonResult = new TblMonResult();
		
		try {

			tblMonResult.setMon_seq(Integer.parseInt(""+result.get("seq")));
			tblMonResult.setTitle(""+result.get("title"));
			tblMonResult.setResult(""+result.get("result"));
			tblMonResult.setType(""+result.get("type"));
			tblMonResult.setRegDate(new Date());
			tblMonResult.setLoadTime(Double.parseDouble(""+result.get("check_loadTime")));
			tblMonResult.setResponse(result.toString());
			tblMonResultRepository.save(tblMonResult);
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - saveResult", className, 11);
         
		}
		
		return tblMonResult;
		
	}

	public void sendResultByProperties(TblMonResult tblMonResult) throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(MinimonApplication.getDriverPath()+"/users.properties");
		properties.load(new java.io.BufferedInputStream(fis));
		String users = properties.getProperty("users");
		for (String user : users.split(",")) {
			smsService.sendSimpleMessage(user, tblMonResult);
		}

	}
	
}
