package com.minimon.service;

import com.minimon.common.CommonUtils;
import com.minimon.common.SeleniumHandler;
import com.minimon.entity.MonCodeData;
import com.minimon.entity.MonTransaction;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TblMonTransactionRepository tblMonTransactionRepository;

    private String className = this.getClass().toString();

    /**
     * transaction 모니터링 검사 실행
     */
    public Map<String, Object> checkTransactions(List<MonTransaction> monTransactions) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

            for (MonTransaction transaction : monTransactions) {
                Map<String, Object> logData = executeTransaction(transaction.getCodeDatas());
                checkData.put("" + transaction.getSeq(), errorCheckTransaction(transaction, logData));
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

        return checkData;
    }


    public List<MonTransaction> findTransactionUseable() {
        Date now = new Date();
        int hours = now.getHours();
        return tblMonTransactionRepository.findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                1, now, now, hours, hours);
    }

    /**
     * transaction 모니터링 검사 상태 검사
     */
    public int checkStatus(Map<String, Object> logData) throws Exception {

        int status = 200;

        try {

            for (String key : logData.keySet()) {

                if (logData.get(key).equals("ERR") == true) status = 500;

            }

        } catch (Exception e) {

            status = 500;

            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 11);

        }

        return status;
    }


    /**
     * transaction 에러 검사
     */
    public Map<String, Object> errorCheckTransaction(MonTransaction transaction, Map<String, Object> logData) throws Exception {
        Map<String, Object> checkData = new HashMap<String, Object>();

        try {

            String result = "SUCCESS";
            int status = Integer.parseInt("" + logData.get("status"));
            double loadTime = Double.parseDouble("" + logData.get("loadTime"));


            /*
             * CHECK
             */
            if (transaction.getStatus() == status) checkData.put("status", "SUCCESS");
            else {
                checkData.put("status", "ERR");
                result = "status ERR";
            }

            if (loadTime <= CommonUtils.getPerData(transaction.getLoadTime(), transaction.getErrLoadTime(), 1))
                checkData.put("loadTime", "SUCCESS");
            else {
                checkData.put("loadTime", " ERR");
                result = "loadTime ERR";
            }


            /*
             * SET PARAM
             */
            checkData.put("logData", logData);
            checkData.put("check_loadTime", loadTime);
            checkData.put("check_status", status);
            checkData.put("origin_loadTime", transaction.getLoadTime());
            checkData.put("origin_status", transaction.getStatus());
            checkData.put("seq", transaction.getSeq());
            checkData.put("type", "TRANSACTION");
            checkData.put("title", transaction.getTitle());
            checkData.put("result", result);

        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 13);

        }

        return checkData;
    }


    /**
     * transaction Code 실행
     */
    public Map<String, Object> executeTransaction(List<MonCodeData> codeDatas) throws Exception {
        Map<String, Object> logData = new HashMap<String, Object>();
        EventFiringWebDriver driver = null;

        try {

            SeleniumHandler selenium = new SeleniumHandler();
            driver = selenium.setUp();

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < codeDatas.size(); i++) {

                MonCodeData monCodeData = codeDatas.get(i);
                logData.put("" + i, selenium.executeAction(selenium, driver, monCodeData.getAction(), monCodeData.getSelector_type(), monCodeData.getSelector_value(), monCodeData.getValue()));

            }
            long endTime = System.currentTimeMillis();


            logData.put("loadTime", endTime - startTime);
            logData.put("status", checkStatus(logData));


            log.debug(logData.toString());

        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 14);

        } finally {

            if (driver != null) driver.quit();

        }

        return logData;
    }


    /**
     * transaction Code 분석
     */
    public MonCodeData getCodeData(String line) throws Exception {
        MonCodeData monCodeData = null;

        try {

            String action = getCodeAction(line);
            if (action != null) {

                String selector_type = getCodeSelectorType(line);
                String selector_value = getCodeSelectorValue(line, action);
                String value = getCodeValue(line, action, selector_type);

                monCodeData = new MonCodeData();
                monCodeData.setAction(action);
                monCodeData.setSelector_type(selector_type);
                monCodeData.setSelector_value(selector_value);
                monCodeData.setValue(value);
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new MyException("CLASS : " + className + " - METHOD : " + new Object() {
            }.getClass().getEnclosingMethod().getName() + " "
                    + "- TYPE = [Function]/  Function - execute", className, 15);

        }

        return monCodeData;

    }


    public String getCodeAction(String line) {

        if (line.indexOf("driver.get") > 0) {

            return "get";

        } else if (line.indexOf("setSize") > 0) {

            return "size";

        } else if (line.indexOf("getWindowHandles") > 0) {

            return "window_handles";

        } else if (line.indexOf("waitForWindow") > 0) {

            return "wait";

        } else if (line.indexOf("switchTo") > 0) {

            return "switch";

        } else if (line.indexOf("click") > 0) {

            return "click";

        } else if (line.indexOf("submit") > 0) {

            return "submit";

        } else if (line.indexOf("sendKeys") > 0) {

            return "sendKeys";

        }

        return null;
    }

    public String getCodeSelectorType(String line) {

        if (line.indexOf("By.id") > 0) {

            return "By.id";

        } else if (line.indexOf("By.cssSelector") > 0) {

            return "By.cssSelector";

        } else if (line.indexOf("By.linkText") > 0) {

            return "By.linkText";

        } else if (line.indexOf("By.className") > 0) {

            return "By.className";

        } else if (line.indexOf("By.name") > 0) {

            return "By.name";

        } else if (line.indexOf("By.tagName") > 0) {

            return "By.tagName";

        } else if (line.indexOf("By.xpath") > 0) {

            return "By.xpath";

        } else if (line.indexOf("By.partialLinkText") > 0) {

            return "By.partialLinkText";

        }

        return null;
    }

    public String getValueByObject(String type, String line, String stObj, String edObj) {

        int stObjLen = stObj.length();

        if (type.equals("first") == true) {

            if (line.indexOf(stObj) < 0) return null;
            return line.substring(line.indexOf(stObj) + stObjLen, line.indexOf(edObj, line.indexOf(stObj)));

        } else {

            if (line.lastIndexOf(stObj) < 0) return null;
            return line.substring(line.lastIndexOf(stObj) + stObjLen, line.indexOf(edObj, line.lastIndexOf(stObj)));

        }
    }

    public String getCodeSelectorValue(String line, String action) {


        if (action.equals("switch") == true || action.equals("click") == true || action.equals("submit") == true || action.equals("sendKeys") == true) {

            return getValueByObject("first", line, "(\"", "\")");

        }

        return null;
    }

    public String getCodeValue(String line, String action, String selector) {

        if (action.equals("get") == true) {

            return getValueByObject("last", line, "(\"", "\")");

        } else if (action.equals("wait") == true) {

            return getValueByObject("first", line, "(\"", "\",");

        } else if (action.equals("switch") == true) {

            return getValueByObject("first", line, "(\"", "\")");

        } else if (action.equals("sendKeys") == true) {

            if (line.indexOf("Keys.ENTER") > 0) {

                return "Keys.ENTER";

            } else if (line.indexOf("Keys.BACK_SPACE") > 0) {

                return "Keys.BACK_SPACE";

            } else if (line.indexOf("Keys.DELETE") > 0) {

                return "Keys.DELETE";

            } else if (line.indexOf("Keys.SPACE") > 0) {

                return "Keys.SPACE";

            } else if (line.indexOf("Keys.ESCAPE") > 0) {

                return "Keys.ESCAPE";

            } else {

                return getValueByObject("first", line, "sendKeys(\"", "\");");

            }

        }

        return null;
    }

}
