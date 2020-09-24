package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpRequestTypeEnum {
    GET("GET", "GET"),
    POST("POST", "POST"),
    PUT("PUT", "PUT"),
    DELETE("DELETE", "DELETE");

    private String code;
    private String value;
}
