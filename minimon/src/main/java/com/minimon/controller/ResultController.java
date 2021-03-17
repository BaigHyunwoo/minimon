package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.service.ResultService;
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
@RequestMapping("/result")
@Api(tags = {"Result Controller"})
public class ResultController {

    private final ResultService resultService;


    @ApiOperation(value = "Result 목록 조회", response = MonResult.class)
    @GetMapping(path = "")
    public CommonResponse getList(@ModelAttribute CommonSearchSpec commonSearchSpec) {
        return new CommonResponse(resultService.getList(commonSearchSpec));
    }

    @ApiOperation(value = "Result 조회", response = MonResult.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional result = resultService.get(seq);
        if (!result.isPresent()) {
            return CommonResponse.notExistResponse();
        }
        return new CommonResponse(result.get());
    }
}