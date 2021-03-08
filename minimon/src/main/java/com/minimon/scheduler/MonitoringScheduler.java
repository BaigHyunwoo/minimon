package com.minimon.scheduler;

import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.service.ApiService;
import com.minimon.service.ResultService;
import com.minimon.service.UrlService;
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
    private final UrlService urlService;
    private final ApiService apiService;

    public void urlMonitoring() {
        List<MonResult> monResults = new ArrayList();
        monResults.addAll(urlService.checkUrls(urlService.findScheduledUrls()));
        check(monResults);
        log.info("URL Monitoring Execute Complete");
    }

    public void apiMonitoring() {
        List<MonResult> monResults = new ArrayList();
        monResults.addAll(apiService.checkApis(apiService.findScheduledApis()));
        check(monResults);
        log.info("API Monitoring Execute Complete");
    }

    @Transactional
    public void check(List<MonResult> monResults) {
        monResults.forEach(monResult -> {
            switch (MonitoringResultCodeEnum.valueOf(monResult.getResult())) {
                case SUCCESS:
                    break;
                default:
                    resultService.saveResult(monResult);
                    resultService.sendResultByProperties(monResult);
                    break;
            }
        });
        log.debug("Monitoring check Complete");
    }
}
