package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "실행 정보", description = "")
public class SchedulerVO {

    @ApiModelProperty(value = "실행 종류")
    private String schedulerType;

    @ApiModelProperty(value = "반복 설정 종류")
    private String activeType;

    @ApiModelProperty(value = "반복 설정 시간")
    private String time = "0";

    @Builder
    public SchedulerVO(String schedulerType, String activeType, String time) {
        this.schedulerType = schedulerType;
        this.activeType = activeType;
        this.time = time;
    }
}
