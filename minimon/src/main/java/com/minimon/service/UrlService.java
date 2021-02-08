package com.minimon.service;

import com.minimon.common.CommonUtil;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonTypeEnum;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonUrlRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final ResultService resultService;
    private final MonUrlRepository monUrlRepository;

    public List<MonUrl> getUrlList() {
        return monUrlRepository.findAll();
    }

    public Optional<MonUrl> getUrl(int seq) {
        return monUrlRepository.findById(seq);
    }

    @Transactional
    public MonUrl saveUrl(MonUrl monUrl) {
        monUrlRepository.save(monUrl);
        return monUrl;
    }

    @Transactional
    public boolean editUrl(MonUrl monUrlVO) {
        Optional<MonUrl> optionalMonUrl = getUrl(monUrlVO.getSeq());
        optionalMonUrl.ifPresent(monUrl -> {
            monUrlRepository.save(monUrlVO);
        });
        return optionalMonUrl.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonUrl> optionalMonUrl = getUrl(seq);
        optionalMonUrl.ifPresent(monUrl -> {
            monUrlRepository.delete(monUrl);
        });
        return optionalMonUrl.isPresent();
    }

    public List<MonUrl> findScheduledUrls() {
        return monUrlRepository.findByMonitoringUseYn(UseStatusEnum.USE.getCode());
    }

    public List<MonResult> checkUrls(List<MonUrl> monUrls) {
        List<MonResult> monResults = new ArrayList<>();
        monUrls.forEach(monUrl -> {
            MonitoringResultVO monitoringResultVO = executeUrl(monUrl.getUrl(), monUrl.getTimeout());
            monResults.add(errorCheck(monUrl, monitoringResultVO));
        });
        return monResults;
    }

    @Transactional
    public MonResult executeUrl(int seq) {
        MonResult monResult = null;

        Optional<MonUrl> optionalMonUrl = getUrl(seq);
        if (optionalMonUrl.isPresent()) {
            MonUrl monUrl = optionalMonUrl.get();
            monResult = resultService.saveResult(errorCheck(monUrl, executeUrl(monUrl.getUrl(), monUrl.getTimeout())));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public MonitoringResultVO executeUrl(String url, int timeout) {
        SeleniumHandler selenium = new SeleniumHandler();
        EventFiringWebDriver driver = selenium.setUp();
        MonitoringResultVO monitoringResultVO;
        try {
            int totalLoadTime = selenium.connect(url, driver, timeout);
            monitoringResultVO = selenium.getResult(selenium.getLog(driver), driver.getCurrentUrl(), totalLoadTime);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return monitoringResultVO;
    }


    public MonResult errorCheck(MonUrl url, MonitoringResultVO monitoringResultVO) {
        return MonResult.builder()
                .monTypeEnum(MonTypeEnum.URL)
                .relationSeq(url.getSeq())
                .title(url.getTitle())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .payload(monitoringResultVO.getTotalPayLoad())
                .result(getResultCode(monitoringResultVO.getStatus(), monitoringResultVO.getTotalLoadTime(), monitoringResultVO.getTotalPayLoad(), url))
                .build();
    }

    public String getResultCode(int status, double totalLoadTime, double totalPayLoad, MonUrl url) {
        if (status >= 400) {
            return MonitoringResultCodeEnum.UNKNOWN.getCode();
        } else if (url.getLoadTimeCheck() == 1 && totalLoadTime >= url.getErrLoadTime()) {
            return MonitoringResultCodeEnum.LOAD_TIME.getCode();
        } else if (url.getPayLoadCheck() == 1 && (CommonUtil.getPerData(url.getPayLoad(), url.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtil.getPerData(url.getPayLoad(), url.getPayLoadPer(), 1))) {
            return MonitoringResultCodeEnum.PAYLOAD.getCode();
        } else {
            return MonitoringResultCodeEnum.SUCCESS.getCode();
        }
    }

}
