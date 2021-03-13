package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "SUCCESS"),
    FAIL(500, "FAIL");

    private int code;
    private String value;
}
