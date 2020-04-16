package com.minimon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minimon.MinimonApplication;
import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonTransaction;
import com.minimon.entity.TblMonUrl;
import com.minimon.repository.TblMonApiRepository;
import com.minimon.repository.TblMonTransactionRepository;
import com.minimon.repository.TblMonUrlRepository;



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
public class MainController {
	
	@Autowired
	TblMonUrlRepository tblMonUrlRepository;

	@Autowired
	TblMonApiRepository tblMonApiRepository;

	@Autowired
	TblMonTransactionRepository tblMonTransactionRepository;
	

	private static final Logger logger = LoggerFactory.getLogger(MainController.class.getName());
	
	/**
	 * 
	 * 메인 화면 접근
	 * 
	 */
    @RequestMapping(path = "/main/index", method= RequestMethod.GET)
	public ModelAndView main(@RequestParam Map<String, Object> map) {
		
		List<TblMonUrl> urlList = tblMonUrlRepository.findAll();
		List<TblMonApi> apiList = tblMonApiRepository.findAll();
		List<TblMonTransaction> transactionList = tblMonTransactionRepository.findAll();
		
		
		ModelAndView mav = new ModelAndView("view/main/index");
		mav.addObject("urlList", urlList);
		mav.addObject("apiList", apiList);
		mav.addObject("transactionList", transactionList);
		mav.addObject("status", 200);
        return mav;
	}
    

	/**
	 * 
	 * Driver Path 등록
	 * 
	 */
	@RequestMapping(path = "/main/driver", method= RequestMethod.POST)
	public HashMap<String, Object> driver(@RequestParam Map<String, Object> param) {
    	HashMap<String, Object> result = new HashMap<String, Object>();

    	try {

    		logger.info("driverPath : "+param.get("driverPath"));
    		MinimonApplication.setDriverPath(""+param.get("driverPath"));
    		
			result.put("result", "success");
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
        return result;
	}
	
}