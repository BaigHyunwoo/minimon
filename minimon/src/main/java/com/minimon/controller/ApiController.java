package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.service.ApiService;
import com.minimon.vo.MonitoringResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
        return new CommonResponse(apiService.getApis());
    }

    @ApiOperation(value = "API 조회", response = MonApi.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional api = apiService.getApi(seq);
        if(!api.isPresent()) {
            return CommonResponse.fail("해당 API 정보가 존재하지 않습니다.");
        }
        return new CommonResponse(api.get());
    }

    @ApiOperation(value = "API 생성", response = MonApi.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonApi monApi) {
        return new CommonResponse(apiService.saveApi(monApi));
    }

    @ApiOperation(value = "API 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonApi monApi) {
        if(!apiService.editApi(monApi)) {
            return CommonResponse.fail("해당 API 정보가 존재하지 않습니다.");
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "API 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if(!apiService.remove(seq)) {
            return CommonResponse.fail("해당 API 정보가 존재하지 않습니다.");
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "API 검사 테스트 실행", response = MonitoringResultVO.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestParam String url, @RequestParam String method, @RequestBody(required = false) String data) {
        return new CommonResponse(apiService.executeApi(url, method, data));
    }

    @ApiOperation(value = "API 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        MonResult monResult = apiService.executeApi(seq);
        if(monResult == null) {
            return CommonResponse.fail("해당 API 정보가 존재하지 않습니다.");
        }
        return new CommonResponse(monResult);
    }
}