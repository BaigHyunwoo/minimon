package com.minimon.controller;

import com.minimon.common.CommonResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Api(tags = {"Test Controller"})
public class TestController {

    @GetMapping(path = "")
    public CommonResponse get() {
        log.info("====TEST==== GET");
        return new CommonResponse("OK");
    }

    @PostMapping(path = "")
    public CommonResponse post(@RequestBody Map<String, String> param) {
        log.info("====TEST==== POST "+param.toString());
        return new CommonResponse(param);
    }

    @PutMapping(path = "")
    public CommonResponse put(@RequestBody Map<String, String> param) {
        log.info("====TEST==== PUT "+param.toString());
        return new CommonResponse(param);
    }

    @DeleteMapping(path = "")
    public CommonResponse delete() {
        log.info("====TEST==== DELETE");
        return new CommonResponse("OK");
    }
}