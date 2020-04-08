package com.minimon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.minimon.entity.TblMonUrl;
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
	
	
	/**
	 * 
	 * 메인 화면 접근
	 * 
	 */
    @RequestMapping(path = "/main/index", method= RequestMethod.GET)
	public ModelAndView main(@RequestParam Map<String, Object> map) {
		
		// Call Mon List 
		List<TblMonUrl> resultList = tblMonUrlRepository.findAll();
		
		
		ModelAndView mav = new ModelAndView("view/index");
		mav.addObject("resultList", resultList);
		mav.addObject("status", 200);
        return mav;
	}
	
}