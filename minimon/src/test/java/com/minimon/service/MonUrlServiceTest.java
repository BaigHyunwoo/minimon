package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.exception.UndefinedResultReceiveException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MonUrlServiceTest {

    @Autowired
    private MonUrlService monUrlService;

    private MonUrl getDefaultMonUrl() {
        return MonUrl.builder()
                .url("https://www.naver.com")
                .title("네이버")
                .errorLoadTime(3000)
                .timeout(5)
                .loadTime(2000)
                .build();
    }

    @Test
    void save() {
        MonUrl monUrl = getDefaultMonUrl();
        assertEquals(monUrl, monUrlService.save(monUrl));
    }

    @Test
    void getList() {
        monUrlService.save(getDefaultMonUrl());
        long size = monUrlService.getList(new CommonSearchSpec()).getTotalElements();
        assertNotEquals(0, size);
    }

    @Test
    void get() {
        MonUrl monUrl = monUrlService.save(getDefaultMonUrl());
        MonUrl selectMonUrl = monUrlService.get(monUrl.getSeq()).get();
        assertEquals(monUrl.getSeq(), selectMonUrl.getSeq());
    }

    @Test
    void remove() {
        MonUrl monUrl = monUrlService.save(getDefaultMonUrl());
        monUrlService.remove(monUrl.getSeq());
        Optional selectMonUrl = monUrlService.get(monUrl.getSeq());
        assertNotEquals(true, selectMonUrl.isPresent());
    }

    @Test
    void executeSuccess() {
        MonUrl monUrl = monUrlService.save(getDefaultMonUrl());
        MonResult monResult = monUrlService.execute(monUrl.getSeq());
        assertEquals(MonitoringResultCodeEnum.SUCCESS, monResult.getResultCode());
    }

    @Test
    void executeSuccessUncheckedLoadTime() {
        MonUrl monUrl = getDefaultMonUrl();
        monUrl.setErrorLoadTime(10);
        monUrl.setLoadTimeCheckYn(UseStatusEnum.N);
        monUrl = monUrlService.save(monUrl);
        MonResult monResult = monUrlService.execute(monUrl.getSeq());
        assertEquals(MonitoringResultCodeEnum.SUCCESS, monResult.getResultCode());
    }

    @Test
    void executeFail() {
        MonUrl monUrl = getDefaultMonUrl();
        monUrl.setErrorLoadTime(1000);
        monUrl = monUrlService.save(monUrl);
        MonResult monResult = monUrlService.execute(monUrl.getSeq());
        assertNotEquals(MonitoringResultCodeEnum.SUCCESS, monResult.getResultCode());
    }

    @Test
    void sendResultFail() {
        assertThrows(UndefinedResultReceiveException.class, () -> {
            MonUrl monUrl = getDefaultMonUrl();
            monUrl.setResultSendUseYn(UseStatusEnum.Y);
            monUrl = monUrlService.save(monUrl);
            monUrlService.execute(monUrl.getSeq());
        });
    }

    @Test
    void check() {
        assertEquals(HttpStatus.OK, monUrlService.check("https://www.daum.net", 3).getStatus());
        assertEquals(HttpStatus.OK, monUrlService.check("https://www.naver.com", 1).getStatus());
    }

    @Test
    void checkFail() {
        assertNotEquals(HttpStatus.OK, monUrlService.check("http://localhost:8080/result", 3).getStatus());
    }
}