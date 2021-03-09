package com.minimon.common;

import com.minimon.vo.MonitoringResultVO;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


@Slf4j
@Component
public class CommonSelenium {

    @Value("${common.driverName}")
    private String driverName;

    @Value("${common.driverPath}")
    private String driverPath;

    private EventFiringWebDriver driver;

    private Map<String, Object> vars;

    JavascriptExecutor js;

    public class WebDriverEventListenerClass extends AbstractWebDriverEventListener {

        long startTime, endTime;

        public void beforeNavigateTo(String arg0, WebDriver arg1) {
            startTime = System.currentTimeMillis();
        }

        public void afterNavigateTo(String arg0, WebDriver arg1) {
            endTime = System.currentTimeMillis();
        }

        public int returnLoadTime() {
            return new Long(endTime - startTime).intValue();
        }
    }

    public String waitForWindow(int timeout) {

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
            System.setProperty(driverName, driverPath + File.separator + "chromedriver.exe");

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

        } catch (Exception e) {
            if (driver != null) driver.quit();
        }

        return driver;
    }


    public int connect(String url, EventFiringWebDriver driver, int timeout) {
        driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
        int totalLoadTime = -1;

        try {

            WebDriverEventListenerClass event = new WebDriverEventListenerClass();
            driver.register(event);
            driver.navigate().to(url);
            totalLoadTime = event.returnLoadTime();

        } catch (TimeoutException e1) {
            log.info("Error - Timeout");

        } catch (Exception e) {
            totalLoadTime = -2;
            log.info("Error - Unknown");
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

    public JSONObject getResourceMessage(LogEntry entry) {
        JSONObject json = new JSONObject(entry.getMessage());
        return json.getJSONObject("message");
    }

    public MonitoringResultVO getResult(LogEntries logs, String currentURL, int totalLoadTime) {

        HttpStatus resultStatus = HttpStatus.BAD_GATEWAY;
        for (Iterator<LogEntry> it = logs.iterator(); it.hasNext(); ) {
            Optional<HttpStatus> isExistStatus = Optional.ofNullable(getResultStatus(getResourceMessage(it.next()), currentURL));
            if(isExistStatus.isPresent()) {
                resultStatus = isExistStatus.get();

            }
        }

        log.debug("WebDriver - Log 분석 완료");
        return MonitoringResultVO.builder()
                .totalLoadTime(totalLoadTime)
                .url(currentURL)
                .status(resultStatus)
                .build();
    }


    public HttpStatus getResultStatus(JSONObject message, String currentURL) {

        /*
         * DATA CONVERT & CHECK
         */
        String methodName = message.getString("method");

        /*
         * Log Exists Check
         */
        if (methodName != null && methodName.equals("Network.responseReceived") == false) {
            return null;
        }

        /*
         * GET DATA
         */
        JSONObject params = message.getJSONObject("params");

        JSONObject response = params.getJSONObject("response");

        JSONObject headersObj = response.getJSONObject("headers");

        Map<String, Object> headers = new CaseInsensitiveMap<>();

        headers.putAll(headersObj.toMap());

        /*
         * SET STATUS
         */
        if (currentURL.equals(response.get("url")) == true) {
            return HttpStatus.valueOf(response.getInt("status"));
        }

        return null;
    }


    public String executeAction(EventFiringWebDriver driver, String action, String selector_type, String selector_value, String value) {
        String result;

        WebElement element;

        try {

            if (action.equals("get") == true) {

                driver.get(value);

            } else if (action.equals("size") == true) {

                driver.manage().window().maximize();

            } else if (action.equals("window_handles") == true) {

                vars.put("window_handles", driver.getWindowHandles());

            } else if (action.equals("wait") == true) {

                vars.put(selector_value, waitForWindow(5000));

            } else if (action.equals("switch") == true) {

                if (selector_value != null) driver.switchTo().frame(selector_value).toString();
                else driver.switchTo().defaultContent();

            } else if (action.equals("click") == true) {

                element = getSelector(driver, selector_type, selector_value);
                element.click();

            } else if (action.equals("sendKeys") == true) {

                element = getSelector(driver, selector_type, selector_value);

                if (value.equals("Keys.ENTER") == true) {

                    element.sendKeys(Keys.ENTER);

                } else if (value.equals("Keys.BACK_SPACE") == true) {

                    element.sendKeys(Keys.BACK_SPACE);

                } else if (value.equals("Keys.DELETE") == true) {

                    element.sendKeys(Keys.DELETE);

                } else if (value.equals("Keys.SPACE") == true) {

                    element.sendKeys(Keys.SPACE);

                } else if (value.equals("Keys.ESCAPE") == true) {

                    element.sendKeys(Keys.ESCAPE);

                } else {

                    element.sendKeys(value);

                }

            } else if (action.equals("submit") == true) {

                element = getSelector(driver, selector_type, selector_value);
                element.submit();

            }

            waitHtml(driver);
            result = "SUCCESS";


        } catch (Exception e) {
            result = e.getMessage();
        }

        return result;
    }

    public WebElement getSelector(EventFiringWebDriver driver, String selector_type, String selector_value) {
        WebElement webElement = null;
        WebDriverWait wait = new WebDriverWait(driver, 30);

        if (selector_type.equals("By.id") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.id(selector_value)));

        } else if (selector_type.equals("By.cssSelector") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector_value)));

        } else if (selector_type.equals("By.linkText") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.linkText(selector_value)));

        } else if (selector_type.equals("By.className") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.className(selector_value)));

        } else if (selector_type.equals("By.name") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.name(selector_value)));

        } else if (selector_type.equals("By.tagName") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.tagName(selector_value)));

        } else if (selector_type.equals("By.xpath") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.xpath(selector_value)));

        } else if (selector_type.equals("By.partialLinkText") == true) {

            webElement = (WebElement) wait.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(selector_value)));

        }

        return webElement;
    }


    public void waitHtml(EventFiringWebDriver driver) throws InterruptedException {

        while (0 < 1) {
            Thread.sleep(100);
            JavascriptExecutor js = driver;
            if (js.executeScript("return document.readyState").toString().equals("complete") == true) {
                break;
            }
        }
    }
}
