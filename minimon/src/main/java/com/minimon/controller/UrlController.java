package com.minimon.controller;

import com.minimon.entity.MonResult;
import com.minimon.entity.MonUrl;
import com.minimon.service.ResultService;
import com.minimon.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;
    private final ResultService resultService;

    private MonUrl setTblMonUrl(MonUrl monUrl, Map<String, Object> param) throws ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        monUrl.setTitle(param.get("title").toString());
        monUrl.setUrl(param.get("url").toString());
        monUrl.setTimer(Integer.parseInt(param.get("timer").toString()));
        monUrl.setTimeout(Integer.parseInt(param.get("timeout").toString()));
        monUrl.setUseable(Integer.parseInt(param.get("useable").toString()));
        monUrl.setLoadTime(Double.parseDouble(param.get("loadTime").toString()));
        monUrl.setErrLoadTime(Integer.parseInt(param.get("errLoadTime").toString()));
        monUrl.setPayLoad(Double.parseDouble(param.get("payLoad").toString()));
        monUrl.setPayLoadPer(Integer.parseInt(param.get("payLoadPer").toString()));
        monUrl.setStatus(Integer.parseInt(param.get("status").toString()));
        monUrl.setLoadTimeCheck(Integer.parseInt(param.get("url_loadTimeCheck").toString()));
        monUrl.setPayLoadCheck(Integer.parseInt(param.get("url_payLoadCheck").toString()));
        monUrl.setStartDate(transFormat.parse(param.get("url_start_date").toString()));
        monUrl.setEndDate(transFormat.parse(param.get("url_end_date").toString()));
        monUrl.setStartHour(Integer.parseInt(param.get("url_start_hour").toString()));
        monUrl.setEndHour(Integer.parseInt(param.get("url_end_hour").toString()));
        monUrl.setTextCheck(Integer.parseInt(param.get("textCheck").toString()));
        monUrl.setTextCheckValue((param.get("textCheckValue").toString()));
        monUrl.setUptDate(new Date());
        if (monUrl.getRegDate() == null) monUrl.setRegDate(new Date());
        return monUrl;
    }

    @GetMapping(path = "/url")
    public HashMap<String, Object> getUrls() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("urlList", urlService.getUrlList());
        result.put("result", "success");
        return result;
    }


    @PostMapping(path = "/url")
    public HashMap<String, Object> createUrl(@RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            urlService.saveUrl(setTblMonUrl(new MonUrl(), param));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }


    @GetMapping(path = "/url/{seq}")
    public HashMap<String, Object> getUrl(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            result.put("data", urlService.getUrl(seq));
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

    @PutMapping(path = "/url/{seq}")
    public HashMap<String, Object> updateUrl(@PathVariable("seq") int seq, @RequestParam Map<String, Object> param) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {


            Optional.ofNullable(urlService.getUrl(seq)).ifPresent(monUrl -> {
                try {
                    urlService.saveUrl(setTblMonUrl(monUrl, param));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });


            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

    @DeleteMapping(path = "/url/{seq}")
    public HashMap<String, Object> delete(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {


            urlService.remove(seq);

            result.put("data", seq);
            result.put("result", "success");


        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

    @PostMapping(path = "/url/check")
    public Map<String, Object> urlCheck(@RequestParam Map<String, Object> data) {
        Map<String, Object> result = new HashMap<String, Object>();

        try {

            result.put("data", urlService.executeUrl("" + data.get("url"), Integer.parseInt("" + data.get("timeout"))));
            result.put("result", "success");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

    @PostMapping(path = "/url/{seq}/execute")
    public HashMap<String, Object> urlExecute(@PathVariable("seq") int seq) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {

            MonUrl existsUrl = urlService.getUrl(seq);

            if (existsUrl != null) {

                Map<String, Object> logData = urlService.executeUrl(existsUrl.getUrl(), existsUrl.getTimeout());
                Map<String, Object> data = urlService.errorCheckUrl(existsUrl, logData);
                result.put(existsUrl.getUrl(), data);

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