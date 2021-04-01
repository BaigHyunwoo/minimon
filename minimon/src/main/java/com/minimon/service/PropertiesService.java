package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.exception.UndefinedDriverException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertiesService {
    private final CommonProperties commonProperties;


    public void setDriverPath(String driverPath) {
        commonProperties.killDriver();

        try {

            commonProperties.setDriverVersion(driverPath);

        } catch (IllegalStateException SE) {
            throw new UndefinedDriverException(SE);
        }

        commonProperties.setDriverPath(driverPath);
    }


    public CommonProperties setResultReceivePath(String path) {
        commonProperties.setResultReceivePath(path);
        return commonProperties;
    }

}
