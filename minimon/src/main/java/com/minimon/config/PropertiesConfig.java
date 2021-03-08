package com.minimon.config;

import com.minimon.common.CommonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = CommonProperties.class)
public class PropertiesConfig {
}
