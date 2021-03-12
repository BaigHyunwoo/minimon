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


    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monApiRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public MonApi get(int seq) {
        return monApiRepository.findById(seq).orElse(null);
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

    public List<MonResult> checkList(List<MonApi> monApis) {
        List<MonResult> monResults = new ArrayList<>();
        monApis.forEach(monApi -> {
            MonitoringResultVO monitoringResultVO = execute(monApi);
            monResults.add(errorCheck(monApi, monitoringResultVO));
        });
        return monResults;
    }

    @Transactional
    public MonResult execute(int seq) {
        MonResult monResult = null;

        Optional<MonApi> optionalMonApi = monApiRepository.findById(seq);
        if (optionalMonApi.isPresent()) {
            MonApi monApi = optionalMonApi.get();
            monResult = resultService.save(errorCheck(monApi, execute(monApi)));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public MonitoringResultVO execute(String url, String method, String data) {
        return request(url, method, data);
    }

    public MonitoringResultVO execute(MonApi api) {
        return request(api.getUrl(), api.getMethod(), api.getData());
    }

    public MonResult errorCheck(MonApi api, MonitoringResultVO monitoringResultVO) {
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
                .build();
    }

    public MonitoringResultCodeEnum getResultCode(HttpStatus status, double totalLoadTime, Object response, MonApi api) {
        if (status == HttpStatus.OK)
            return MonitoringResultCodeEnum.SUCCESS;
        else if (api.getLoadTimeCheckYn().equals(UseStatusEnum.Y) && totalLoadTime >= api.getErrorLoadTime())
            return MonitoringResultCodeEnum.LOAD_TIME;
        else if (api.getResponseCheckYn().equals(UseStatusEnum.Y) && !response.equals(api.getResponse()))
            return MonitoringResultCodeEnum.RESPONSE;
        else
            return MonitoringResultCodeEnum.UNKNOWN;
    }

    public MonitoringResultVO request(String url, String method, String data) {
        long st = System.currentTimeMillis();
        String response = commonRestTemplate.callApi(HttpMethod.valueOf(method), url, data);
        return MonitoringResultVO.builder()
                .status(HttpStatus.OK)
                .totalLoadTime(new Long(System.currentTimeMillis() - st).intValue())
                .response(response)
                .build();
    }
}
