package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.service.MonApiService;
import com.minimon.vo.MonitoringResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/monApi")
@Api(tags = {"Monitoring Api Controller"})
public class MonApiController {
    private final MonApiService monApiService;


    @ApiOperation(value = "API 목록 조회", response = MonApi.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(monApiService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "API 조회", response = MonApi.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        MonApi api = monApiService.get(seq);
        if (api == null) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(api);
    }

    @ApiOperation(value = "API 생성", response = MonApi.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonApi monApi) {
        return new CommonResponse(monApiService.save(monApi));
    }

    @ApiOperation(value = "API 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonApi monApi) {
        if (!monApiService.edit(monApi)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "API 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!monApiService.remove(seq)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "API 검사 테스트 실행", response = MonitoringResultVO.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestParam String url, @RequestParam String method, @RequestBody(required = false) String data) {
        return new CommonResponse(monApiService.execute(url, method, data));
    }

    @ApiOperation(value = "API 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        MonResult monResult = monApiService.execute(seq);
        if (monResult == null) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(monResult);
    }
}