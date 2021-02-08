package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.service.UrlService;
import com.minimon.vo.MonUrlCheckVO;
import com.minimon.vo.MonitoringResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/url")
@Api(tags = {"Url Controller"})
public class UrlController {
    private final UrlService urlService;


    @ApiOperation(value = "URL 목록 조회", response = MonUrl.class)
    @GetMapping(path = "")
    public CommonResponse getUrls() {
        return new CommonResponse(urlService.getUrlList());
    }

    @ApiOperation(value = "URL 조회", response = MonUrl.class)
    @GetMapping(path = "/{seq}")
    public CommonResponse get(@PathVariable("seq") int seq) {
        Optional url = urlService.getUrl(seq);
        if (!url.isPresent()) {
            return CommonResponse.fail("해당 URL 정보가 존재하지 않습니다.");
        }
        return new CommonResponse(url.get());
    }

    @ApiOperation(value = "URL 생성", response = MonUrl.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonUrl monUrl) {
        return new CommonResponse(urlService.saveUrl(monUrl));
    }

    @ApiOperation(value = "URL 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonUrl monUrl) {
        if (!urlService.editUrl(monUrl)) {
            return CommonResponse.fail("해당 URL 정보가 존재하지 않습니다.");
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "URL 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        if (!urlService.remove(seq)) {
            return CommonResponse.fail("해당 URL 정보가 존재하지 않습니다.");
        }
        return new CommonResponse();
    }

    @ApiOperation(value = "URL 검사 테스트 실행", response = MonitoringResultVO.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestBody MonUrlCheckVO monUrlCheckVO) {
        return new CommonResponse(urlService.executeUrl(monUrlCheckVO.getUrl(), monUrlCheckVO.getTimeout()));
    }

    @ApiOperation(value = "URL 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        MonResult monResult = urlService.executeUrl(seq);
        if (monResult == null) {
            return CommonResponse.fail("해당 URL 정보가 존재하지 않습니다.");
        }
        return new CommonResponse(monResult);
    }
}