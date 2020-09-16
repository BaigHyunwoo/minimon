package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchedulerStatusEnum {
    RUNNING("RUNNING", "실행"),
    STOP("STOP", "중지");

    private String code;
    private String value;
}
