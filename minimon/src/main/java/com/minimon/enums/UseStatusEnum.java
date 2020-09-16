package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UseStatusEnum {
    USE("USE", "사용"),
    UNUSED("UNUSED", "사용안함");

    private String code;
    private String value;
}
