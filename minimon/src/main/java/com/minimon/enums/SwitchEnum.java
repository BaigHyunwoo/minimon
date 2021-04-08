package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwitchEnum {
    ON("ON", "사용"),
    OFF("OFF", "사용 중지");

    private String code;
    private String value;
}
