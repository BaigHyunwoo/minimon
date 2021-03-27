package com.minimon.vo;

import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ApiModel(value = "실행 스케줄러 VO")
public class RunningSchedulerVO {

    @ApiModelProperty(value = "실행 타입")
    private int currentThreadSize;

    @ApiModelProperty(value = "실행 상태")
    private int totalTaskSize;

    @ApiModelProperty(value = "작업 목록")
    private Map<SchedulerTypeEnum, SchedulerTaskVO> scheduledTasks = new HashMap<>();

    @ApiModelProperty(value = "실행 작업 수")
    public long getRunningTaskCount() {
        return this.scheduledTasks.entrySet()
                .stream()
                .filter(scheduledTask -> scheduledTask.getValue().getStatus().equals(SchedulerStatusEnum.RUNNING))
                .count();
    }

    public void setScheduledTasks(SchedulerTypeEnum schedulerTypeEnum, SchedulerTaskVO schedulerTaskVO) {
        this.scheduledTasks.put(schedulerTypeEnum, schedulerTaskVO);
    }
}
