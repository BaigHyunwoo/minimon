package com.minimon.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectErrorCodeEnum {
    TIMEOUT("TIMEOUT", -1),
    UNKNOWN("UNKNOWN", -2);

    private String code;
    private int value;
}
