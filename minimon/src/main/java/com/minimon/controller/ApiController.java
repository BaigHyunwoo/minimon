package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"Api Controller"})
public class ApiController {
    private final ApiService apiService;


    @ApiOperation(value = "API 목록 조회", response = MonApi.class)
    @GetMapping(path = "")
    public CommonResponse getApis() {
        return new CommonResponse(apiService.getApiList());
    }

    @ApiOperation(value = "API 조회", response = MonApi.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        return new CommonResponse(apiService.getApi(seq));
    }

    @ApiOperation(value = "API 생성", response = MonApi.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonApi monApi) {
        return new CommonResponse(apiService.saveApi(monApi));
    }

    @ApiOperation(value = "API 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonApi monApi) {
        return new CommonResponse(apiService.editApi(monApi));
    }

    @ApiOperation(value = "API 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        return new CommonResponse(apiService.remove(seq));
    }

    @ApiOperation(value = "API 검사 테스트 실행", response = Map.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestBody MonApi monApi) throws Exception {
        return new CommonResponse(apiService.executeApi(monApi));
    }

    @ApiOperation(value = "API 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        return new CommonResponse(apiService.executeApi(seq));
    }
}