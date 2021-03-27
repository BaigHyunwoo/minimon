package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonAct;
import com.minimon.entity.MonResult;
import com.minimon.service.MonActService;
import com.minimon.vo.MonitoringResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/monAct")
@Api(tags = {"Monitoring Act Controller"})
public class MonActController {

    private final MonActService monActService;


    @ApiOperation(value = "목록 조회", response = MonAct.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(monActService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "조회", response = MonAct.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional transaction = monActService.get(seq);
        if (!transaction.isPresent()) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(transaction.get());
    }

    @ApiOperation(value = "생성", response = MonAct.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonAct monAct) {
        return new CommonResponse(monActService.save(monAct));
    }

    @ApiOperation(value = "수정", response = void.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonAct monTransaction) {
        if (!monActService.edit(monTransaction)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "삭제", response = void.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!monActService.remove(seq)) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "검사 테스트", produces = "multipart/form-data", response = MonitoringResultVO.class)
    @PostMapping(value = "/check")
    public CommonResponse check(@RequestParam MultipartFile actFile) {
        MonitoringResultVO monitoringResultVO = monActService.executeCodeList(actFile);
        if (monitoringResultVO == null) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(monitoringResultVO);
    }

    @ApiOperation(value = "검사 실행", response = MonResult.class)
    @GetMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        MonResult monResult = monActService.execute(seq);
        if (monResult == null) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(monResult);
    }
}