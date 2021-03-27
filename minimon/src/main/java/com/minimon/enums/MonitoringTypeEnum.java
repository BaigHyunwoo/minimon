package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonitoringTypeEnum {
    URL("URL", "웹 사이트"),
    API("API", "API 호출"),
    ACT("ACT", "기능 동작");

    private String code;
    private String value;
}
