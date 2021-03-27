package com.minimon.vo;

import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
@ApiModel(value = "스케줄러 태스크 VO")
public class SchedulerTaskVO {

    @ApiModelProperty(value = "실행 타입")
    private SchedulerTypeEnum schedulerType;

    @ApiModelProperty(value = "실행 상태")
    private SchedulerStatusEnum status;

    @ApiModelProperty(value = "실행 작업")
    private ScheduledFuture scheduler;
}
