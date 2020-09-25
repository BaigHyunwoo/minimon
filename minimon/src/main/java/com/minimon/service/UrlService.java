package com.minimon.service;

import com.minimon.common.CommonUtil;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonTypeEnum;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.repository.MonUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final ResultService resultService;
    private final MonUrlRepository monUrlRepository;

    public List<MonUrl> getUrlList() {
        return monUrlRepository.findAll();
    }

    @Cacheable(value = "URL", key = "#seq", unless = "#result == null")
    public MonUrl getUrl(int seq) {
        return monUrlRepository.findBySeq(seq);
    }

    @Cacheable(value = "URL", unless = "#result == null")
    public List<MonUrl> getMonUrls() {
        return monUrlRepository.findAll();
    }

    @CacheEvict(value = "URL", key = "#monUrl.seq")
    public MonUrl saveUrl(MonUrl monUrl) {
        monUrlRepository.save(monUrl);
        return monUrl;
    }

    @CacheEvict(value = "URL", key = "#monUrlVO.seq")
    public boolean editUrl(MonUrl monUrlVO) {
        Optional<MonUrl> optionalMonUrl = Optional.ofNullable(getUrl(monUrlVO.getSeq()));
        optionalMonUrl.ifPresent(monUrl -> {
            monUrlRepository.save(monUrlVO);
        });
        return optionalMonUrl.isPresent();
    }

    @CacheEvict(value = "URL", key = "#seq")
    public boolean remove(int seq) {
        Optional<MonUrl> optionalMonUrl = Optional.ofNullable(getUrl(seq));
        optionalMonUrl.ifPresent(monUrl -> {
            monUrlRepository.delete(monUrl);
        });
        return optionalMonUrl.isPresent();
    }

    public List<MonResult> checkUrls(List<MonUrl> monUrls) {
        List<MonResult> monResults = new ArrayList<>();
        monUrls.forEach(monUrl -> {
            Map<String, Object> logData = executeUrl(monUrl.getUrl(), monUrl.getTimeout());
            monResults.add(errorCheckUrl(monUrl, logData));
        });
        return monResults;
    }

    public List<MonUrl> findScheduledUrls() {
        Date now = new Date();
        int hours = now.getHours();
        return monUrlRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    public MonResult executeUrl(int seq) {
        Optional<MonUrl> optionalMonUrl = Optional.ofNullable(getUrl(seq));
        MonResult monResult = null;
        if (optionalMonUrl.isPresent()) {
            MonUrl monUrl = optionalMonUrl.get();
            monResult = resultService.saveResult(errorCheckUrl(monUrl, executeUrl(monUrl.getUrl(), monUrl.getTimeout())));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public Map<String, Object> executeUrl(String url, int timeout) {
        SeleniumHandler selenium = new SeleniumHandler();
        EventFiringWebDriver driver = selenium.setUp();

        selenium.connectUrl(url, driver, timeout);
        Map<String, Object> logData = selenium.getResult(selenium.getLog(driver), driver.getCurrentUrl());
        logData.put("source", selenium.getSource(driver));
        log.debug(logData.toString());

        if (driver != null) driver.quit();
        return logData;
    }


    public MonResult errorCheckUrl(MonUrl url, Map<String, Object> logData) {
        int status = Integer.parseInt("" + logData.get("status"));
        double totalLoadTime = Double.parseDouble("" + logData.get("totalLoadTime"));
        double totalPayLoad = Double.parseDouble("" + logData.get("totalPayLoad"));
        String source = logData.get("source").toString();

        return MonResult.builder()
                .monTypeEnum(MonTypeEnum.URL)
                .relationSeq(url.getSeq())
                .title(url.getTitle())
                .loadTime(totalLoadTime)
                .payload(totalPayLoad)
                .result(errCheck(status, totalLoadTime, totalPayLoad, source, url))
                .build();
    }

    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String source, MonUrl url) {
        if (status >= 400) {
            return MonitoringResultCodeEnum.UNKNOWN.getCode();
        } else if (url.getLoadTimeCheck() == 1 && totalLoadTime >= url.getErrLoadTime()) {
            return MonitoringResultCodeEnum.LOAD_TIME.getCode();
        } else if (url.getPayLoadCheck() == 1 && (CommonUtil.getPerData(url.getPayLoad(), url.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtil.getPerData(url.getPayLoad(), url.getPayLoadPer(), 1))) {
            return MonitoringResultCodeEnum.PAYLOAD.getCode();
        } else if (url.getTextCheck() == 1 && source.indexOf(url.getTextCheckValue()) >= 0) {
            return MonitoringResultCodeEnum.TEXT.getCode();
        } else
            return MonitoringResultCodeEnum.SUCCESS.getCode();
    }

}
