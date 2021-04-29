package com.minimon.vo;

import com.minimon.enums.MonitoringTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "모니터링 태스크 VO")
public class MonitoringTaskVO {

    @ApiModelProperty(value = "타입")
    private MonitoringTypeEnum monitoringType;

    @ApiModelProperty(value = "시퀀스")
    private int seq;

    @ApiModelProperty(value = "실행 작업")
    private Runnable task;

    @Builder
    public MonitoringTaskVO(MonitoringTypeEnum monitoringType, int seq, Runnable task) {
        this.monitoringType = monitoringType;
        this.seq = seq;
        this.task = task;
    }
}
