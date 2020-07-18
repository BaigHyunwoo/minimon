package com.minimon.service;

import com.minimon.MinimonApplication;
import com.minimon.common.SendingHttp;
import com.minimon.entity.MonResult;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final MonResultRepository monResultRepository;

    private String className = this.getClass().toString();

    public MonResult saveResult(Map<String, Object> result) throws Exception {

        MonResult monResult = new MonResult();

        try {

            monResult.setMon_seq(Integer.parseInt("" + result.get("seq")));
            monResult.setTitle("" + result.get("title"));
            monResult.setResult("" + result.get("result"));
            monResult.setType("" + result.get("type"));
            monResult.setRegDate(new Date());
            monResult.setLoadTime(Double.parseDouble("" + result.get("check_loadTime")));
            monResult.setResponse(result.toString());
            monResultRepository.save(monResult);

        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - saveResult", className, 11);

        }

        return monResult;

    }

    public void sendResultByProperties(MonResult monResult) throws Exception {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(MinimonApplication.getDriverPath() + "/users.properties");
        properties.load(new java.io.BufferedInputStream(fis));
        String users = properties.getProperty("users");
        String destination = properties.getProperty("destination");
        String text = new StringBuffer()
                .append("\n" + monResult.getRegDate() + " ")
                .append("\n" + monResult.getType() + " : " + monResult.getTitle() + " ")
                .append("\nRESULT : " + monResult.getResult() + " ")
                .toString();
        for (String user : users.split(",")) {
            SendingHttp sendingHttp = new SendingHttp();
            sendingHttp.sendingMassage(destination, text, user);
            log.info("SEND API : " + user + "  Body : " + text);
        }

    }

}
