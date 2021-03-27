package com.minimon.vo;

import com.minimon.enums.SchedulerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
@ApiModel(value = "실행 스케줄러 VO")
public class RunningSchedulerVO {

    @ApiModelProperty(value = "실행 타입")
    private int currentThreadSize;

    @ApiModelProperty(value = "실행 상태")
    private int totalTaskSize;

    @ApiModelProperty(value = "실행 작업")
    private Map<SchedulerTypeEnum, SchedulerTaskVO> scheduledTasks = new HashMap<>();

    public void setScheduledTasks(SchedulerTypeEnum schedulerTypeEnum, SchedulerTaskVO schedulerTaskVO){
        this.scheduledTasks.put(schedulerTypeEnum, schedulerTaskVO);
    }
}
