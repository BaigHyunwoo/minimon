package com.minimon.controller;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/commonConfig")
@Api(tags = {"CommonConfig Controller"})
public class CommonConfigController {
    private final CommonProperties commonProperties;


    @ApiOperation(value = "Property 조회", response = CommonProperties.class)
    @GetMapping(path = "")
    public CommonResponse getList() {
        return new CommonResponse(commonProperties);
    }
}