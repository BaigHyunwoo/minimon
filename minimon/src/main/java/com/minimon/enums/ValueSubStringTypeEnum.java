package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValueSubStringTypeEnum {
    FRONT("FRONT", "창 이동"),
    BACK("BACK", "사이즈 변경");


    private String code;
    private String value;
}
