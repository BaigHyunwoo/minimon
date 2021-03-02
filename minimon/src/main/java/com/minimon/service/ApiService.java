package com.minimon.service;

import com.minimon.common.CommonRestTemplate;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonApiRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final CommonRestTemplate commonRestTemplate;
    private final ResultService resultService;
    private final MonApiRepository monApiRepository;


    public List<MonApi> getApis() {
        return monApiRepository.findAll();
    }

    public Optional<MonApi> getApi(int seq) {
        return monApiRepository.findById(seq);
    }

    @Transactional
    public MonApi saveApi(MonApi monApi) {
        monApiRepository.save(monApi);
        return monApi;
    }

    @Transactional
    public boolean editApi(MonApi monApiVO) {
        Optional<MonApi> optionalMonApi = getApi(monApiVO.getSeq());
        optionalMonApi.ifPresent(monUrl -> {
            monApiRepository.save(monApiVO);
        });
        return optionalMonApi.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonApi> optionalMonApi = getApi(seq);
        optionalMonApi.ifPresent(monApi -> {
            monApiRepository.delete(monApi);
        });
        return optionalMonApi.isPresent();
    }

    public List<MonApi> findScheduledApis() {
        return monApiRepository.findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum.USE.getCode());
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

        Optional<MonApi> optionalMonApi = getApi(seq);
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
        if (status >= 400)
            return MonitoringResultCodeEnum.UNKNOWN.getCode();
        else if (api.getLoadTimeCheckYn().equals(UseStatusEnum.USE.getCode()) && totalLoadTime >= api.getErrLoadTime())
            return MonitoringResultCodeEnum.LOAD_TIME.getCode();
        else if (api.getResponseCheckYn().equals(UseStatusEnum.USE.getCode()) && response.equals(api.getResponse()) == false)
            return MonitoringResultCodeEnum.RESPONSE.getCode();
        else
            return MonitoringResultCodeEnum.SUCCESS.getCode();
    }

    public MonitoringResultVO httpSending(String url, String method, String data) {
        long st = System.currentTimeMillis();
        String response = commonRestTemplate.callApi(HttpMethod.valueOf(method), url, data);
        long loadTime = System.currentTimeMillis() - st;
        return MonitoringResultVO.builder()
                .status(200)
                .totalLoadTime(new Long(loadTime).intValue())
                .response(response)
                .build();
    }
}
