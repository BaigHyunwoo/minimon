package com.minimon.common;

import com.minimon.MinimonApplication;
import com.minimon.entity.MonResult;
import com.minimon.service.ApiService;
import com.minimon.service.ResultService;
import com.minimon.service.TransactionService;
import com.minimon.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ExecuteTool {
    private final ResultService resultService;
    private final UrlService urlService;
    private final ApiService apiService;
    private final TransactionService transactionService;
    private String className = this.getClass().toString();

    @Scheduled(cron = "0 0/5 * * * *")
    public void execute() throws Exception {

        if (MinimonApplication.getDriverPath().length() > 1) {
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            resultList.add(apiService.checkApis(apiService.findApi()));
            resultList.add(urlService.checkUrls(urlService.findUrl()));
            resultList.add(transactionService.checkTransactions(transactionService.findTransactionUseable()));
            check(resultList);
            log.info("Monitoring Execute Complete");
        } else {
            log.info("Please save your webDriverPath at the main page");
        }
    }

    public void check(List<Map<String, Object>> resultList) throws Exception {

        for (Map<String, Object> result : resultList) {
            for (Object value : result.values()) {
                Map<String, Object> checkLog = (Map<String, Object>) value;
                if (checkLog.get("result").equals("SUCCESS") == false) {
                    MonResult monResult = resultService.saveResult(checkLog);
                    resultService.sendResultByProperties(monResult);
                }
            }
        }

        log.debug("Monitoring check Complete");

    }
}
