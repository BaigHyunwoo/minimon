package com.minimon.service;

import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonApiRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonApiService {
    private final CommonRestTemplate commonRestTemplate;
    private final ResultService resultService;
    private final MonApiRepository monApiRepository;


    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monApiRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonApi> get(int seq) {
        return monApiRepository.findById(seq);
    }

    @Transactional
    public MonApi save(MonApi monApi) {
        monApiRepository.save(monApi);
        return monApi;
    }

    @Transactional
    public boolean edit(MonApi monApiVO) {
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

    public List<MonApi> findScheduledList() {
        return monApiRepository.findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum.Y);
    }

    public Runnable executeTask(int seq){
        return () -> {
            MonResult monResult = execute(seq);
            resultService.save(monResult);
            resultService.sendResultByProperties(monResult);
        };
    }

    @Transactional
    public MonResult execute(int seq) {
        MonResult monResult = null;

        Optional<MonApi> optionalMonApi = monApiRepository.findById(seq);
        if (optionalMonApi.isPresent()) {
            MonApi monApi = optionalMonApi.get();
            MonitoringResultVO monitoringResultVO = check(monApi);
            log.info(monApi.getTitle()+" 실행 : "+monitoringResultVO.toString());

            monResult = resultService.save(errorCheck(monApi, monitoringResultVO));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    private MonitoringResultVO check(MonApi api) {
        return request(api.getUrl(), api.getMethod(), api.getData());
    }

    public MonitoringResultVO check(String url, String method, String data) {
        return request(url, HttpMethod.valueOf(method), data);
    }

    private MonResult errorCheck(MonApi api, MonitoringResultVO monitoringResultVO) {
        return MonResult.builder()
                .monitoringTypeEnum(MonitoringTypeEnum.API)
                .relationSeq(api.getSeq())
                .title(api.getMethod() + " : " + api.getTitle())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .resultCode(getResultCode(
                        monitoringResultVO.getStatus(),
                        monitoringResultVO.getTotalLoadTime(),
                        monitoringResultVO.getResponse(),
                        api))
                .status(monitoringResultVO.getStatus())
                .resultSendUseYn(api.getResultSendUseYn())
                .response(monitoringResultVO.getResponse())
                .build();
    }

    private MonitoringResultCodeEnum getResultCode(HttpStatus status, double totalLoadTime, Object response, MonApi api) {
        if (api.getLoadTimeCheckYn().equals(UseStatusEnum.Y) && totalLoadTime >= api.getErrorLoadTime()) {
            return MonitoringResultCodeEnum.LOAD_TIME;
        } else if (api.getResponseCheckYn().equals(UseStatusEnum.Y) && !response.equals(api.getResponse())) {
            return MonitoringResultCodeEnum.RESPONSE;
        } else if (status == HttpStatus.OK) {
            return MonitoringResultCodeEnum.SUCCESS;
        } else {
            return MonitoringResultCodeEnum.UNKNOWN;
        }
    }

    private MonitoringResultVO request(String url, HttpMethod method, String data) {
        long st = System.currentTimeMillis();
        String response = commonRestTemplate.callApi(method, url, data);
        return MonitoringResultVO.builder()
                .url(url)
                .status(HttpStatus.OK)
                .totalLoadTime(Long.valueOf(System.currentTimeMillis() - st).intValue())
                .response(response)
                .build();
    }
}
