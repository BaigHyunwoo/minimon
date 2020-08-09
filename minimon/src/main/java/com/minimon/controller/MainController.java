package com.minimon.controller;

import com.minimon.MinimonApplication;
import com.minimon.service.ApiService;
import com.minimon.service.TransactionService;
import com.minimon.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final UrlService urlService;
    private final ApiService apiService;
    private final TransactionService transactionService;

    @GetMapping(path = "/index")
    public ModelAndView main() {
        ModelAndView mav = new ModelAndView("");
        mav.addObject("urlList", urlService.getMonUrls());
        mav.addObject("apiList", apiService.getApis());
        mav.addObject("transactionList", transactionService.getTransactions());
        mav.addObject("status", 200);
        return mav;
    }

    @PostMapping(path = "/set/{driverPath}")
    public HashMap<String, Object> driver(@PathVariable String driverPath) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        log.info("driverPath : " + driverPath);
        MinimonApplication.setDriverPath(driverPath);
        result.put("result", "success");
        return result;
    }
}