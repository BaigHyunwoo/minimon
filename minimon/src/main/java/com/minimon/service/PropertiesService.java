package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertiesService {
    private final CommonProperties commonProperties;


    public void setDriverPath(MultipartFile driver){
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver");
            String path = new File(commonProperties.getDriverPath()).getAbsolutePath() + commonProperties.getDriverFileName();
            File file = new File(path);
            driver.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResultReceivePath(String path){
        commonProperties.setResultReceivePath(path);
    }

}
