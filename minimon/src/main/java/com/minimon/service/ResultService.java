package com.minimon.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimon.entity.TblMonResult;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonResultRepository;

@Service
public class ResultService {

	@Autowired
	EmailService emailService;

	@Autowired
	SmsService smsService;

	@Autowired
	TblMonResultRepository tblMonResultRepository;

	private String className = this.getClass().toString();

	
	/**
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	public TblMonResult saveResult(Map<String, Object> result) throws Exception {

		TblMonResult tblMonResult = new TblMonResult();
		
		try {

			tblMonResult.setMon_seq(Integer.parseInt(""+result.get("seq")));
			tblMonResult.setTitle(""+result.get("title"));
			tblMonResult.setResponse(result.toString());
			tblMonResult.setResult(""+result.get("result"));
			tblMonResult.setType(""+result.get("type"));
			tblMonResult.setRegDate(new Date());
			tblMonResult.setLoadTime(Double.parseDouble(""+result.get("check_loadTime")));
			tblMonResultRepository.save(tblMonResult);
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - saveResult", className, 11);
         
		}
		
		return tblMonResult;
		
	}


	public void sendResult(TblMonResult tblMonResult) throws Exception {
		smsService.sendSimpleMessage("010-3220-8934", "모니터링 검사 결과", tblMonResult);// CTO님
		smsService.sendSimpleMessage("010-9380-4365", "모니터링 검사 결과", tblMonResult);// 국 책임님
		smsService.sendSimpleMessage("010-9871-8476", "모니터링 검사 결과", tblMonResult);// 이상배 주임님
		smsService.sendSimpleMessage("010-5391-7226", "모니터링 검사 결과", tblMonResult);// 이 책임님
//		emailService.sendSimpleMessage("qorto12@naver.com", "모니터링 검사 결과", tblMonResult);
	}

	
}
