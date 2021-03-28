package com.minimon.common;

import com.minimon.entity.MonCodeData;
import com.minimon.enums.*;
import com.minimon.exception.UndefinedDriverException;
import com.minimon.vo.MonActCodeResultVO;
import com.minimon.vo.MonUrlResourceVO;
import com.minimon.vo.MonitoringResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommonSelenium {
    private final CommonProperties commonProperties;

    private Map<String, Object> vars;

    private JavascriptExecutor js;

    private int waitForWindowTimeout = 5000;

    private class WebDriverEventListenerClass extends AbstractWebDriverEventListener {

        long startTime, endTime;

        public void beforeNavigateTo(String arg0, WebDriver arg1) {
            startTime = System.currentTimeMillis();
        }

        public void afterNavigateTo(String arg0, WebDriver arg1) {
            endTime = System.currentTimeMillis();
        }

        public int returnLoadTime() {
            return Long.valueOf(endTime - startTime).intValue();
        }
    }

    public String waitForWindow(EventFiringWebDriver driver, int timeout) {

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<String> whNow = driver.getWindowHandles();
        Set<String> whThen = (Set<String>) vars.get("window_handles");

        if (whNow.size() > whThen.size()) {
            whNow.removeAll(whThen);
        }
        return whNow.iterator().next();
    }

    public EventFiringWebDriver setUp() {

        EventFiringWebDriver driver = null;

        try {

            // 기존 프로세스 킬
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver");

            // 크롬 드라이버 파일 경로설정
            System.setProperty(commonProperties.getDriverName(), commonProperties.getDriverPath() + commonProperties.getDriverFileName());

            LoggingPreferences logPrefs = new LoggingPreferences();

            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("w3c", false);

            /**
             *  동작 체크용
             *  브라우저 창 가리려면 주석 제거
             */
            // options.addArguments("headless");

            options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
            driver = new EventFiringWebDriver(new ChromeDriver(options));
            js = driver;
            vars = new HashMap<>();
            log.debug("WebDriver - 연결 완료");

        } catch (IOException e) {
            e.printStackTrace();
            if (driver != null) driver.quit();
        } catch (IllegalStateException ex) {
            throw new UndefinedDriverException();
        }

        return driver;
    }


    public int connect(String url, EventFiringWebDriver driver, int timeout) {
        driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
        int totalLoadTime;

        try {

            WebDriverEventListenerClass event = new WebDriverEventListenerClass();
            driver.register(event);
            driver.navigate().to(url);
            totalLoadTime = event.returnLoadTime();

        } catch (TimeoutException e1) {
            totalLoadTime = ConnectErrorCodeEnum.TIMEOUT.getValue();

        } catch (Exception e) {
            totalLoadTime = ConnectErrorCodeEnum.UNKNOWN.getValue();
            log.info("Error - Unknown " + e.getMessage());
        }

        return totalLoadTime;
    }


    public LogEntries getLog(EventFiringWebDriver driver) {
        LogEntries logs = null;

        try {

            logs = driver.manage().logs().get(LogType.PERFORMANCE);

            log.debug("WebDriver - Log 호출 완료");

        } catch (TimeoutException ex) {

            return logs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }

    public MonitoringResultVO getResult(LogEntries logs, String currentURL, int totalLoadTime) {
        List<MonUrlResourceVO> monUrlResourceVOList = new ArrayList<>();
        HttpStatus resultStatus = HttpStatus.BAD_GATEWAY;

        int resourceCnt = 0;
        for (Iterator<LogEntry> it = logs.iterator(); it.hasNext(); resourceCnt++) {
            JSONObject resourceMessage = getResourceMessage(it.next());
            if (isExist(resourceMessage)) {
                JSONObject resourceParam = resourceMessage.getJSONObject("params");
                JSONObject resourceResponse = resourceParam.getJSONObject("response");

                Optional<HttpStatus> isExistStatus = Optional.ofNullable(getResultStatus(resourceResponse, currentURL));
                if (isExistStatus.isPresent()) {
                    resultStatus = isExistStatus.get();
                } else {
                    monUrlResourceVOList.add(getResourceData(resourceResponse, resourceCnt));
                }
            }
        }

        log.debug("WebDriver - Log 분석 완료");
        return MonitoringResultVO.builder()
                .totalLoadTime(totalLoadTime)
                .url(currentURL)
                .status(resultStatus)
                .response(monUrlResourceVOList)
                .build();
    }

    public JSONObject getResourceMessage(LogEntry entry) {
        JSONObject json = new JSONObject(entry.getMessage());
        return json.getJSONObject("message");
    }

    public Boolean isExist(JSONObject message) {
        String methodName = message.getString("method");
        if (methodName != null && methodName.equals("Network.responseReceived") == false) {
            return false;
        }
        return true;
    }

    public HttpStatus getResultStatus(JSONObject response, String currentURL) {
        if (currentURL.equals(response.get("url"))) {
            return HttpStatus.valueOf(response.getInt("status"));
        }
        return null;
    }

    public MonUrlResourceVO getResourceData(JSONObject resourceResponse, int sortOrder) {
        JSONObject headersObj = resourceResponse.getJSONObject("headers");
        Map<String, Object> headers = new CaseInsensitiveMap<>();
        headers.putAll(headersObj.toMap());

        double payLoad = headers.containsKey("content-length") ? Double.parseDouble(headers.get("content-length").toString()) : 0;
        String type = headers.containsKey("content-type") ? headers.get("content-type").toString() : "";
        int status = resourceResponse.getInt("status");
        String resourceUrl = resourceResponse.getString("url");
        double resourceLoadTime = 0;

        if (!resourceResponse.isNull("timing")) {
            JSONObject timing = resourceResponse.getJSONObject("timing");
            for (Iterator<String> itt = timing.keys(); itt.hasNext(); ) {
                String key = itt.next();
                if (key.equals("requestTime") == false && timing.getDouble(key) > 0) {
                    resourceLoadTime += timing.getDouble(key);
                }
            }
        }

        return MonUrlResourceVO.builder()
                .url(resourceUrl)
                .type(type)
                .status(status)
                .payLoad(payLoad)
                .sortOrder(sortOrder)
                .loadTime(resourceLoadTime)
                .build();
    }


    public MonActCodeResultVO executeAction(EventFiringWebDriver driver, MonCodeData monCodeData, int sortOrder) {
        HttpStatus status = HttpStatus.OK;
        String response;
        CodeActionEnum codeAction = monCodeData.getCodeAction();
        CodeSelectorTypeEnum codeSelectorType = monCodeData.getCodeSelectorType();
        String codeSelectorValue = monCodeData.getCodeSelectorValue();
        String codeValue = monCodeData.getCodeValue();
        WebElement element;
        try {

            switch (codeAction) {
                case GET:
                    driver.get(codeValue);
                    break;
                case SIZE:
                    driver.manage().window().maximize();
                    break;
                case WINDOW_HANDLES:
                    vars.put("window_handles", driver.getWindowHandles());
                    break;
                case WAIT:
                    vars.put(codeSelectorValue, waitForWindow(driver, waitForWindowTimeout));
                    break;
                case SWITCH:
                    if (codeSelectorValue != null) driver.switchTo().frame(codeSelectorValue).toString();
                    else driver.switchTo().defaultContent();
                    break;
                case CLICK:
                    element = getSelector(driver, codeSelectorType, codeSelectorValue);
                    element.click();
                    break;
                case SEND:
                    element = getSelector(driver, codeSelectorType, codeSelectorValue);
                    Object codeSendKeyType = CodeSendKeyTypeEnum.codeOf(codeValue);
                    if (codeSendKeyType == null) {
                        element.sendKeys(codeValue);
                    } else if (CodeSendKeyTypeEnum.ENTER.equals(codeSendKeyType)) {
                        element.sendKeys(Keys.ENTER);
                    } else if (CodeSendKeyTypeEnum.SPACE.equals(codeSendKeyType)) {
                        element.sendKeys(Keys.SPACE);
                    } else if (CodeSendKeyTypeEnum.DELETE.equals(codeSendKeyType)) {
                        element.sendKeys(Keys.DELETE);
                    } else if (CodeSendKeyTypeEnum.ESCAPE.equals(codeSendKeyType)) {
                        element.sendKeys(Keys.ESCAPE);
                    } else if (CodeSendKeyTypeEnum.BACK_SPACE.equals(codeSendKeyType)) {
                        element.sendKeys(Keys.BACK_SPACE);
                    }
                    break;
                case SUBMIT:
                    element = getSelector(driver, codeSelectorType, codeSelectorValue);
                    element.submit();
                    break;
            }

            waitHtml(driver);
            response = MonitoringResultCodeEnum.SUCCESS.getCode();


        } catch (Exception e) {
            e.printStackTrace();
            response = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return MonActCodeResultVO
                .builder()
                .sortOrder(sortOrder + 1)
                .monCodeData(monCodeData)
                .status(status)
                .response(response)
                .build();
    }

    public WebElement getSelector(EventFiringWebDriver driver, CodeSelectorTypeEnum codeSelectorType, String selector_value) {
        switch (codeSelectorType) {
            case ID:
                return driver.findElement(By.id(selector_value));
            case CSS:
                return driver.findElement(By.cssSelector(selector_value));
            case TEXT:
                return driver.findElement(By.linkText(selector_value));
            case CLASS:
                return driver.findElement(By.className(selector_value));
            case NAME:
                return driver.findElement(By.name(selector_value));
            case TAG:
                return driver.findElement(By.tagName(selector_value));
            case XPATH:
                return driver.findElement(By.xpath(selector_value));
            case PARTIAL_LINK:
                return driver.findElement(By.partialLinkText(selector_value));
        }
        return null;
    }


    public void waitHtml(EventFiringWebDriver driver) throws InterruptedException {

        while (0 < 1) {
            Thread.sleep(100);
            JavascriptExecutor js = driver;
            if (js.executeScript("return document.readyState").toString().equals("complete")) {
                break;
            }
        }
    }

}
