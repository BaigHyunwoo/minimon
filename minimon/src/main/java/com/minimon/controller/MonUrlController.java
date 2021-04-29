package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.service.MonUrlService;
import com.minimon.service.MonitoringService;
import com.minimon.vo.MonUrlCheckVO;
import com.minimon.vo.MonitoringResultVO;
import com.minimon.vo.MonitoringTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/monUrl")
@Api(tags = {"Monitoring Url Controller"})
public class MonUrlController {
    private final MonUrlService monUrlService;
    private final MonitoringService monitoringService;


    @ApiOperation(value = "URL 목록 조회", response = MonUrl.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(monUrlService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "URL 조회", response = MonUrl.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional url = monUrlService.get(seq);
        if (!url.isPresent()) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(url.get());
    }

    @ApiOperation(value = "URL 생성", response = MonUrl.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonUrl monUrl) {
        return new CommonResponse(monUrlService.save(monUrl));
    }

    @ApiOperation(value = "URL 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonUrl monUrl) {
        if (!monUrlService.edit(monUrl)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "URL 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!monUrlService.remove(seq)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "URL 검사 테스트 실행", response = MonitoringResultVO.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestBody MonUrlCheckVO monUrlCheckVO) {
        return new CommonResponse(monUrlService.check(monUrlCheckVO.getUrl(), monUrlCheckVO.getTimeout()));
    }

    @ApiOperation(value = "URL 검사 Task 추가", response = MonResult.class)
    @GetMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        if (!monUrlService.get(seq).isPresent()) {
            return CommonResponse.notExistResponse();
        }

        monitoringService.addTask(MonitoringTaskVO.builder()
                .monitoringType(MonitoringTypeEnum.URL)
                .seq(seq)
                .task(monUrlService.executeTask(seq))
                .build());
        return new CommonResponse();
    }
}