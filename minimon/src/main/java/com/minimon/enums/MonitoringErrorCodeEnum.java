package com.minimon.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonitoringErrorCodeEnum {
    TIMEOUT("TIMEOUT", "연결 시간 초과"),
    LOAD_TIME("LOAD_TIME", "시간"),
    PAYLOAD("PAYLOAD", "용량"),
    TEXT("TEXT", "검색어"),
    SUCCESS("SUCCESS", "성공");

    private String code;
    private String value;
}
