package com.minimon.service;

import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ResultServiceTest {

    @Autowired
    private ResultService resultService;


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
    public void resultSendTest() {
        assertNotNull(resultService.sendResultByProperties(getDefaultMonResult()));
    }
}