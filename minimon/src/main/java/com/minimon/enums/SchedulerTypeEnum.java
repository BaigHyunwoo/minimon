package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchedulerTypeEnum {
    MONITORING("MONITORING", "모니터링", "CRON", "0 0/5 * * * *");

    private String code;
    private String value;
    private String activeType;
    private String time;
}
