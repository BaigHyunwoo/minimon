package com.minimon.service;

import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.exception.UndefinedResultReceiveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
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
                .resultSendUseYn(UseStatusEnum.Y)
                .build();
    }

    @Test
    public void resultSendErrorTest() {
        assertThrows(UndefinedResultReceiveException.class, () -> resultService.sendResult(getDefaultMonResult()));
    }

    @Test
    public void resultSendIgnoreTest() {
        MonResult monResult = getDefaultMonResult();
        monResult.setResultSendUseYn(UseStatusEnum.N);
        assertNull(resultService.sendResult(monResult));
    }
}