package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final CommonRestTemplate commonRestTemplate;
    private final MonResultRepository monResultRepository;
    private final CommonProperties commonProperties;

    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monResultRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonResult> get(int seq) {
        return monResultRepository.findById(seq);
    }

    @Transactional
    public MonResult save(MonResult monResult) {
        monResultRepository.save(monResult);
        return monResult;
    }

    public void sendResultByProperties(MonResult monResult) {
        try {

            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(commonProperties.getLocation());
            properties.load(new java.io.BufferedInputStream(fis));
            String location = properties.getProperty("location");

            commonRestTemplate.callApi(HttpMethod.POST, location, monResult);
        } catch (Exception e) {
            log.info("SEND RESULT ERROR");
        }
    }

    private String getResultText(MonResult monResult) {
        return "\n" + monResult.getRegDate() + " " +
                "\n" + monResult.getMonitoringType().getValue() + " : " + monResult.getTitle() + " " +
                "\nRESULT : " + monResult.getResultCode().getValue() + " ";
    }
}
