package com.minimon.controller;

import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonApiParam;
import com.minimon.entity.TblMonResult;
import com.minimon.repository.TblMonApiRepository;
import com.minimon.service.ApiService;
import com.minimon.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 메인 서버
 *
 * @author 백현우
 */
@RestController
public class ApiController {

    @Autowired
    ApiService apiService;

    @Autowired
    ResultService resultService;

    @Autowired
    TblMonApiRepository tblMonApiRepository;

    /**
     * API DTO Set
     */
    private TblMonApi setTblMonApi(TblMonApi tblMonApi, Map<String, Object> param) throws ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        tblMonApi.setTitle("" + param.get("title"));
        tblMonApi.setUrl("" + param.get("url"));
        tblMonApi.setTimer(Integer.parseInt("" + param.get("timer")));
        tblMonApi.setStartDate(transFormat.parse(param.get("api_start_date").toString()));
        tblMonApi.setEndDate(transFormat.parse(param.get("api_end_date").toString()));
        tblMonApi.setStartHour(Integer.parseInt(param.get("api_start_hour").toString()));
        tblMonApi.setEndHour(Integer.parseInt(param.get("api_end_hour").toString()));
        tblMonApi.setTimeout(Integer.parseInt("" + param.get("timeout")));
        tblMonApi.setUseable(Integer.parseInt("" + param.get("api_useable")));
        tblMonApi.setLoadTime(Double.parseDouble("" + param.get("loadTime")));
        tblMonApi.setErrLoadTime(Integer.parseInt("" + param.get("errLoadTime")));
        tblMonApi.setPayLoad(Double.parseDouble("" + param.get("payLoad")));
        tblMonApi.setPayLoadPer(Integer.parseInt("" + param.get("payLoadPer")));
        tblMonApi.setLoadTimeCheck(Integer.parseInt("" + param.get("api_loadTimeCheck")));
        tblMonApi.setPayLoadCheck(Integer.parseInt("" + param.get("api_payLoadCheck")));
        tblMonApi.setResponseCheck(Integer.parseInt("" + param.get("api_responseCheck")));
        tblMonApi.setStatus(Integer.parseInt("" + param.get("status")));
        tblMonApi.setData_type("" + param.get("data_type"));
        tblMonApi.setMethod("" + param.get("method"));
        tblMonApi.setResponse("" + param.get("response"));
        tblMonApi.setUptDate(new Date());
        if (tblMonApi.getRegDate() == null) tblMonApi.setRegDate(new Date());
        tblMonApi.setApiParams(setTblMonApiParams(param));

        return tblMonApi;
    }


    /**
     * API DTO Set
     */
    private ArrayList<TblMonApiParam> setTblMonApiParams(Map<String, Object> param) {
        ArrayList<TblMonApiParam> apiParams = new ArrayList<TblMonApiParam>();

        try {

            if (param.get("keys") == null) return apiParams;

            String[] keys = ("" + param.get("keys")).substring(1, ("" + param.get("keys")).length() - 1).split(",");
            String[] values = ("" + param.get("values")).substring(1, ("" + param.get("values")).length() - 1).split(",");

            for (int i = 0; i < keys.length; i++) {
                TblMonApiParam tblMonApiParam = new TblMonApiParam();
                tblMonApiParam.setParam_key(keys[i].replaceAll("\"", ""));
                tblMonApiParam.setParam_value(values[i].replaceAll("\"", ""));
                if (tblMonApiParam.getRegDate() == null) tblMonApiParam.setRegDate(new Date());
                tblMonApiParam.setUptDate(new Date());
                apiParams.add(tblMonApiParam);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return apiParams;
    }

    /**
     * API LIST  호출
     */
    @RequestMapping(path = "/api", method = RequestMethod.GET)
    public HashMap<String, Object> getUrls() {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            List<TblMonApi> apiList = tblMonApiRepository.findAll();
            result.put("apiList", apiList);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API 생성
     */
    @RequestMapping(path = "/api", method = RequestMethod.POST)
    public HashMap<String, Object> createAPi(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            tblMonApiRepository.save(setTblMonApi(new TblMonApi(), param));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API INFO  호출
     */
    @RequestMapping(path = "/api/{seq}", method = RequestMethod.GET)
    public HashMap<String, Object> getApi(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            TblMonApi TblMonApi = tblMonApiRepository.findBySeq(seq);
            result.put("data", TblMonApi);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API 업데이트
     */
    @RequestMapping(path = "/api/{seq}", method = RequestMethod.PUT)
    public HashMap<String, Object> updateApi(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);

            if (existsApi != null) {

                tblMonApiRepository.save(setTblMonApi(existsApi, param));

            }

            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API 삭제
     */
    @RequestMapping(path = "/api/{seq}", method = RequestMethod.DELETE)
    public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);

            if (existsApi != null) {

                tblMonApiRepository.delete(existsApi);

            }

            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API CHECK
     */
    @RequestMapping(path = "/apiCheck", method = RequestMethod.POST)
    public HashMap<String, Object> apiCheck(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            TblMonApi tblMonApi = setTblMonApi(new TblMonApi(), param);
            result.put("data", apiService.executeApi(tblMonApi));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API  검사 실행
     */
    @RequestMapping(path = "/apiExecute/{seq}", method = RequestMethod.GET)
    public HashMap<String, Object> apiExecute(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        try {

            TblMonApi existsApi = tblMonApiRepository.findBySeq(seq);

            if (existsApi != null) {

                Map<String, Object> logData = apiService.executeApi(existsApi);
                Map<String, Object> data = apiService.errorCheckApi(existsApi, logData);
                result.put(existsApi.getUrl(), data);

                TblMonResult tblMonResult = resultService.saveResult(data);
                resultService.sendResultByProperties(tblMonResult);
            }

            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


}