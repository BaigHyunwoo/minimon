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
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"Api Controller"})
public class ApiController {
    private final ApiService apiService;


    @ApiOperation(value = "API 목록 조회", response = MonApi.class)
    @GetMapping(path = "")
    public List<MonApi> getApis() {
        return apiService.getApiList();
    }

    @ApiOperation(value = "API 조회", response = MonApi.class)
    @GetMapping(path = "/{seq}")
    public MonApi get(@PathVariable("seq") int seq) {
        return apiService.getApi(seq);
    }

    @ApiOperation(value = "API 생성", response = MonApi.class)
    @PostMapping(path = "")
    public MonApi create(@RequestBody MonApi monApi) {
        apiService.saveApi(monApi);
        return monApi;
    }

    @ApiOperation(value = "API 수정", response = MonApi.class)
    @PutMapping(path = "")
    public boolean update(@RequestBody MonApi monApi) {
        return apiService.editApi(monApi);
    }

    @ApiOperation(value = "API 삭제", response = Boolean.class)
    @DeleteMapping(path = "/{seq}")
    public boolean delete(@PathVariable("seq") int seq) {
        return apiService.remove(seq);
    }

    @ApiOperation(value = "API 검사 테스트 실행", response = Map.class)
    @PostMapping(path = "/check")
    public Map<String, Object> check(@RequestBody MonApi monApi) throws Exception {
        return apiService.executeApi(monApi);
    }

    @ApiOperation(value = "API 검사 실행", response = MonResult.class)
    @PostMapping(path = "/{seq}/execute")
    public MonResult execute(@PathVariable("seq") int seq) {
        return apiService.executeApi(seq);
    }
}