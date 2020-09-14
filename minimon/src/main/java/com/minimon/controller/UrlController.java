package com.minimon.controller;

import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.service.UrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/url")
@Api(tags = {"Url Controller"})
public class UrlController {
    private final UrlService urlService;


    @ApiOperation(value = "URL 목록 조회", response = MonUrl.class, responseContainer = "List")
    @GetMapping(path = "")
    public List<MonUrl> getUrls() {
        return urlService.getUrlList();
    }

    @ApiOperation(value = "URL 조회", response = MonUrl.class)
    @GetMapping(path = "/{seq}")
    public MonUrl get(@PathVariable("seq") int seq) {
        return urlService.getUrl(seq);
    }

    @ApiOperation(value = "URL 생성", response = MonUrl.class)
    @PostMapping(path = "")
    public MonUrl create(@RequestBody MonUrl monUrl) {
        urlService.saveUrl(monUrl);
        return monUrl;
    }

    @ApiOperation(value = "URL 수정", response = MonUrl.class)
    @PutMapping(path = "")
    public boolean update(@RequestBody MonUrl monUrl) {
        return urlService.editUrl(monUrl);
    }

    @ApiOperation(value = "URL 삭제", response = boolean.class)
    @DeleteMapping(path = "/{seq}")
    public boolean delete(@PathVariable("seq") int seq) {
        return urlService.remove(seq);
    }

    @ApiOperation(value = "URL 검사 테스트 실행", response = Map.class)
    @PostMapping(path = "/check")
    public Map<String, Object> check(@RequestParam Map<String, Object> data) throws Exception {
        return urlService.executeUrl(data.get("url").toString(), Integer.parseInt(data.get("timeout").toString()));
    }

    @ApiOperation(value = "URL 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public MonResult execute(@PathVariable("seq") int seq) {
        return urlService.executeUrl(seq);
    }
}