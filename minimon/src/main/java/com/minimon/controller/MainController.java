package com.minimon.controller;

import com.minimon.MinimonApplication;
import com.minimon.common.CommonResponse;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
@Api(tags = {"Main Controller"})
public class MainController {

    @PostMapping(path = "/set")
    public CommonResponse driver(@RequestParam String driverPath) {
        log.info("driverPath : " + driverPath);
        MinimonApplication.setDriverPath(driverPath);
        return new CommonResponse();
    }
}