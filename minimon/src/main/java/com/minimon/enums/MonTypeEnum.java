package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonTypeEnum {
    URL("URL", "웹 사이트"),
    API("API", "API 호출"),
    TRANSACTION("TRANSACTION", "기능 동작");

    private String code;
    private String value;
}
