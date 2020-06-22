package com.minimon.common;

import com.minimon.MinimonApplication;
import com.minimon.controller.MainController;
import com.minimon.entity.TblMonResult;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonApiRepository;
import com.minimon.repository.TblMonTransactionRepository;
import com.minimon.repository.TblMonUrlRepository;
import com.minimon.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 모니터링 동작 관리
 *
 * @author 백현우
 */
@RestController
@EnableScheduling
public class ExecuteTool {

    @Autowired
    TblMonUrlRepository tblMonUrlRepository;

    @Autowired
    TblMonApiRepository tblMonApiRepository;

    @Autowired
    TblMonTransactionRepository tblMonTransactionRepository;

    @Autowired
    ResultService resultService;

    @Autowired
    UrlService urlService;

    @Autowired
    ApiService apiService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    EmailService emailService;

    @Autowired
    SmsService smsService;

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    private String className = this.getClass().toString();


    /**
     * 모니터링 실행
     */
    @Scheduled(cron = "0 0/5 * * * *")
    public void execute() throws Exception {

        try {
            if (MinimonApplication.getDriverPath().length() > 1) {
                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                resultList.add(apiService.checkApis(apiService.findApi()));
                resultList.add(urlService.checkUrls(urlService.findUrl()));
                resultList.add(transactionService.checkTransactions(transactionService.findTransactionUseable()));
                check(resultList);
                logger.info("Monitoring Execute Complete");
            } else {
                logger.info("Please save your webDriverPath at the main page");
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

    }


    /**
     * 모니터링 실행 결과 전송
     */
    public void check(List<Map<String, Object>> resultList) throws Exception {

        try {
            for (Map<String, Object> result : resultList) {
                for (Object value : result.values()) {
                    Map<String, Object> checkLog = (Map<String, Object>) value;
                    if (checkLog.get("result").equals("SUCCESS") == false) {
                        TblMonResult tblMonResult = resultService.saveResult(checkLog);
                        resultService.sendResultByProperties(tblMonResult);
                    }
                }
            }

            logger.debug("Monitoring check Complete");

        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - check", className, 12);

        }

    }


}
