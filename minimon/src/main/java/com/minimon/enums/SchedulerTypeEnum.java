package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchedulerTypeEnum {
    URL_MONITORING("URL_MONITORING", "URL 모니터링", "CRON", "0 0/5 * * * *"),
    API_MONITORING("API_MONITORING", "API 모니터링", "CRON", "0 0/5 * * * *"),
    ACT_MONITORING("ACT_MONITORING", "ACT 모니터링", "CRON", "0 0/5 * * * *");

    private String code;
    private String value;
    private String activeType;
    private String time;
}
