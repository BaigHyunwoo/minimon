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
	
	

	
}
