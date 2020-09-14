package com.minimon.service;

import com.minimon.enums.MonErrorCode;
import com.minimon.common.CommonUtils;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.MonUrl;
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
    public void saveUrl(MonUrl monUrl) {
        monUrlRepository.save(monUrl);
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

    public Map<String, Object> checkUrls(List<MonUrl> urls) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        for (MonUrl url : urls) {
            Map<String, Object> logData = executeUrl(url.getUrl(), url.getTimeout());
            checkData.put(url.getUrl(), errorCheckUrl(url, logData));
        }
        return checkData;
    }

    public List<MonUrl> findUrl() {
        Date now = new Date();
        int hours = now.getHours();
        return monUrlRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    public boolean executeUrl(int seq) {
        Optional<MonUrl> optionalMonUrl = Optional.ofNullable(getUrl(seq));
        optionalMonUrl.ifPresent(monUrl -> {
            try {

                resultService.sendResultByProperties(
                        resultService.saveResult(
                                errorCheckUrl(monUrl,
                                        executeUrl(monUrl.getUrl(), monUrl.getTimeout()))));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return optionalMonUrl.isPresent();
    }

    public Map<String, Object> executeUrl(String url, int timeout) throws Exception {
        Map<String, Object> logData = new HashMap<String, Object>();
        EventFiringWebDriver driver = null;

        SeleniumHandler selenium = new SeleniumHandler();
        driver = selenium.setUp();

        selenium.connectUrl(url, driver, timeout);
        logData = selenium.expectionLog(
                selenium.getLog(driver),
                driver.getCurrentUrl()
        );
        logData.put("source", selenium.getSource(driver));
        log.debug(logData.toString());

        if (driver != null) driver.quit();
        return logData;
    }


    public Map<String, Object> errorCheckUrl(MonUrl url, Map<String, Object> logData) {
        Map<String, Object> checkData = new HashMap<String, Object>();

        int status = Integer.parseInt("" + logData.get("status"));
        double totalLoadTime = Double.parseDouble("" + logData.get("totalLoadTime"));
        double totalPayLoad = Double.parseDouble("" + logData.get("totalPayLoad"));
        String source = logData.get("source").toString();

        checkData.put("url", url.getUrl());
        checkData.put("seq", url.getSeq());
        checkData.put("type", "URL");
        checkData.put("title", url.getTitle());
        checkData.put("check_loadTime", totalLoadTime);
        checkData.put("result", errCheck(status, totalLoadTime, totalPayLoad, source, url));

        return checkData;
    }

    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String source, MonUrl url) {
        if (status >= 400)
            return status + " ERR";
        else if (url.getLoadTimeCheck() == 1 && totalLoadTime >= url.getErrLoadTime())
            return MonErrorCode.LOAD_TIME.getCode();
        else if (url.getPayLoadCheck() == 1 && (CommonUtils.getPerData(url.getPayLoad(), url.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtils.getPerData(url.getPayLoad(), url.getPayLoadPer(), 1)))
            return MonErrorCode.PAYLOAD.getCode();
        else if (url.getTextCheck() == 1 && source.indexOf(url.getTextCheckValue()) >= 0) {
            return MonErrorCode.TEXT.getCode();
        } else
            return MonErrorCode.SUCCESS.getCode();
    }

}
