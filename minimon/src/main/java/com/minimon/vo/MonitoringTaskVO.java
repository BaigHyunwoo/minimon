package com.minimon.vo;

import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
@ApiModel(value = "모니터링 태스크 VO")
public class MonitoringTaskVO {

    @ApiModelProperty(value = "타입")
    private MonitoringTypeEnum monitoringType;

    @ApiModelProperty(value = "시퀀스")
    private int seq;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "실행 작업")
    private Runnable task;
}
