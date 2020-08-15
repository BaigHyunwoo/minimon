package com.minimon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@Api(value = "ResultController")
public class ResultController {

    @PostMapping(path = "/result")
    public void resultReceiver(@RequestParam Map<String, Object> param) {
        log.info("====RESULT====");
        log.info(param.toString());
    }

}