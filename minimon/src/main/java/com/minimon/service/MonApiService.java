package com.minimon.service;

import com.minimon.common.CommonRestTemplate;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonApiRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonApiService {
    private final CommonRestTemplate commonRestTemplate;
    private final ResultService resultService;
    private final MonApiRepository monApiRepository;


    public List<MonApi> getApis() {
        return monApiRepository.findAll();
    }

    public MonApi getApi(int seq) {
        return monApiRepository.findById(seq).orElse(null);
    }

    @Transactional
    public MonApi saveApi(MonApi monApi) {
        monApiRepository.save(monApi);
        return monApi;
    }

    @Transactional
    public boolean editApi(MonApi monApiVO) {
        Optional<MonApi> optionalMonApi = monApiRepository.findById(monApiVO.getSeq());
        optionalMonApi.ifPresent(monUrl -> monApiRepository.save(monApiVO));
        return optionalMonApi.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonApi> optionalMonApi = monApiRepository.findById(seq);
        optionalMonApi.ifPresent(monApiRepository::delete);
        return optionalMonApi.isPresent();
    }

    public List<MonApi> findScheduledApis() {
        return monApiRepository.findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum.Y);
    }

    public List<MonResult> checkApis(List<MonApi> monApis) {
        List<MonResult> monResults = new ArrayList<>();
        monApis.forEach(monApi -> {
            MonitoringResultVO monitoringResultVO = executeApi(monApi);
            monResults.add(errorCheckApi(monApi, monitoringResultVO));
        });
        return monResults;
    }

    @Transactional
    public MonResult executeApi(int seq) {
        MonResult monResult = null;

        Optional<MonApi> optionalMonApi = monApiRepository.findById(seq);
        if (optionalMonApi.isPresent()) {
            MonApi monApi = optionalMonApi.get();
            monResult = resultService.saveResult(errorCheckApi(monApi, executeApi(monApi)));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public MonitoringResultVO executeApi(String url, String method, String data) {
        return httpSending(url, method, data);
    }

    public MonitoringResultVO executeApi(MonApi api) {
        return httpSending(api.getUrl(), api.getMethod(), api.getData());
    }

    public MonResult errorCheckApi(MonApi api, MonitoringResultVO monitoringResultVO) {
        return MonResult.builder()
                .monitoringTypeEnum(MonitoringTypeEnum.API)
                .relationSeq(api.getSeq())
                .title(api.getMethod() + " : " + api.getTitle())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .result(errCheck(
                        monitoringResultVO.getStatus(),
                        monitoringResultVO.getTotalLoadTime(),
                        monitoringResultVO.getResponse(),
                        api))
                .build();
    }

    public String errCheck(int status, double totalLoadTime, String response, MonApi api) {
        if (status >= HttpStatus.BAD_REQUEST.value())
            return MonitoringResultCodeEnum.UNKNOWN.getCode();
        else if (api.getLoadTimeCheckYn().equals(UseStatusEnum.Y) && totalLoadTime >= api.getErrorLoadTime())
            return MonitoringResultCodeEnum.LOAD_TIME.getCode();
        else if (api.getResponseCheckYn().equals(UseStatusEnum.Y) && !response.equals(api.getResponse()))
            return MonitoringResultCodeEnum.RESPONSE.getCode();
        else
            return MonitoringResultCodeEnum.SUCCESS.getCode();
    }

    public MonitoringResultVO httpSending(String url, String method, String data) {
        long st = System.currentTimeMillis();
        String response = commonRestTemplate.callApi(HttpMethod.valueOf(method), url, data);
        return MonitoringResultVO.builder()
                .status(HttpStatus.OK.value())
                .totalLoadTime(new Long(System.currentTimeMillis() - st).intValue())
                .response(response)
                .build();
    }
}
