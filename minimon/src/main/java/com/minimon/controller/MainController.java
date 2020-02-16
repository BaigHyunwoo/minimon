package com.minimon.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;



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
	
	private String className = this.getClass().toString();
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);

	

	
	/**
	 * 
	 * 메인 화면 접근
	 * 
	 */
	@RequestMapping("/")
	public ModelAndView main(@RequestParam Map<String, Object> map) {
		ModelAndView mav = new ModelAndView("view/index");
        return mav;
	}
	
}