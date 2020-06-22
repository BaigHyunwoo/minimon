package com.minimon.service;

import com.minimon.common.CommonUtils;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.TblMonUrl;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonUrlRepository;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UrlService {

    @Autowired
    TblMonUrlRepository tblMonUrlRepository;

    private String className = this.getClass().toString();

    private Logger logger = LoggerFactory.getLogger(UrlService.class);


    /**
     * URL 모니터링 검사 실행
     */
    public Map<String, Object> checkUrls(List<TblMonUrl> urls) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

            for (TblMonUrl url : urls) {
                Map<String, Object> logData = executeUrl(url.getUrl(), url.getTimeout());
                checkData.put(url.getUrl(), errorCheckUrl(url, logData));
            }

        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

        return checkData;
    }

    public List<TblMonUrl> findUrl() {
        Date now = new Date();
        int hours = now.getHours();
        return tblMonUrlRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    /**
     * URL 실행
     */
    public Map<String, Object> executeUrl(String url, int timeout) throws Exception {
        Map<String, Object> logData = new HashMap<String, Object>();
        EventFiringWebDriver driver = null;

        try {

            SeleniumHandler selenium = new SeleniumHandler();
            driver = selenium.setUp();

            selenium.connectUrl(url, driver, timeout);
            logData = selenium.expectionLog(
                    selenium.getLog(driver),
                    driver.getCurrentUrl()
            );
            logData.put("source", selenium.getSource(driver));

            logger.debug(logData.toString());


        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 12);

        } finally {

            if (driver != null) driver.quit();

        }

        return logData;
    }


    /**
     * URL 에러 검사
     */
    public Map<String, Object> errorCheckUrl(TblMonUrl url, Map<String, Object> logData) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

            int status = Integer.parseInt("" + logData.get("status"));
            double totalLoadTime = Double.parseDouble("" + logData.get("totalLoadTime"));
            double totalPayLoad = Double.parseDouble("" + logData.get("totalPayLoad"));
            String source = logData.get("source").toString();

            checkData.put("url", url.getUrl());
            checkData.put("seq", url.getSeq());
            checkData.put("type", "URL");
            checkData.put("title", url.getTitle());
            checkData.put("check_loadTime", totalLoadTime);
            checkData.put("result", errCheck(status, totalLoadTime, totalPayLoad, source, url));

        } catch (Exception e) {
            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

        return checkData;
    }

    public String errCheck(int status, double totalLoadTime, double totalPayLoad, String source, TblMonUrl url) {
        if (status >= 400)
            return status + " ERR";
        else if (url.getLoadTimeCheck() == 1 && totalLoadTime >= url.getErrLoadTime())
            return "LOAD TIME ERR";
        else if (url.getPayLoadCheck() == 1 && (CommonUtils.getPerData(url.getPayLoad(), url.getPayLoadPer(), 2) > totalPayLoad
                || totalPayLoad > CommonUtils.getPerData(url.getPayLoad(), url.getPayLoadPer(), 1)))
            return "PAYLOAD ERR";
        else if (url.getTextCheck() == 1 && source.indexOf(url.getTextCheckValue()) >= 0) {
            return "TEXT ERR";
        }
        else
            return "SUCCESS";
    }

    public void saveUrl(TblMonUrl tblMonUrl) {
        tblMonUrlRepository.save(tblMonUrl);
    }

    public List<TblMonUrl> getUrlList() {
        return tblMonUrlRepository.findAll();
    }

    public TblMonUrl getUrl(int seq) {
        return tblMonUrlRepository.findBySeq(seq);
    }

    public void remove(int seq) {
        Optional.of(getUrl(seq)).ifPresent(tblMonUrl -> tblMonUrlRepository.delete(tblMonUrl));
    }
}
