package com.minimon.controller;

import com.minimon.entity.MonApi;
import com.minimon.repository.MonApiRepository;
import com.minimon.service.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController(value = "api")
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;
    private final MonApiRepository monApiRepository;

    @GetMapping(path = "")
    public List<MonApi> getUrls() {
        return monApiRepository.findAll();
    }


    @PostMapping(path = "")
    public MonApi createAPi(@RequestBody MonApi monApi) {
        apiService.saveApi(monApi);
        return monApi;
    }

    @GetMapping(path = "/{seq}")
    public MonApi getApi(@PathVariable("seq") int seq) {
        return apiService.getApi(seq);
    }

    @PostMapping(path = "")
    public boolean updateApi(@RequestBody MonApi monApi) {
        return apiService.editApi(monApi);
    }

    @DeleteMapping(path = "/{seq}")
    public boolean delete(@PathVariable("seq") int seq) {
        return apiService.remove(seq);
    }

    @PostMapping(path = "/check")
    public Map<String, Object> check(@RequestBody MonApi monApi) throws Exception {
        return apiService.executeApi(monApi);
    }


    @GetMapping(path = "/{seq}/execute")
    public boolean execute(@PathVariable("seq") int seq) {
        return apiService.executeApi(seq);
    }
}