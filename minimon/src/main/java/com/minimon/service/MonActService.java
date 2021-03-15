package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.common.CommonSelenium;
import com.minimon.common.CommonUtil;
import com.minimon.entity.MonAct;
import com.minimon.entity.MonCodeData;
import com.minimon.entity.MonResult;
import com.minimon.enums.*;
import com.minimon.repository.MonActRepository;
import com.minimon.vo.MonActCodeResultVO;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MonActService {

    private final CommonSelenium commonSelenium;
    private final ResultService resultService;
    private final MonActRepository monActRepository;

    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monActRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonAct> get(int seq) {
        return monActRepository.findById(seq);
    }

    @Transactional
    public MonAct save(MonAct monAct) {
        monActRepository.save(monAct);
        return monAct;
    }

    @Transactional
    public boolean edit(MonAct monActVO) {
        Optional<MonAct> optionalMonAct = get(monActVO.getSeq());
        optionalMonAct.ifPresent(monAct -> monActRepository.save(monActVO));
        return optionalMonAct.isPresent();
    }

    @Transactional
    public boolean remove(int seq) {
        Optional<MonAct> optionalMonAct = get(seq);
        optionalMonAct.ifPresent(monActRepository::delete);
        return optionalMonAct.isPresent();
    }

    public List<MonAct> findScheduledList() {
        return monActRepository.findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum.Y);
    }

    public List<MonResult> checkList(List<MonAct> monActs) {
        List<MonResult> monResults = new ArrayList<>();
        monActs.forEach(monAct -> {
            MonitoringResultVO monitoringResultVO = executeCodeList(monAct.getCodeDataList());
            monResults.add(errorCheck(monAct, monitoringResultVO));
        });
        return monResults;
    }

    @Transactional
    public MonResult execute(int seq) {
        MonResult monResult = null;

        Optional<MonAct> optionalMonAct = get(seq);
        if (optionalMonAct.isPresent()) {
            MonAct monAct = optionalMonAct.get();
            monResult = resultService.save(errorCheck(monAct, executeCodeList(monAct.getCodeDataList())));
            resultService.sendResultByProperties(monResult);
        }
        return monResult;
    }

    public MonitoringResultVO executeCodeList(MultipartFile monActFile) {
        return executeCodeList(getTestSource(monActFile));
    }

    private MonitoringResultVO executeCodeList(List<MonCodeData> codeDataList) {
        List<MonActCodeResultVO> monActCodeResultVOList = new ArrayList<>();
        long loadTime = 0;
        HttpStatus status;

        EventFiringWebDriver driver = commonSelenium.setUp();

        try {
            long startTime = System.currentTimeMillis();
            monActCodeResultVOList = codeDataList.stream().map(monCodeData -> commonSelenium.executeAction(driver, monCodeData, codeDataList.indexOf(monCodeData))).collect(Collectors.toList());
            long endTime = System.currentTimeMillis();

            loadTime = endTime - startTime;
            status = checkStatus(monActCodeResultVOList);


        } catch (TimeoutException ex) {

            status = HttpStatus.REQUEST_TIMEOUT;

        } catch (Exception e) {

            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();

        } finally {

            if (driver != null) driver.quit();

        }

        return MonitoringResultVO.builder()
                .status(status)
                .totalLoadTime(new Long(loadTime).intValue())
                .response(monActCodeResultVOList)
                .build();
    }

    private HttpStatus checkStatus(List<MonActCodeResultVO> monActCodeResultVOList) {
        HttpStatus status = HttpStatus.OK;
        for (MonActCodeResultVO monActCodeResultVO : monActCodeResultVOList) {
            if (monActCodeResultVO.getStatus() != HttpStatus.OK) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return status;
    }

    private MonResult errorCheck(MonAct monAct, MonitoringResultVO monitoringResultVO) {
        MonitoringResultCodeEnum resultCode = MonitoringResultCodeEnum.SUCCESS;

        if (monAct.getStatus() != monitoringResultVO.getStatus().value()) {
            resultCode = MonitoringResultCodeEnum.UNKNOWN;
        }

        if (monitoringResultVO.getTotalLoadTime() > CommonUtil.getPerData(monAct.getLoadTime(), monAct.getErrorLoadTime(), 1)) {
            resultCode = MonitoringResultCodeEnum.LOAD_TIME;
        }

        return MonResult.builder()
                .title(monAct.getTitle())
                .monitoringTypeEnum(MonitoringTypeEnum.TRANSACTION)
                .relationSeq(monAct.getSeq())
                .resultCode(resultCode)
                .status(monitoringResultVO.getStatus())
                .loadTime(monitoringResultVO.getTotalLoadTime())
                .build();
    }

    private List<MonCodeData> getTestSource(MultipartFile monActFile) {
        List<MonCodeData> codeDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(monActFile.getInputStream()))) {
            String line;
            boolean check = false;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("@Test") > 0) check = true;
                if (check) {
                    MonCodeData monCodeData = getCodeData(line);
                    if (monCodeData != null) {
                        codeDataList.add(monCodeData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
        }
        return codeDataList;
    }

    private MonCodeData getCodeData(String line) {
        MonCodeData monCodeData = null;

        CodeActionEnum codeAction = getCodeAction(line);
        if (codeAction != null) {
            CodeSelectorTypeEnum codeSelectorType = getCodeSelectorType(line);
            String codeSelectorValue = getCodeSelectorValue(line, codeAction);
            String codeValue = getCodeValue(line, codeAction);

            monCodeData = new MonCodeData();
            monCodeData.setCodeAction(codeAction);
            monCodeData.setCodeSelectorType(codeSelectorType);
            monCodeData.setCodeSelectorValue(codeSelectorValue);
            monCodeData.setCodeValue(codeValue);
        }

        return monCodeData;
    }

    private CodeActionEnum getCodeAction(String line) {
        for (CodeActionEnum codeActionEnum : CodeActionEnum.values()) {
            if (line.indexOf(codeActionEnum.getCode()) > 0) {
                return codeActionEnum;
            }
        }
        return null;
    }

    private CodeSelectorTypeEnum getCodeSelectorType(String line) {
        for (CodeSelectorTypeEnum codeSelectorTypeEnum : CodeSelectorTypeEnum.values()) {
            if (line.indexOf(codeSelectorTypeEnum.getCode()) > 0) {
                return codeSelectorTypeEnum;
            }
        }
        return null;
    }

    private String getValueByObject(ValueSubStringTypeEnum subStringTypeEnum, String line, String stObj, String edObj) {
        int stObjLen = stObj.length();
        switch (subStringTypeEnum) {
            case FRONT:
                if (line.indexOf(stObj) < 0) return null;
                return line.substring(line.indexOf(stObj) + stObjLen, line.indexOf(edObj, line.indexOf(stObj)));
            case BACK:
                if (line.lastIndexOf(stObj) < 0) return null;
                return line.substring(line.lastIndexOf(stObj) + stObjLen, line.indexOf(edObj, line.lastIndexOf(stObj)));
        }
        return null;
    }

    private String getCodeSelectorValue(String line, CodeActionEnum codeAction) {
        switch (codeAction) {
            case CLICK:
            case SWITCH:
            case SUBMIT:
            case SEND:
                return getValueByObject(ValueSubStringTypeEnum.FRONT, line, "(\"", "\")");
            default:
                return null;
        }
    }

    private String getCodeValue(String line, CodeActionEnum codeAction) {
        switch (codeAction) {
            case GET:
                return getValueByObject(ValueSubStringTypeEnum.BACK, line, "(\"", "\")");
            case WAIT:
                return getValueByObject(ValueSubStringTypeEnum.FRONT, line, "(\"", "\",");
            case SWITCH:
                return getValueByObject(ValueSubStringTypeEnum.FRONT, line, "(\"", "\")");
            case SEND:
                for (CodeSendKeyTypeEnum codeSendKeyTypeEnum : CodeSendKeyTypeEnum.values()) {
                    if (line.indexOf(codeSendKeyTypeEnum.getCode()) > 0) {
                        return codeSendKeyTypeEnum.getCode();
                    }
                }
                return getValueByObject(ValueSubStringTypeEnum.FRONT, line, "sendKeys(\"", "\");");
            default:
                return null;
        }
    }

}
