package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.common.CommonSelenium;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonUrlRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonUrlService {
    private final ResultService resultService;
    private final MonUrlRepository monUrlRepository;
    private final CommonSelenium commonSelenium;

    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monUrlRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonUrl> get(int seq) {
        return monUrlRepository.findById(seq);
    }

    @Transactional
    public MonUrl save(MonUrl monUrl) {
        monUrlRepository.save(monUrl);
        return monUrl;
    }

    @Transactional
    public boolean edit(MonUrl monUrlVO) {
        Optional<MonUrl> optionalMonUrl = get(monUrlVO.getSeq());
        optionalMonUrl.ifPresent(monUrl -> monUrlRepository.save(monUrlVO));
        return optionalMonUrl.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonUrl> optionalMonUrl = get(seq);
        optionalMonUrl.ifPresent(monUrlRepository::delete);
        return optionalMonUrl.isPresent();
    }

    public List<MonUrl> findScheduledList() {
        return monUrlRepository.findByMonitoringUseYn(UseStatusEnum.Y);
    }

    public List<MonResult> checkList(List<MonUrl> monUrls) {
        List<MonResult> monResults = new ArrayList<>();
        monUrls.forEach(monUrl -> {
            MonitoringResultVO monitoringResultVO = execute(monUrl.getUrl(), monUrl.getTimeout());
            monResults.add(errorCheck(monUrl, monitoringResultVO));
        });
        return monResults;
    }

    @Transactional
    public MonResult execute(int seq) {
        MonResult monResult = null;

        Optional<MonUrl> optionalMonUrl = get(seq);
        if (optionalMonUrl.isPresent()) {
            MonUrl monUrl = optionalMonUrl.get();
            monResult = resultService.save(errorCheck(monUrl, execute(monUrl.getUrl(), monUrl.getTimeout())));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public MonitoringResultVO execute(String url, int timeout) {
        EventFiringWebDriver driver = commonSelenium.setUp();
        MonitoringResultVO monitoringResultVO;

        try {

            int totalLoadTime = commonSelenium.connect(url, driver, timeout);
            monitoringResultVO = commonSelenium.getResult(commonSelenium.getLog(driver), driver.getCurrentUrl(), totalLoadTime);

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return monitoringResultVO;
    }

    private MonResult errorCheck(MonUrl url, MonitoringResultVO monitoringResultVO) {
        return MonResult.builder()
                .monitoringTypeEnum(MonitoringTypeEnum.URL)
                .relationSeq(url.getSeq())
                .title(url.getTitle())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .resultCode(getResultCode(monitoringResultVO.getStatus(), monitoringResultVO.getTotalLoadTime(), url))
                .status(monitoringResultVO.getStatus())
                .build();
    }

    private MonitoringResultCodeEnum getResultCode(HttpStatus status, double totalLoadTime, MonUrl url) {
        if (status == HttpStatus.OK) {
            return MonitoringResultCodeEnum.SUCCESS;
        } else if (url.getLoadTimeCheckYn().equals(UseStatusEnum.Y) && totalLoadTime >= url.getErrorLoadTime()) {
            return MonitoringResultCodeEnum.LOAD_TIME;
        } else {
            return MonitoringResultCodeEnum.UNKNOWN;
        }
    }
}
