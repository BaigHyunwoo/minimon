package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.common.CommonSelenium;
import com.minimon.common.CommonUtil;
import com.minimon.entity.MonCodeData;
import com.minimon.entity.MonResult;
import com.minimon.entity.MonTransaction;
import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.repository.MonTransactionRepository;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonTransactionService {

    private final CommonSelenium commonSelenium;
    private final MonTransactionRepository monTransactionRepository;

    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monTransactionRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonTransaction> get(int seq) {
        return monTransactionRepository.findById(seq);
    }

    @Transactional
    public MonTransaction save(MonTransaction monTransaction) {
        monTransactionRepository.save(monTransaction);
        return monTransaction;
    }

    @Transactional
    public boolean edit(MonTransaction monTransactionVO) {
        Optional<MonTransaction> optionalMonTransaction = get(monTransactionVO.getSeq());
        optionalMonTransaction.ifPresent(monTransaction -> monTransactionRepository.save(monTransactionVO));
        return optionalMonTransaction.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonTransaction> optionalMonTransaction = get(seq);
        optionalMonTransaction.ifPresent(monTransactionRepository::delete);
        return optionalMonTransaction.isPresent();
    }

    public List<MonResult> checkList(List<MonTransaction> monTransactions) {
        List<MonResult> monResults = new ArrayList<>();
        monTransactions.forEach(monTransaction -> {
            MonitoringResultVO monitoringResultVO = execute(monTransaction.getCodeDataList());
            monResults.add(errorCheck(monTransaction, monitoringResultVO));
        });
        return monResults;
    }

    public HttpStatus checkStatus(Map<String, Object> responseData) {

        HttpStatus status = HttpStatus.OK;

        try {

            for (String key : responseData.keySet()) {
                if (responseData.get(key).equals("ERR") == true) status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();

        }

        return status;
    }

    public MonResult errorCheck(MonTransaction transaction, MonitoringResultVO monitoringResultVO) {
        MonitoringResultCodeEnum resultCode = MonitoringResultCodeEnum.SUCCESS;

        if (transaction.getStatus() != monitoringResultVO.getStatus().value()) {
            resultCode = MonitoringResultCodeEnum.UNKNOWN;
        }

        if (monitoringResultVO.getTotalLoadTime() > CommonUtil.getPerData(transaction.getLoadTime(), transaction.getErrorLoadTime(), 1)) {
            resultCode = MonitoringResultCodeEnum.LOAD_TIME;
        }

        return MonResult.builder()
                .title(transaction.getTitle())
                .monitoringTypeEnum(MonitoringTypeEnum.TRANSACTION)
                .relationSeq(transaction.getSeq())
                .resultCode(resultCode)
                .status(monitoringResultVO.getStatus())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .build();
    }

    public List<MonCodeData> getTestSource(MultipartFile transactionFile) {
        List<MonCodeData> codeDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(transactionFile.getInputStream()))) {
            String line;
            boolean check = false;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("@Test") > 0) check = true;
                if (check == true) {
                    MonCodeData monCodeData = getCodeData(line);
                    if (monCodeData != null) {
                        codeDataList.add(monCodeData);
                        log.debug(monCodeData.getAction() + " " + monCodeData.getSelector_type() + "  " + monCodeData.getSelector_value() + "     " + monCodeData.getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
        return codeDataList;
    }

    public MonitoringResultVO executeFile(MultipartFile transactionFile) {
        return execute(getTestSource(transactionFile));
    }


    public MonitoringResultVO execute(List<MonCodeData> codeDataList) {
        Map<String, Object> responseData = new HashMap<>();
        long loadTime = 0;
        HttpStatus status = HttpStatus.OK;

        EventFiringWebDriver driver = commonSelenium.setUp();

        try {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < codeDataList.size(); i++) {

                MonCodeData monCodeData = codeDataList.get(i);
                responseData.put("" + i, commonSelenium.executeAction(driver, monCodeData.getAction(), monCodeData.getSelector_type(), monCodeData.getSelector_value(), monCodeData.getValue()));

            }
            long endTime = System.currentTimeMillis();

            loadTime = endTime - startTime;
            status = checkStatus(responseData);


        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (driver != null) driver.quit();

        }

        return MonitoringResultVO.builder()
                .status(status)
                .totalLoadTime(new Long(loadTime).intValue())
                .response(responseData.toString())
                .build();
    }


    /**
     * transaction Code 분석
     */
    public MonCodeData getCodeData(String line) {
        MonCodeData monCodeData = null;

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
