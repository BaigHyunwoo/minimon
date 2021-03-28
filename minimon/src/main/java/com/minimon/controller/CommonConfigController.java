package com.minimon.controller;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonResponse;
import com.minimon.service.PropertiesService;
import com.minimon.vo.MonitoringResultVO;
import com.minimon.vo.ResultReceiveEditVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/commonConfig")
@Api(tags = {"CommonConfig Controller"})
public class CommonConfigController {
    private final CommonProperties commonProperties;
    private final PropertiesService propertiesService;


    @ApiOperation(value = "Property 조회", response = CommonProperties.class)
    @GetMapping(path = "")
    public CommonResponse getList() {
        return new CommonResponse(commonProperties);
    }

    @ApiOperation(value = "드라이버 파일 변경", produces = "multipart/form-data")
    @PutMapping(path = "/path/driver")
    public CommonResponse setDriverPath(@RequestParam MultipartFile driver) {
        propertiesService.setDriverPath(driver);
        return new CommonResponse();
    }

    @ApiOperation(value = "모니터링 수신 API 경로 설정")
    @PutMapping(path = "/path/result")
    public CommonResponse setResultPath(@RequestBody ResultReceiveEditVO resultReceiveEditVO) {
        propertiesService.setResultReceivePath(resultReceiveEditVO.getResultReceivePath());
        return new CommonResponse();
    }
}