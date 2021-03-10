package com.minimon.service;

import com.minimon.common.CommonRestTemplate;
import com.minimon.entity.MonResult;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final CommonRestTemplate commonRestTemplate;
    private final MonResultRepository monResultRepository;

    @Value("${common.location}")
    private String location;


    @Transactional
    public MonResult save(MonResult monResult) {
        monResultRepository.save(monResult);
        return monResult;
    }

    public void sendResultByProperties(MonResult monResult) {

        try {

            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(location);
            properties.load(new java.io.BufferedInputStream(fis));
            String location = properties.getProperty("location");
            String text = getResultText(monResult);

            commonRestTemplate.callApi(HttpMethod.GET, location, text);

            log.info("SEND API : " + location + "  Body : " + text);
        } catch (Exception e) {
            log.info("SEND RESULT ERROR");
        }
    }

    public String getResultText(MonResult monResult) {
        return "\n" + monResult.getRegDate() + " " +
                "\n" + monResult.getMonitoringType().getValue() + " : " + monResult.getTitle() + " " +
                "\nRESULT : " + monResult.getResultCode().getValue() + " ";
    }
}
