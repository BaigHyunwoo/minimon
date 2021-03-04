package com.minimon.service;

import com.minimon.common.CommonSender;
import com.minimon.common.CommonProperties;
import com.minimon.entity.MonResult;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final MonResultRepository monResultRepository;

    @Value("${common.location}")
    private String location;

    public MonResult saveResult(Map<String, Object> result) {
        MonResult monResult = new MonResult();
        monResult.setTitle("" + result.get("title"));
        monResult.setResult("" + result.get("result"));
        monResult.setLoadTime(Double.parseDouble("" + result.get("check_loadTime")));
        monResult.setResponse(result.toString());
        monResultRepository.save(monResult);
        return monResult;
    }

    public MonResult saveResult(MonResult monResult) {
        monResultRepository.save(monResult);
        return monResult;
    }

    public void sendResultByProperties(MonResult monResult) {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(location + "/location.properties");
            properties.load(new java.io.BufferedInputStream(fis));
            String location = properties.getProperty("location");
            String text = getResultText(monResult);
            CommonSender commonSender = new CommonSender();
            commonSender.sendingMassage(location, text);
            log.info("SEND API : " + location + "  Body : " + text);
        } catch (Exception e) {
            log.info("SEND RESULT ERROR");
        }
    }

    public String getResultText(MonResult monResult) {
        return new StringBuffer()
                .append("\n" + monResult.getRegDate() + " ")
                .append("\n" + monResult.getRelationType() + " : " + monResult.getTitle() + " ")
                .append("\nRESULT : " + monResult.getResult() + " ")
                .toString();
    }
}
