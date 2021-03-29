package com.minimon.common;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

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


    @Override
    public void afterPropertiesSet() {
        setDriverVersion();
    }

    public void setDriverVersion() {
        ChromeDriver driver = null;
        try {

            System.setProperty(this.driverName, this.driverPath + this.driverFileName);
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
