package com.minimon.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonApiParam;
import com.minimon.repository.TblMonApiRepository;
import com.minimon.service.ApiService;
import com.minimon.service.EmailService;



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
public class ApiController {

	@Autowired
	ApiService apiService;

	@Autowired
	EmailService emailService;
	
	@Autowired
	TblMonApiRepository tblMonApiRepository;

	/**
	 * 
	 * API DTO Set
	 * 
	 */
	private TblMonApi setTblMonApi(TblMonApi TblMonApi, Map<String, Object> param) {
		TblMonApi.setTitle(""+param.get("title"));
		TblMonApi.setUrl(""+param.get("url"));
		TblMonApi.setTimer(Integer.parseInt(""+param.get("timer")));
		TblMonApi.setTimeout(Integer.parseInt(""+param.get("timeout")));
		TblMonApi.setUseable(Integer.parseInt(""+param.get("api_useable")));
		TblMonApi.setLoadTime(Double.parseDouble(""+param.get("loadTime")));
		TblMonApi.setLoadTimePer(Integer.parseInt(""+param.get("loadTimePer")));
		TblMonApi.setPayLoad(Double.parseDouble(""+param.get("payLoad")));
		TblMonApi.setPayLoadPer(Integer.parseInt(""+param.get("payLoadPer")));
		TblMonApi.setStatus(Integer.parseInt(""+param.get("status")));
		TblMonApi.setData_type(""+param.get("data_type"));
		TblMonApi.setMethod(""+param.get("method"));
		TblMonApi.setResponse(""+param.get("response"));
		TblMonApi.setUptDate(new Date());
		if(TblMonApi.getRegDate() == null) TblMonApi.setRegDate(new Date());
		TblMonApi.setApiParams(setTblMonApiParams(param));
		
		return TblMonApi;
	}
	

	/**
	 * 
	 * API DTO Set
	 * 
	 */
	private ArrayList<TblMonApiParam> setTblMonApiParams(Map<String, Object> param) {
		ArrayList<TblMonApiParam> apiParams = new ArrayList<TblMonApiParam>();
		
		try {

			if(param.get("keys") == null) return apiParams;
			
			String[] keys = (""+param.get("keys")).substring(1, (""+param.get("keys")).length()-1).split(",");
			String[] values = (""+param.get("values")).substring(1, (""+param.get("values")).length()-1).split(",");
			
			for(int i = 0; i < keys.length; i++) {
				TblMonApiParam tblMonApiParam = new TblMonApiParam();
				tblMonApiParam.setParam_key(keys[i].replaceAll("\"", ""));
				tblMonApiParam.setParam_value(values[i].replaceAll("\"", ""));
				if(tblMonApiParam.getRegDate() == null) tblMonApiParam.setRegDate(new Date());
		        tblMonApiParam.setUptDate(new Date());
		        apiParams.add(tblMonApiParam);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return apiParams;
	}
	
	/**
	 * 
	 * API LIST  호출
	 * 
	 */
    @RequestMapping(path = "/api", method= RequestMethod.GET)
	public HashMap<String, Object> getUrls() {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		List<TblMonApi> apiList = tblMonApiRepository.findAll();
    		result.put("apiList", apiList);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}
    

	/**
	 * 
	 * API 생성
	 * 
	 */
    @RequestMapping(path = "/api", method= RequestMethod.POST)
	public HashMap<String, Object> createAPi(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {

    		tblMonApiRepository.save(setTblMonApi(new TblMonApi(), param));
			result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}


	/**
	 * 
	 * API INFO  호출
	 * 
	 */
    @RequestMapping(path = "/api/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> getApi(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi TblMonApi = tblMonApiRepository.findBySeq(seq);
    		result.put("data", TblMonApi);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}



	/**
	 * 
	 * API 업데이트
	 * 
	 */
    @RequestMapping(path = "/api/{seq}", method= RequestMethod.PUT)
	public HashMap<String, Object> updateApi(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {
    			
    			tblMonApiRepository.save(setTblMonApi(existsApi, param));
    			
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
	 * API 삭제
	 * 
	 */
    @RequestMapping(path = "/api/{seq}", method= RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {
    			
    			tblMonApiRepository.delete(existsApi);
    			
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
	 * API CHECK
	 * 
	 */
    @RequestMapping(path = "/apiCheck", method= RequestMethod.POST)
	public HashMap<String, Object> apiCheck(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {
        	TblMonApi tblMonApi = setTblMonApi(new TblMonApi(), param);
    		result.put("data", apiService.executeApi(tblMonApi));
    		result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}


	/**
	 * 
	 * API  검사 실행
	 * 
	 */
    @RequestMapping(path = "/apiExecute/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> apiExecute(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	try {

    		TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {

				Map<String, Object> logData = apiService.executeApi(existsApi);
				result.put(existsApi.getUrl(), apiService.errorCheckApi(existsApi, logData));
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