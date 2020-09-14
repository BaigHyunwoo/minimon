package com.minimon.controller;

import com.minimon.MinimonApplication;
import com.minimon.service.ApiService;
import com.minimon.service.TransactionService;
import com.minimon.service.UrlService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;


@Slf4j
@RestController("/main")
@RequiredArgsConstructor
@Api(tags = {"Main Controller"})
public class MainController {

    @PostMapping(path = "/set/{driverPath}")
    public HashMap<String, Object> driver(@PathVariable String driverPath) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        log.info("driverPath : " + driverPath);
        MinimonApplication.setDriverPath(driverPath);
        result.put("result", "success");
        return result;
    }
}