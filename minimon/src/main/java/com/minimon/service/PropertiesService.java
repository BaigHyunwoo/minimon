package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.exception.DriverUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertiesService {
    private final CommonProperties commonProperties;


    public void setDriverPath(MultipartFile driver) {
        try {

            killDriver();
            String path = new File(commonProperties.getDriverPath()).getAbsolutePath() + commonProperties.getDriverFileName();
            File file = new File(path);
            driver.transferTo(file);

            commonProperties.setDriverVersion();
        } catch (FileUploadException e) {
            throw new DriverUploadException(e);
        } catch (IOException e) {
            throw new DriverUploadException(e);
        }
    }

    private void killDriver() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setResultReceivePath(String path) {
        commonProperties.setResultReceivePath(path);
    }

}
