package com.minimon.scheduler;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringScheduler {
    private final ResultService resultService;
    private final UrlService urlService;
    private final ApiService apiService;
    private final TransactionService transactionService;

    public void execute() {
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

    public void check(List<Map<String, Object>> resultList) {
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
