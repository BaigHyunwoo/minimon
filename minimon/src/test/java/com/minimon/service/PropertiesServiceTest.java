package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.exception.UndefinedDriverException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PropertiesServiceTest {

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private CommonProperties commonProperties;

    @Test
    void undefinedDriverPath() {
        assertThrows(UndefinedDriverException.class, () -> propertiesService.setDriverPath("C:\\tt"));
    }

    @Test
    void setResultReceivePath() {
        String path = "http://localhost:8080";
        assertNotNull(commonProperties.getResultReceivePath(), path);
        propertiesService.setResultReceivePath(path);
        assertEquals(commonProperties.getResultReceivePath(), path);
    }
}