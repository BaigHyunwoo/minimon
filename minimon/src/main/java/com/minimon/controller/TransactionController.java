package com.minimon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.MonCodeData;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonTransaction;
import com.minimon.repository.MonTransactionRepository;
import com.minimon.service.EmailService;
import com.minimon.service.ResultService;
import com.minimon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 
 * 메인 서버 
 * 
 * 
 * 
 * @author 백현우
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final MonTransactionRepository monTransactionRepository;

	private final TransactionService transactionService;

    private final EmailService emailService;

	private final ResultService resultService;

	/**
	 * 
	 * URL DTO Set
	 * 
	 */
	private MonTransaction setTblMonTransaction(MonTransaction monTransaction, Map<String, Object> param) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			monTransaction.setTitle(""+param.get("title"));
			monTransaction.setTimer(Integer.parseInt(""+param.get("timer")));
			monTransaction.setStartDate(transFormat.parse(param.get("transaction_start_date").toString()));
			monTransaction.setEndDate(transFormat.parse(param.get("transaction_end_date").toString()));
			monTransaction.setStartHour(Integer.parseInt(param.get("transaction_start_hour").toString()));
			monTransaction.setEndHour(Integer.parseInt(param.get("transaction_end_hour").toString()));
			monTransaction.setTimeout(Integer.parseInt(""+param.get("timeout")));
			monTransaction.setUseable(Integer.parseInt(""+param.get("transaction_useable")));
			monTransaction.setLoadTimeCheck(Integer.parseInt("" + param.get("transaction_loadTimeCheck")));
			monTransaction.setLoadTime(Double.parseDouble(""+param.get("loadTime")));
			monTransaction.setErrLoadTime(Integer.parseInt(""+param.get("errLoadTime")));
			monTransaction.setStatus(Integer.parseInt(""+param.get("status")));
			monTransaction.setTransactionCode(""+param.get("transactionCode"));
			monTransaction.setUptDate(new Date());
			if(monTransaction.getRegDate() == null) monTransaction.setRegDate(new Date());
			monTransaction.setCodeDatas(objectMapper.readValue(param.get("codeDatas").toString(), new TypeReference<List<MonCodeData>>(){}));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return monTransaction;
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

    		List<MonTransaction> transactionList = monTransactionRepository.findAll();
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

    		monTransactionRepository.save(setTblMonTransaction(new MonTransaction(), param));
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
    		


    		MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);

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

    		MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {

    			monTransactionRepository.save(setTblMonTransaction(existsTransaction, param));
    			
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


    		MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {
    			
    			monTransactionRepository.delete(existsTransaction);
    			
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
	@RequestMapping(value="/transactionCheck", method= RequestMethod.POST)
	public Map<String, Object> transactionCheck(MultipartFile transactionFile) {
    	Map<String, Object> result = new HashMap<String, Object>();

    	try {
    		List<MonCodeData> codeDatas = new ArrayList<MonCodeData>();
    		
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
		    		MonCodeData monCodeData = transactionService.getCodeData(line);
		    		if(monCodeData != null) {
		    			codeDatas.add(monCodeData);
			    		log.debug(monCodeData.getAction()+" "+ monCodeData.getSelector_type()+ "  "+ monCodeData.getSelector_value()+"     "+ monCodeData.getValue());
		    		}

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

    		MonTransaction existsTransaction = monTransactionRepository.findBySeq(seq);
    		
    		if(existsTransaction != null) {
    			
    			Map<String, Object> logData = transactionService.executeTransaction(existsTransaction.getCodeDatas());
				Map<String, Object> data = transactionService.errorCheckTransaction(existsTransaction, logData);
				result.put(""+existsTransaction.getSeq(), data);

				MonResult monResult = resultService.saveResult(data);
				resultService.sendResultByProperties(monResult);

    		}
    		
    		result.put("data", seq);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}

    
}