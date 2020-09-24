package com.minimon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/result")
@Api(tags = {"Result Controller"})
public class ResultController {

    @PostMapping(path = "")
    public void resultReceiver(@RequestBody Map<String, Object> param) {
        log.info("====RESULT====");
        log.info(param.toString());
    }
}