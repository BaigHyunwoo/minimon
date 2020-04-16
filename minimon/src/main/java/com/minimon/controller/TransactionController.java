package com.minimon.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.TblMonCodeData;
import com.minimon.entity.TblMonTransaction;
import com.minimon.repository.TblMonTransactionRepository;
import com.minimon.service.EmailService;
import com.minimon.service.TransactionService;



/**
 * 
 * 메인 서버 
 * 
 * 
 * 
 * @author 백현우
 *
 */
@RestController
public class TransactionController {

	@Autowired
	TblMonTransactionRepository tblMonTransactionRepository;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	EmailService emailService;


	/**
	 * 
	 * URL DTO Set
	 * 
	 */
	private TblMonTransaction setTblMonTransaction(TblMonTransaction tblMonTransaction, Map<String, Object> param) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			
			tblMonTransaction.setTitle(""+param.get("title"));
			tblMonTransaction.setTimer(Integer.parseInt(""+param.get("timer")));
			tblMonTransaction.setTimeout(Integer.parseInt(""+param.get("timeout")));
			tblMonTransaction.setUseable(Integer.parseInt(""+param.get("transaction_useable")));
			tblMonTransaction.setLoadTime(Double.parseDouble(""+param.get("loadTime")));
			tblMonTransaction.setLoadTimePer(Integer.parseInt(""+param.get("loadTimePer")));
			tblMonTransaction.setStatus(Integer.parseInt(""+param.get("status")));
			tblMonTransaction.setTransactionCode(""+param.get("transactionCode"));
			tblMonTransaction.setUptDate(new Date());
			if(tblMonTransaction.getRegDate() == null) tblMonTransaction.setRegDate(new Date());
			tblMonTransaction.setCodeDatas(objectMapper.readValue(param.get("codeDatas").toString(), new TypeReference<List<TblMonCodeData>>(){}));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return tblMonTransaction;
	}
	
	/**
	 * 
	 * TRANSACTION LIST  호출
	 * 
	 */
    @RequestMapping(path = "/transaction", method= RequestMethod.GET)
	public HashMap<String, Object> getUrls() {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		List<TblMonTransaction> transactionList = tblMonTransactionRepository.findAll();
    		result.put("transactionList", transactionList);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}
    

	/**
	 * 
	 * transaction 생성
	 * 
	 */
	@RequestMapping(path = "/transaction", method= RequestMethod.POST)
	public HashMap<String, Object> createTransaction(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {

    		tblMonTransactionRepository.save(setTblMonTransaction(new TblMonTransaction(), param));
			result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}


	/**
	 * 
	 * transaction INFO  호출
	 * 
	 */
    @RequestMapping(path = "/transaction/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> getTransaction(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {
    		


    		TblMonTransaction existsTransaction = tblMonTransactionRepository.findBySeq(seq);

    		result.put("data", existsTransaction);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}



	/**
	 * 
	 * transaction 업데이트
	 * 
	 */
    @RequestMapping(path = "/transaction/{seq}", method= RequestMethod.PUT)
	public HashMap<String, Object> updateTransaction(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonTransaction existsTransaction = tblMonTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {

    			tblMonTransactionRepository.save(setTblMonTransaction(existsTransaction, param));
    			
    		}
    		
    		result.put("data", seq);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}

    




	/**
	 * 
	 * transaction 삭제
	 * 
	 */
    @RequestMapping(path = "/transaction/{seq}", method= RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {


    		TblMonTransaction existsTransaction = tblMonTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {
    			
    			tblMonTransactionRepository.delete(existsTransaction);
    			
    		}
    		
    		result.put("data", seq);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}

    
	/**
	 * 
	 * Upload transaction Code
	 * 
	 */
    @ResponseBody
	@RequestMapping(value="/transactionCheck", method=RequestMethod.POST)
	public Map<String, Object> transactionCheck(MultipartFile transactionFile) {
    	Map<String, Object> result = new HashMap<String, Object>();

    	try {
    		List<TblMonCodeData> codeDatas = new ArrayList<TblMonCodeData>();
    		
		    /*
		     * READ CODE FILE
		     */
			BufferedReader br;
			String line;
		    InputStream is = transactionFile.getInputStream();
		    br = new BufferedReader(new InputStreamReader(is));
		    boolean check = false;
		    while ((line = br.readLine()) != null) {
		    	
		    	/*
		    	 * TEST FUNCTION START
		    	 */
		    	if(line.indexOf("@Test") > 0) check = true;
		    	if(check == true) {
		    		TblMonCodeData tblMonCodeData = transactionService.getCodeData(line);
		    		if(tblMonCodeData != null) codeDatas.add(tblMonCodeData);
		    	}
		    }

    		Map<String, Object> logData = transactionService.executeTransaction(codeDatas);
    		result.put("data", logData);
    		result.put("codeDatas", codeDatas);
        	result.put("result", "success");

			
		} catch (Exception e) {
			
			e.printStackTrace();
    		result.put("result", "ERR");
    		result.put("msg", e.getMessage());
			
		}
		
        return result;
	}


	/**
	 * 
	 * transaction  검사 실행
	 * 
	 */
    @RequestMapping(path = "/transactionExecute/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> transactionExecute(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonTransaction existsTransaction = tblMonTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {
    			
    			Map<String, Object> logData = transactionService.executeTransaction(existsTransaction.getCodeDatas());
    			result.put(""+existsTransaction.getSeq(), transactionService.errorCheckTransaction(existsTransaction, logData));
				emailService.sendSimpleMessage("qorto12@naver.com", "모니터링 검사 결과", result.toString());
    			
    		}
    		
    		result.put("data", seq);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}

    
}