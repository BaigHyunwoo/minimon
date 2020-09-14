package com.minimon.controller;

import com.minimon.entity.MonApi;
import com.minimon.entity.MonApiParam;
import com.minimon.entity.MonResult;
import com.minimon.repository.MonApiRepository;
import com.minimon.service.ApiService;
import com.minimon.service.ResultService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"Api Controller"})
public class ApiController {

    private final ApiService apiService;

    private final ResultService resultService;

    private final MonApiRepository monApiRepository;

    /**
     * API DTO Set
     */
    private MonApi setTblMonApi(MonApi monApi, Map<String, Object> param) throws ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        monApi.setTitle("" + param.get("title"));
        monApi.setUrl("" + param.get("url"));
        monApi.setTimer(Integer.parseInt("" + param.get("timer")));
        monApi.setStartDate(transFormat.parse(param.get("api_start_date").toString()));
        monApi.setEndDate(transFormat.parse(param.get("api_end_date").toString()));
        monApi.setStartHour(Integer.parseInt(param.get("api_start_hour").toString()));
        monApi.setEndHour(Integer.parseInt(param.get("api_end_hour").toString()));
        monApi.setTimeout(Integer.parseInt("" + param.get("timeout")));
        monApi.setUseable(Integer.parseInt("" + param.get("api_useable")));
        monApi.setLoadTime(Double.parseDouble("" + param.get("loadTime")));
        monApi.setErrLoadTime(Integer.parseInt("" + param.get("errLoadTime")));
        monApi.setPayLoad(Double.parseDouble("" + param.get("payLoad")));
        monApi.setPayLoadPer(Integer.parseInt("" + param.get("payLoadPer")));
        monApi.setLoadTimeCheck(Integer.parseInt("" + param.get("api_loadTimeCheck")));
        monApi.setPayLoadCheck(Integer.parseInt("" + param.get("api_payLoadCheck")));
        monApi.setResponseCheck(Integer.parseInt("" + param.get("api_responseCheck")));
        monApi.setStatus(Integer.parseInt("" + param.get("status")));
        monApi.setData_type("" + param.get("data_type"));
        monApi.setMethod("" + param.get("method"));
        monApi.setResponse("" + param.get("response"));
        monApi.setUptDate(new Date());
        if (monApi.getRegDate() == null) monApi.setRegDate(new Date());
        monApi.setApiParams(setTblMonApiParams(param));

        return monApi;
    }


    /**
     * API DTO Set
     */
    private ArrayList<MonApiParam> setTblMonApiParams(Map<String, Object> param) {
        ArrayList<MonApiParam> apiParams = new ArrayList<MonApiParam>();

        try {

            if (param.get("keys") == null) return apiParams;

            String[] keys = ("" + param.get("keys")).substring(1, ("" + param.get("keys")).length() - 1).split(",");
            String[] values = ("" + param.get("values")).substring(1, ("" + param.get("values")).length() - 1).split(",");

            for (int i = 0; i < keys.length; i++) {
                MonApiParam monApiParam = new MonApiParam();
                monApiParam.setParam_key(keys[i].replaceAll("\"", ""));
                monApiParam.setParam_value(values[i].replaceAll("\"", ""));
                if (monApiParam.getRegDate() == null) monApiParam.setRegDate(new Date());
                monApiParam.setUptDate(new Date());
                apiParams.add(monApiParam);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return apiParams;
    }

    /**
     * API LIST  호출
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public HashMap<String, Object> getUrls() {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            List<MonApi> apiList = monApiRepository.findAll();
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
    @RequestMapping(path = "", method = RequestMethod.POST)
    public HashMap<String, Object> createAPi(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            monApiRepository.save(setTblMonApi(new MonApi(), param));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API INFO  호출
     */
    @RequestMapping(path = "/{seq}", method = RequestMethod.GET)
    public HashMap<String, Object> getApi(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            MonApi MonApi = monApiRepository.findBySeq(seq);
            result.put("data", MonApi);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API 업데이트
     */
    @RequestMapping(path = "/{seq}", method = RequestMethod.PUT)
    public HashMap<String, Object> updateApi(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            MonApi existsApi = monApiRepository.findBySeq(seq);

            if (existsApi != null) {

                monApiRepository.save(setTblMonApi(existsApi, param));

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
    @RequestMapping(path = "/{seq}", method = RequestMethod.DELETE)
    public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            MonApi existsApi = monApiRepository.findBySeq(seq);

            if (existsApi != null) {

                monApiRepository.delete(existsApi);

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
    @RequestMapping(path = "check", method = RequestMethod.POST)
    public HashMap<String, Object> apiCheck(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            MonApi monApi = setTblMonApi(new MonApi(), param);
            result.put("data", apiService.executeApi(monApi));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    /**
     * API  검사 실행
     */
    @RequestMapping(path = "execute/{seq}", method = RequestMethod.GET)
    public HashMap<String, Object> apiExecute(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        try {

            MonApi existsApi = monApiRepository.findBySeq(seq);

            if (existsApi != null) {

                Map<String, Object> logData = apiService.executeApi(existsApi);
                Map<String, Object> data = apiService.errorCheckApi(existsApi, logData);
                result.put(existsApi.getUrl(), data);

                MonResult monResult = resultService.saveResult(data);
                resultService.sendResultByProperties(monResult);
            }

            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


}