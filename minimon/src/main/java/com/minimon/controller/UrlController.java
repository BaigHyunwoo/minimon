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

    		TblMonUrl tblMonUrl = setTblMonUrl(new TblMonUrl(), param);
    		
    		tblMonUrl.setRegDate(new Date());
    		tblMonUrl.setUptDate(new Date());
			tblMonUrlRepository.save(tblMonUrl);
			result.put("seq", tblMonUrl.getSeq());
			result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}

	private TblMonUrl setTblMonUrl(TblMonUrl tblMonUrl, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
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

    		TblMonUrl existsData = tblMonUrlRepository.findBySeq(seq);
    		
    		if(existsData != null) tblMonUrlRepository.save(tblMonUrl);
    		
    		result.put("data", tblMonUrl);
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
}