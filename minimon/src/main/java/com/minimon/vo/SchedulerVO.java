package com.minimon.vo;

import com.minimon.enums.SchedulerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@ApiModel(value = "실행 정보")
public class SchedulerVO {

    @ApiModelProperty(value = "실행 종류")
    private SchedulerTypeEnum schedulerType;

    @ApiModelProperty(value = "반복 설정 종류")
    private String activeType;

    @ApiModelProperty(value = "반복 설정 시간")
    private String time;

    @Builder
    public SchedulerVO(SchedulerTypeEnum schedulerType, String activeType, String time) {
        this.schedulerType = schedulerType;
        this.activeType = activeType;
        this.time = time;
    }
}
