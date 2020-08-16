package com.minimon.controller;

import com.minimon.entity.MonUrl;
import com.minimon.service.ResultService;
import com.minimon.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController(value = "url")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;
    private final ResultService resultService;

    @GetMapping(path = "")
    public List<MonUrl> getUrls() {
        return urlService.getUrlList();
    }

    @PostMapping(path = "")
    public MonUrl createUrl(@RequestBody MonUrl monUrl) {
        urlService.saveUrl(monUrl);
        return monUrl;
    }

    @GetMapping(path = "/{seq}")
    public MonUrl getUrl(@PathVariable("seq") int seq) {
        return urlService.getUrl(seq);
    }

    @PutMapping(path = "")
    public boolean updateUrl(@RequestBody MonUrl monUrl) {
        return urlService.editUrl(monUrl);
    }

    @DeleteMapping(path = "/{seq}")
    public boolean delete(@PathVariable("seq") int seq) {
        return urlService.remove(seq);
    }

    @PostMapping(path = "/check")
    public Map<String, Object> urlCheck(@RequestParam Map<String, Object> data) throws Exception {
        return urlService.executeUrl(data.get("url").toString(), Integer.parseInt(data.get("timeout").toString()));
    }

    @PostMapping(path = "/url/{seq}/execute")
    public boolean urlExecute(@PathVariable("seq") int seq) {
        return urlService.executeUrl(seq);
    }
}