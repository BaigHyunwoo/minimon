package com.minimon.service;

import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonUtil;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.MonTypeEnum;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.UseStatusEnum;
import com.minimon.repository.MonApiRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
                .monTypeEnum(MonTypeEnum.API)
                .relationSeq(api.getSeq())
                .title(api.getMethod() + " : " + api.getTitle())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .payload(monitoringResultVO.getTotalPayLoad())
                .result(errCheck(
                        monitoringResultVO.getStatus(),
                        monitoringResultVO.getTotalLoadTime(),
                        monitoringResultVO.getTotalPayLoad(),
                        monitoringResultVO.getResponse(),
                        api))
                .build();
    }


    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String response, MonApi api) {
        if (status >= 400)
            return MonitoringResultCodeEnum.UNKNOWN.getCode();
        else if (api.getLoadTimeCheck() == 1 && totalLoadTime >= api.getErrLoadTime())
            return MonitoringResultCodeEnum.LOAD_TIME.getCode();
        else if (api.getPayLoadCheck() == 1 && (CommonUtil.getPerData(api.getPayLoad(), api.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtil.getPerData(api.getPayLoad(), api.getPayLoadPer(), 1)))
            return MonitoringResultCodeEnum.PAYLOAD.getCode();
        else if (api.getResponseCheck() == 1 && response.equals(api.getResponse()) == false)
            return MonitoringResultCodeEnum.RESPONSE.getCode();
        else
            return MonitoringResultCodeEnum.SUCCESS.getCode();
    }

    public MonitoringResultVO httpSending(String url, String method, String data) {
//        long st = System.currentTimeMillis();
//        HttpResponse response = null;
//        try {
//            response = HttpClients.createDefault().execute(getHttpRequest(method, url, data));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        long ed = System.currentTimeMillis();
//        return getApiLogData(st, ed, response);
        return MonitoringResultVO.builder().status(200).response(commonRestTemplate.callApi(HttpMethod.valueOf(method), url)).build();
    }


    public MonitoringResultVO getApiLogData(long st, long ed, HttpResponse response) {
        long loadTime = ed - st;
        long payLoad = response.getEntity().getContentLength();
        int status = response.getStatusLine().getStatusCode();

        StringBuffer responseData = new StringBuffer();
        if (status >= 200 && status < 400) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String inputLine = "";
                while ((inputLine = reader.readLine()) != null) {
                    responseData.append(inputLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            payLoad = response.getEntity().getContentLength();
            if (payLoad == -1) payLoad = CommonUtil.getByteLength(responseData.toString());
        }

        return MonitoringResultVO.builder()
                .totalLoadTime(new Long(loadTime).intValue())
                .totalPayLoad(new Long(payLoad).intValue())
                .status(status)
                .response(responseData.toString())
                .build();
    }

    public HttpUriRequest getHttpRequest(String method, String url, String data) {
        HttpUriRequest http = null;

        try {
            switch (HttpMethod.valueOf(method)) {
                case GET:
                    HttpGet httpget = new HttpGet(url);
                    http = httpget;
                    break;
                case POST:
                    HttpPost httppost = new HttpPost(url);
                    httppost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
                    http = httppost;
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
                    http = httpPut;
                    break;
                case DELETE:
                    HttpDelete httpDelete = new HttpDelete(url);
                    http = httpDelete;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return http;
    }
}
