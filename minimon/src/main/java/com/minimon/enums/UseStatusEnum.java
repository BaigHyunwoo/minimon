package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UseStatusEnum {
    USE("Y", "사용"),
    UNUSED("N", "사용안함");

    private String code;
    private String value;
}
