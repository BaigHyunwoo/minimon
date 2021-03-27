package com.minimon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.vo.MonApiCheckVO;
import com.minimon.vo.MonitoringResultVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MonApiServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MonApiService monApiService;

    private MonApi getDefaultMonApi() {
        return MonApi.builder()
                .title("API 모니터링 목록 조회")
                .method(HttpMethod.GET)
                .url("http://localhost:8080/monApi")
                .timeout(5)
                .loadTime(2000)
                .errorLoadTime(3000)
                .build();
    }

    private MonResult getDefaultMonResult() {
        return MonResult.builder()
                .title("GOOGLE")
                .relationSeq(0)
                .monitoringTypeEnum(MonitoringTypeEnum.URL)
                .resultCode(MonitoringResultCodeEnum.SUCCESS)
                .status(HttpStatus.OK)
                .loadTime(2000)
                .build();
    }

    @Test
    void save() {
        MonApi monApi = getDefaultMonApi();
        assertEquals(monApi, monApiService.save(monApi));
    }

    @Test
    void getList() {
        monApiService.save(getDefaultMonApi());
        long size = monApiService.getList(new CommonSearchSpec()).getTotalElements();
        assertNotEquals(0, size);
    }

    @Test
    void get() {
        MonApi monApi = monApiService.save(getDefaultMonApi());
        MonApi selectMonApi = monApiService.get(monApi.getSeq()).get();
        assertEquals(monApi.getSeq(), selectMonApi.getSeq());
    }


    @Test
    void remove() {
        MonApi monApi = monApiService.save(getDefaultMonApi());
        monApiService.remove(monApi.getSeq());
        Optional selectMonApi = monApiService.get(monApi.getSeq());
        assertNotEquals(true, selectMonApi.isPresent());
    }

    @Test
    void executeGetMethod() {
        MonitoringResultVO result = monApiService.execute("https://www.naver.com", HttpMethod.GET.name(), null);
        assertEquals(HttpStatus.OK, result.getStatus());
    }

    @Test
    void executePostMethod() throws JsonProcessingException {
        MonitoringResultVO result = monApiService.execute("http://localhost:8080/result/receive", HttpMethod.POST.name(), objectMapper.writeValueAsString(getDefaultMonResult()));
        assertEquals(HttpStatus.OK, result.getStatus());
    }
}