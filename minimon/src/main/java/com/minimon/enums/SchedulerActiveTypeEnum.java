package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchedulerActiveTypeEnum {
    CRON("CRON", "고정 시간"),
    DELAY("DELAY", "반복");

    private String code;
    private String value;
}
