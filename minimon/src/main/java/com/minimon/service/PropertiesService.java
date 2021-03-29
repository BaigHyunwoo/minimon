package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.exception.DriverUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertiesService {
    private final CommonProperties commonProperties;


    public void setDriverPath(String driverPath) {
        try {

            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            commonProperties.setDriverPath(driverPath);
            commonProperties.setDriverVersion();

        } catch (IOException e) {
            throw new DriverUploadException(e);
        }
    }


    public void setResultReceivePath(String path) {
        commonProperties.setResultReceivePath(path);
    }

}
