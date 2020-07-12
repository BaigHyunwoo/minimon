package com.minimon.controller;

import com.minimon.MinimonApplication;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonTransaction;
import com.minimon.repository.MonApiRepository;
import com.minimon.repository.MonTransactionRepository;
import com.minimon.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 메인 서버
 *
 * @author 백현우
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final UrlService urlService;

    private final MonApiRepository monApiRepository;

    private final MonTransactionRepository monTransactionRepository;

    /**
     * 메인 화면 접근
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public ModelAndView main(@RequestParam Map<String, Object> map) {

        List<MonApi> apiList = monApiRepository.findAll();
        List<MonTransaction> transactionList = monTransactionRepository.findAll();


        ModelAndView mav = new ModelAndView("index");
        mav.addObject("urlList", urlService.getMonUrls());
        mav.addObject("apiList", apiList);
        mav.addObject("transactionList", transactionList);
        mav.addObject("status", 200);
        return mav;
    }


    /**
     * Driver Path 등록
     */
    @RequestMapping(path = "/main/driver", method = RequestMethod.POST)
    public HashMap<String, Object> driver(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            log.info("driverPath : " + param.get("driverPath"));
            MinimonApplication.setDriverPath("" + param.get("driverPath"));

            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

}