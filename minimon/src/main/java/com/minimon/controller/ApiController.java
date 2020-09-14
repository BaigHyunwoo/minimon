package com.minimon.controller;

import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController(value = "api")
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"Api Controller"})
public class ApiController {
    private final ApiService apiService;


    @ApiOperation(value = "API 목록 조회", response = MonApi.class, responseContainer = "List")
    @GetMapping(path = "")
    public List<MonApi> getUrls() {
        return apiService.getApiList();
    }

    @ApiOperation(value = "API 조회", response = MonApi.class)
    @GetMapping(path = "/{seq}")
    public MonApi getApi(@PathVariable("seq") int seq) {
        return apiService.getApi(seq);
    }

    @ApiOperation(value = "API 생성", response = MonApi.class)
    @PostMapping(path = "")
    public MonApi createAPi(@RequestBody MonApi monApi) {
        apiService.saveApi(monApi);
        return monApi;
    }

    @ApiOperation(value = "API 수정", response = MonApi.class)
    @PostMapping(path = "")
    public boolean updateApi(@RequestBody MonApi monApi) {
        return apiService.editApi(monApi);
    }

    @ApiOperation(value = "API 삭제", response = MonApi.class)
    @DeleteMapping(path = "/{seq}")
    public boolean delete(@PathVariable("seq") int seq) {
        return apiService.remove(seq);
    }

    @ApiOperation(value = "API 검사 테스트", response = MonApi.class)
    @PostMapping(path = "/check")
    public Map<String, Object> check(@RequestBody MonApi monApi) throws Exception {
        return apiService.executeApi(monApi);
    }

    @ApiOperation(value = "API 검사 실행", response = MonResult.class)
    @GetMapping(path = "/{seq}/execute")
    public MonResult execute(@PathVariable("seq") int seq) {
        return apiService.executeApi(seq);
    }
}