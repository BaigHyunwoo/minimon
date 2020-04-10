package com.minimon.controller;

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
	TblMonApiRepository TblMonApiRepository;

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
		TblMonApi.setUseable(Integer.parseInt(""+param.get("useable")));
		TblMonApi.setLoadTime(Double.parseDouble(""+param.get("loadTime")));
		TblMonApi.setLoadTimePer(Integer.parseInt(""+param.get("loadTimePer")));
		TblMonApi.setPayLoad(Double.parseDouble(""+param.get("payLoad")));
		TblMonApi.setPayLoadPer(Integer.parseInt(""+param.get("payLoadPer")));
		TblMonApi.setStatus(Integer.parseInt(""+param.get("status")));
		TblMonApi.setUptDate(new Date());
		if(TblMonApi.getRegDate() == null) TblMonApi.setRegDate(new Date());
		return TblMonApi;
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

    		List<TblMonApi> apiList = TblMonApiRepository.findAll();
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
	public HashMap<String, Object> createUrl(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {

			TblMonApiRepository.save(setTblMonApi(new TblMonApi(), param));
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
	public HashMap<String, Object> getUrl(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi TblMonApi = TblMonApiRepository.findBySeq(seq);
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
	public HashMap<String, Object> updateUrl(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi existsApi = TblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {
    			
    			TblMonApiRepository.save(setTblMonApi(existsApi, param));
    			
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

    		TblMonApi existsApi = TblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {
    			
    			TblMonApiRepository.delete(existsApi);
    			
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
	public Map<String, Object> urlCheck(@RequestParam Map<String, Object> data) {
    	Map<String, Object> result = new HashMap<String, Object>();

    	try {

    		//result.put("data", apiService.executeApi(""+data.get("url"), Integer.parseInt(""+data.get("url"))));
    		result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}
    


	/**
	 * 
	 * URL  검사 실행
	 * 
	 */
    @RequestMapping(path = "/apiExecute/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> urlExecute(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonApi existsApi = TblMonApiRepository.findBySeq(seq);
    		
    		if(existsApi != null) {

				//Map<String, Object> logData = apiService.executeApi(existsApi.getUrl(), existsApi.getTimeout());
				//result.put(existsUrl.getUrl(), apiService.errorCheckUrl(existsUrl, logData));
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