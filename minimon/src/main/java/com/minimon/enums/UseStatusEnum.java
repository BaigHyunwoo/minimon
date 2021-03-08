package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UseStatusEnum {
    Y("Y", "사용"),
    N("N", "사용안함");

    private String code;
    private String value;
}
