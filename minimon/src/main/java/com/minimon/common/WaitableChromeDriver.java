package com.minimon.common;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;

public class WaitableChromeDriver extends ChromeDriver {
    private static final ChromeOptions OPTIONS;

    static {
        if (SystemUtils.IS_OS_WINDOWS) {
            try {
				System.setProperty("webdriver.chrome.driver", new ClassPathResource("/setting/chromedriver.exe").getFile().getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        if (SystemUtils.IS_OS_LINUX) {
            System.setProperty("webdriver.chrome.driver", "webdriver/chrome/chromedriver");
        }
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		
        OPTIONS = new ChromeOptions();
        OPTIONS.addArguments("headless");   // 브라우저를 띄우지 않고 테스트
		OPTIONS.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }


	@Override
    public WebElement findElement(By by) {
        new WebDriverWait(this, 10).until(ExpectedConditions.presenceOfElementLocated(by));
        return super.findElement(by);
    }

}
