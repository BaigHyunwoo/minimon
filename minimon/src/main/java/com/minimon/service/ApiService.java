package com.minimon.service;

import com.minimon.common.CommonUtils;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonApiParam;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.repository.MonApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiService {
    private final ResultService resultService;
    private final MonApiRepository monApiRepository;

    public List<MonApi> getApiList() {
        return monApiRepository.findAll();
    }

    public MonApi getApi(int seq) {
        return monApiRepository.findBySeq(seq);
    }

    @Cacheable(value = "list", key = "'api'")
    public List<MonApi> getApis() {
        return monApiRepository.findAll();
    }

    @CacheEvict(value = "list", key = "'api'")
    public void saveApi(MonApi monApi) {
        monApiRepository.save(monApi);
    }

    @CacheEvict(value = "list", key = "'api'")
    public boolean editApi(MonApi monApiVO) {
        Optional<MonApi> optionalMonApi = Optional.ofNullable(getApi(monApiVO.getSeq()));
        optionalMonApi.ifPresent(monUrl -> {
            monApiRepository.save(monApiVO);
        });
        return optionalMonApi.isPresent();
    }

    @CacheEvict(value = "list", key = "'api'")
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

    public Map<String, Object> checkApis(List<MonApi> apis) throws Exception {
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
            try {
                MonApi monApi = optionalMonApi.get();
                monResult = resultService.saveResult(errorCheckApi(monApi, executeApi(monApi)));
                resultService.sendResultByProperties(monResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monResult;
    }

    public Map<String, Object> executeApi(MonApi api) throws Exception {
        return httpSending(api);
    }

    public Map<String, Object> errorCheckApi(MonApi api, Map<String, Object> logData) throws Exception {
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
            return status + " ERR";
        else if (api.getLoadTimeCheck() == 1 && totalLoadTime >= api.getErrLoadTime())
            return "LOAD TIME ERR";
        else if (api.getPayLoadCheck() == 1 && (CommonUtils.getPerData(api.getPayLoad(), api.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtils.getPerData(api.getPayLoad(), api.getPayLoadPer(), 1)))
            return "PAYLOAD ERR";
        else if (api.getResponseCheck() == 1 && response.equals(api.getResponse()) == false)
            return "RESPONSE ERR";
        else
            return "SUCCESS";
    }

    public Map<String, Object> httpSending(MonApi api) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();


        HttpClient httpclient = HttpClients.createDefault();

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        for (MonApiParam monApiParam : api.getApiParams()) {
            params.add(new BasicNameValuePair(monApiParam.getParam_key(), monApiParam.getParam_value()));
        }

        long st = System.currentTimeMillis();
        HttpResponse response = httpclient.execute(httpRequest(api.getMethod(), api.getUrl(), params));
        long ed = System.currentTimeMillis();

        result = getApiLogData(st, ed, response);

        return result;
    }


    public Map<String, Object> getApiLogData(long st, long ed, HttpResponse response) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        BufferedReader reader = null;


        long loadTime = ed - st;
        long payLoad = response.getEntity().getContentLength();
        int status = response.getStatusLine().getStatusCode();

        StringBuffer responseData = new StringBuffer();
        if (status >= 200 && status < 400) {
            String inputLine = "";

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            while ((inputLine = reader.readLine()) != null) {
                responseData.append(inputLine);
            }

            reader.close();

            payLoad = response.getEntity().getContentLength();
            if (payLoad == -1) payLoad = CommonUtils.getByteLength(responseData.toString());
        }

        result.put("loadTime", loadTime);
        result.put("status", status);
        result.put("payLoad", payLoad);
        result.put("response", responseData.toString());

        return result;
    }


    public HttpUriRequest httpRequest(String method, String url, List<NameValuePair> params) throws Exception {
        HttpUriRequest http = null;


        if (method.equals("GET") == true) {

            HttpGet httpget = new HttpGet(url);

            http = httpget;
        } else if (method.equals("POST") == true) {

            HttpPost httppost = new HttpPost(url);

            httppost.setEntity(new UrlEncodedFormEntity(params));

            http = httppost;

        } else if (method.equals("PUT") == true) {

            HttpPut httpput = new HttpPut(url);

            httpput.setEntity(new UrlEncodedFormEntity(params));

            http = httpput;

        } else if (method.equals("DELETE") == true) {

            HttpDelete httpdelete = new HttpDelete(url);

            http = httpdelete;

        } else if (method.equals("PATCH") == true) {

            HttpPatch httppatch = new HttpPatch(url);

            httppatch.setEntity(new UrlEncodedFormEntity(params));

            http = httppatch;

        }
        return http;
    }

}
