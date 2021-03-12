package com.minimon.scheduler;

import com.minimon.entity.MonResult;
import com.minimon.service.MonApiService;
import com.minimon.service.MonTransactionService;
import com.minimon.service.ResultService;
import com.minimon.service.MonUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringScheduler {
    private final ResultService resultService;
    private final MonUrlService monUrlService;
    private final MonApiService monApiService;
    private final MonTransactionService monTransactionService;

    public void urlMonitoring() {
        List<MonResult> monResults = new ArrayList();
        monResults.addAll(monUrlService.checkList(monUrlService.findScheduledUrls()));
        check(monResults);
        log.info("URL Monitoring Execute Complete");
    }

    public void apiMonitoring() {
        List<MonResult> monResults = new ArrayList();
        monResults.addAll(monApiService.checkList(monApiService.findScheduledApis()));
        check(monResults);
        log.info("API Monitoring Execute Complete");
    }

    public void transactionMonitoring() {
        List<MonResult> monResults = new ArrayList();
        monResults.addAll(monTransactionService.checkList(monTransactionService.findScheduledList()));
        check(monResults);
        log.info("ACT Monitoring Execute Complete");
    }

    @Transactional
    public void check(List<MonResult> monResults) {
        monResults.forEach(monResult -> {
            switch (monResult.getResultCode()) {
                case SUCCESS:
                    break;
                default:
                    resultService.save(monResult);
                    resultService.sendResultByProperties(monResult);
                    break;
            }
        });
        log.debug("Monitoring check Complete");
    }
}
