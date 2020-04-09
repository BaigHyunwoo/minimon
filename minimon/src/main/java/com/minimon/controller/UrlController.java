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

import com.minimon.entity.TblMonUrl;
import com.minimon.repository.TblMonUrlRepository;
import com.minimon.service.UrlService;



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
public class UrlController {

	@Autowired
	UrlService urlService;
	
	@Autowired
	TblMonUrlRepository tblMonUrlRepository;

	/**
	 * 
	 * URL DTO Set
	 * 
	 */
	private TblMonUrl setTblMonUrl(TblMonUrl tblMonUrl, Map<String, Object> param) {
		tblMonUrl.setTitle(""+param.get("title"));
		tblMonUrl.setUrl(""+param.get("url"));
		tblMonUrl.setTimer(Integer.parseInt(""+param.get("timer")));
		tblMonUrl.setTimeout(Integer.parseInt(""+param.get("timeout")));
		tblMonUrl.setUseable(Integer.parseInt(""+param.get("useable")));
		tblMonUrl.setLoadTime(Double.parseDouble(""+param.get("loadTime")));
		tblMonUrl.setLoadTimePer(Integer.parseInt(""+param.get("loadTimePer")));
		tblMonUrl.setPayLoad(Double.parseDouble(""+param.get("payLoad")));
		tblMonUrl.setPayLoadPer(Integer.parseInt(""+param.get("payLoadPer")));
		tblMonUrl.setStatus(Integer.parseInt(""+param.get("status")));
		tblMonUrl.setUptDate(new Date());
		if(tblMonUrl.getRegDate() == null) tblMonUrl.setRegDate(new Date());
		return tblMonUrl;
	}
	
	/**
	 * 
	 * URL LIST  호출
	 * 
	 */
    @RequestMapping(path = "/url", method= RequestMethod.GET)
	public HashMap<String, Object> getUrls() {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		List<TblMonUrl> urlList = tblMonUrlRepository.findAll();
    		result.put("urlList", urlList);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}
    

	/**
	 * 
	 * URL 생성
	 * 
	 */
    @RequestMapping(path = "/url", method= RequestMethod.POST)
	public HashMap<String, Object> createUrl(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {

			tblMonUrlRepository.save(setTblMonUrl(new TblMonUrl(), param));
			result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}


	/**
	 * 
	 * URL INFO  호출
	 * 
	 */
    @RequestMapping(path = "/url/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> getUrl(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonUrl tblMonUrl = tblMonUrlRepository.findBySeq(seq);
    		result.put("data", tblMonUrl);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}



	/**
	 * 
	 * URL 업데이트
	 * 
	 */
    @RequestMapping(path = "/url/{seq}", method= RequestMethod.PUT)
	public HashMap<String, Object> updateUrl(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonUrl existsUrl = tblMonUrlRepository.findBySeq(seq);
    		
    		if(existsUrl != null) {
    			
    			tblMonUrlRepository.save(setTblMonUrl(existsUrl, param));
    			
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
	 * URL 삭제
	 * 
	 */
    @RequestMapping(path = "/url/{seq}", method= RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonUrl existsUrl = tblMonUrlRepository.findBySeq(seq);
    		
    		if(existsUrl != null) {
    			
    			tblMonUrlRepository.delete(existsUrl);
    			
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
	 * URL CHECK
	 * 
	 */
    @RequestMapping(path = "/urlCheck", method= RequestMethod.POST)
	public Map<String, Object> urlCheck(@RequestParam Map<String, Object> data) {
    	Map<String, Object> result = new HashMap<String, Object>();

    	try {

    		result.put("data", urlService.executeUrl(""+data.get("url")));
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
    @RequestMapping(path = "/urlExecute/{seq}", method= RequestMethod.GET)
	public HashMap<String, Object> urlExecute(@PathVariable("seq") int seq) {
    	HashMap<String, Object> result = new HashMap<String, Object>();
    	
    	try {

    		TblMonUrl existsUrl = tblMonUrlRepository.findBySeq(seq);
    		
    		if(existsUrl != null) {

				Map<String, Object> logData = urlService.executeUrl(existsUrl.getUrl());
				result.put(existsUrl.getUrl(), urlService.errorCheckUrl(existsUrl, logData));
    			
    		}
    		
    		result.put("data", seq);
    		result.put("result", "success");
    		
    		
    	} catch (Exception e) {
    		
			e.printStackTrace();
			
		}
		
        return result;
	}

    
}