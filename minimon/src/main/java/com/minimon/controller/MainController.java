package com.minimon.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonApiParam;
import com.minimon.entity.TblMonUrl;
import com.minimon.repository.TblMonApiRepository;
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
	
	/**
	 * 
	 * 메인 화면 접근
	 * 
	 */
    @RequestMapping(path = "/main/index", method= RequestMethod.GET)
	public ModelAndView main(@RequestParam Map<String, Object> map) {
		
		List<TblMonUrl> urlList = tblMonUrlRepository.findAll();

		TblMonApiParam tblMonApiParam = new TblMonApiParam();
		tblMonApiParam.setParam_key("key");
		tblMonApiParam.setParam_value("data");
		tblMonApiParam.setRegDate(new Date());
		tblMonApiParam.setUptDate(new Date());
		
		TblMonApi TblMonApi = new TblMonApi();
		TblMonApi.setUrl("http://localhost:8080/url");
		TblMonApi.setTitle("GET TEST");
		TblMonApi.setTimeout(5);
		TblMonApi.setTimer(5);
		TblMonApi.setUseable(1);
		TblMonApi.setStatus(200);
		TblMonApi.setLoadTime(5000);
		TblMonApi.setLoadTimePer(5);
		TblMonApi.setPayLoad(500);
		TblMonApi.setPayLoadPer(5);
		TblMonApi.setData_type("JSON");
		TblMonApi.setMethod("GET");
		TblMonApi.setUptDate(new Date());
		TblMonApi.setRegDate(new Date());
		TblMonApi.addApiParam(tblMonApiParam);
		tblMonApiRepository.save(TblMonApi);
		List<TblMonApi> apiList = tblMonApiRepository.findAll();
		
		
		ModelAndView mav = new ModelAndView("view/main/index");
		mav.addObject("urlList", urlList);
		mav.addObject("apiList", apiList);
		mav.addObject("status", 200);
        return mav;
	}
	
}