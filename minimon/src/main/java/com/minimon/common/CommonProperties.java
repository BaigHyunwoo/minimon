package com.minimon.common;

import com.minimon.exception.KillDriverProcessException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.IOException;

@Slf4j
@Getter
@Setter
@ConstructorBinding
@ConfigurationProperties("common")
public class CommonProperties implements InitializingBean {

    private String resultReceivePath;

    private String driverName;

    private String driverPath;

    private String driverFileName;

    private String driverVersion;

    private String driverFileDownloadPath;


    @Override
    public void afterPropertiesSet() {
        killDriver();
        setDriverVersion(this.driverPath);
    }

    public void killDriver() {
        try {

            Process process = Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            process.waitFor();
            log.info("KILL DRIVER : " + !process.isAlive());

        } catch (IOException | InterruptedException e) {
            throw new KillDriverProcessException(e);
        }
    }

    public void setDriverVersion(String driverPath) {
        ChromeDriver driver = null;
        try {

            System.setProperty(this.driverName, driverPath + this.driverFileName);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("w3c", false);
            options.addArguments("headless");
            driver = new ChromeDriver(options);
            Capabilities capabilities = driver.getCapabilities();
            this.driverVersion = capabilities.getVersion();

        } catch (SessionNotCreatedException SE) {

        } finally {
            if (driver != null) driver.quit();
        }
    }
}
