package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonUrl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MonApiServiceTest {

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
    }

    @Test
    void execute() {
    }
}