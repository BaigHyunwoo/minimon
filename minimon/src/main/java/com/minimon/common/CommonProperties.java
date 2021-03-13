package com.minimon.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Setter
@ConstructorBinding
@ConfigurationProperties("common")
public class CommonProperties {

    private String location;

    private String driverName;

    private String driverPath;
}
