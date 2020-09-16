package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.service.UrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return new CommonResponse(urlService.getUrl(seq));
    }

    @ApiOperation(value = "URL 생성", response = MonUrl.class)
    @PostMapping(path = "")
    public CommonResponse create(@RequestBody MonUrl monUrl) {
        return new CommonResponse(urlService.saveUrl(monUrl));
    }

    @ApiOperation(value = "URL 수정", response = boolean.class)
    @PutMapping(path = "")
    public CommonResponse update(@RequestBody MonUrl monUrl) {
        return new CommonResponse(urlService.editUrl(monUrl));
    }

    @ApiOperation(value = "URL 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public CommonResponse delete(@PathVariable("seq") int seq) {
        return new CommonResponse(urlService.remove(seq));
    }

    @ApiOperation(value = "URL 검사 테스트 실행", response = Map.class)
    @PostMapping(path = "/check")
    public CommonResponse check(@RequestParam Map<String, Object> data) throws Exception {
        return new CommonResponse(urlService.executeUrl(data.get("url").toString(), Integer.parseInt(data.get("timeout").toString())));
    }

    @ApiOperation(value = "URL 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public CommonResponse execute(@PathVariable("seq") int seq) {
        return new CommonResponse(urlService.executeUrl(seq));
    }
}