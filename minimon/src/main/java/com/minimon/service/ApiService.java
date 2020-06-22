package com.minimon.service;

import com.minimon.common.CommonUtils;
import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonApiParam;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonApiRepository;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class ApiService {

    @Autowired
    TblMonApiRepository tblMonApiRepository;

    private String className = this.getClass().toString();
    private Logger logger = LoggerFactory.getLogger(ApiService.class);


    public List<TblMonApi> findApi() {
        Date now = new Date();
        int hours = now.getHours();
        return tblMonApiRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    /**
     * API 모니터링 검사 실행
     */
    public Map<String, Object> checkApis(List<TblMonApi> apis) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

            for (TblMonApi api : apis) {
                Map<String, Object> logData = executeApi(api);
                checkData.put("" + api.getSeq(), errorCheckApi(api, logData));
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

        return checkData;
    }


    /**
     * API 실행
     */
    public Map<String, Object> executeApi(TblMonApi api) throws Exception {
        Map<String, Object> logData = new HashMap<String, Object>();

        try {

            logData = httpSending(api);

            logger.debug(logData.toString());


        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 12);

        }

        return logData;
    }


    /**
     * URL 에러 검사
     */
    public Map<String, Object> errorCheckApi(TblMonApi api, Map<String, Object> logData) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

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

        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 13);

        }

        return checkData;
    }


    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String response, TblMonApi api) {
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

    /**
     * HTTP SENDING
     */
    @SuppressWarnings("deprecation")
    public Map<String, Object> httpSending(TblMonApi api) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try {

            HttpClient httpclient = HttpClients.createDefault();

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            for (TblMonApiParam tblMonApiParam : api.getApiParams()) {
                params.add(new BasicNameValuePair(tblMonApiParam.getParam_key(), tblMonApiParam.getParam_value()));
            }

            long st = System.currentTimeMillis();
            HttpResponse response = httpclient.execute(httpRequest(api.getMethod(), api.getUrl(), params));
            long ed = System.currentTimeMillis();

            result = getApiLogData(st, ed, response);
            httpclient.getConnectionManager().shutdown();

        } catch (Exception e) {

            e.printStackTrace();
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 14);

        }

        return result;
    }


    /**
     * GET HTTP LOG DATA
     */
    public Map<String, Object> getApiLogData(long st, long ed, HttpResponse response) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        BufferedReader reader = null;

        try {

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 15);
        }

        return result;
    }


    /**
     * HTTP REQUEST
     */
    public HttpUriRequest httpRequest(String method, String url, List<NameValuePair> params) throws Exception {
        HttpUriRequest http = null;

        try {

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 16);
        }

        return http;
    }

}
