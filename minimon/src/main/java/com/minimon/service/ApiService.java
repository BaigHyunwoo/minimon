package com.minimon.service;

import com.minimon.common.CommonUtil;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonResult;
import com.minimon.enums.HttpRequestTypeEnum;
import com.minimon.enums.MonitoringErrorCodeEnum;
import com.minimon.repository.MonApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiService {
    private final ResultService resultService;
    private final MonApiRepository monApiRepository;


    @Cacheable(value = "API", key = "'list'", unless = "#result == null")
    public List<MonApi> getApis() {
        return monApiRepository.findAll();
    }

    @Cacheable(value = "API", key = "#seq", unless = "#result == null")
    public MonApi getApi(int seq) {
        return monApiRepository.findBySeq(seq);
    }

    @CacheEvict(value = "API", key = "#monApi.seq", allEntries = true)
    public MonApi saveApi(MonApi monApi) {
        monApiRepository.save(monApi);
        return monApi;
    }

    @CacheEvict(value = "API", key = "#monApi.seq", allEntries = true)
    public boolean editApi(MonApi monApiVO) {
        Optional<MonApi> optionalMonApi = Optional.ofNullable(getApi(monApiVO.getSeq()));
        optionalMonApi.ifPresent(monUrl -> {
            monApiRepository.save(monApiVO);
        });
        return optionalMonApi.isPresent();
    }

    @CacheEvict(value = "API", key = "#monApi.seq", allEntries = true)
    public boolean remove(int seq) {
        Optional<MonApi> optionalMonApi = Optional.ofNullable(getApi(seq));
        optionalMonApi.ifPresent(monApi -> {
            monApiRepository.delete(monApi);
        });
        return optionalMonApi.isPresent();
    }

    public List<MonApi> findApi() {
        Date now = new Date();
        int hours = now.getHours();
        return monApiRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    public Map<String, Object> checkApis(List<MonApi> apis) {
        Map<String, Object> checkData = new HashMap<String, Object>();
        for (MonApi api : apis) {
            Map<String, Object> logData = executeApi(api);
            checkData.put("" + api.getSeq(), errorCheckApi(api, logData));
        }
        return checkData;
    }

    public MonResult executeApi(int seq) {
        Optional<MonApi> optionalMonApi = Optional.ofNullable(monApiRepository.findBySeq(seq));
        MonResult monResult = null;
        if (optionalMonApi.isPresent()) {
            MonApi monApi = optionalMonApi.get();
            monResult = resultService.saveResult(errorCheckApi(monApi, executeApi(monApi)));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public Map<String, Object> executeApi(String url, String method, String data) {
        return httpSending(url, method, data);
    }

    public Map<String, Object> executeApi(MonApi api) {
        return httpSending(api.getUrl(), api.getMethod(), api.getData());
    }

    public Map<String, Object> errorCheckApi(MonApi api, Map<String, Object> logData) {
        Map<String, Object> checkData = new HashMap<String, Object>();

        int status = Integer.parseInt("" + logData.get("status"));
        double loadTime = Double.parseDouble("" + logData.get("loadTime"));
        double payLoad = Double.parseDouble("" + logData.get("payLoad"));
        String response = "" + logData.get("response");


        checkData.put("url", api.getUrl());
        checkData.put("method", api.getMethod());
        checkData.put("seq", api.getSeq());
        checkData.put("title", api.getTitle());
        checkData.put("type", "API");
        checkData.put("check_loadTime", loadTime);
        checkData.put("result", errCheck(status, loadTime, payLoad, response, api));

        return checkData;
    }


    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String response, MonApi api) {
        if (status >= 400)
            return MonitoringErrorCodeEnum.UNKNOWN.getCode();
        else if (api.getLoadTimeCheck() == 1 && totalLoadTime >= api.getErrLoadTime())
            return MonitoringErrorCodeEnum.LOAD_TIME.getCode();
        else if (api.getPayLoadCheck() == 1 && (CommonUtil.getPerData(api.getPayLoad(), api.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtil.getPerData(api.getPayLoad(), api.getPayLoadPer(), 1)))
            return MonitoringErrorCodeEnum.PAYLOAD.getCode();
        else if (api.getResponseCheck() == 1 && response.equals(api.getResponse()) == false)
            return MonitoringErrorCodeEnum.RESPONSE.getCode();
        else
            return MonitoringErrorCodeEnum.SUCCESS.getCode();
    }

    public Map<String, Object> httpSending(String url, String method, String data) {
        long st = System.currentTimeMillis();
        HttpResponse response = null;
        try {
            response = HttpClients.createDefault().execute(getHttpRequest(method, url, data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long ed = System.currentTimeMillis();

        return getApiLogData(st, ed, response);
    }


    public Map<String, Object> getApiLogData(long st, long ed, HttpResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        BufferedReader reader = null;


        long loadTime = ed - st;
        long payLoad = response.getEntity().getContentLength();
        int status = response.getStatusLine().getStatusCode();

        StringBuffer responseData = new StringBuffer();
        if (status >= 200 && status < 400) {
            String inputLine = "";

            try {
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while ((inputLine = reader.readLine()) != null) {
                    responseData.append(inputLine);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            payLoad = response.getEntity().getContentLength();
            if (payLoad == -1) payLoad = CommonUtil.getByteLength(responseData.toString());
        }

        result.put("loadTime", loadTime);
        result.put("status", status);
        result.put("payLoad", payLoad);
        result.put("response", responseData.toString());

        return result;
    }

    public HttpUriRequest getHttpRequest(String method, String url, String data) {
        HttpUriRequest http = null;

        try {
            switch (HttpRequestTypeEnum.valueOf(method)) {
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
