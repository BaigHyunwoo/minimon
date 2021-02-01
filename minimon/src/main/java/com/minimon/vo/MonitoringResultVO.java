package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "모니터링 실행 결과 VO")
public class MonitoringResultVO {

    @ApiModelProperty(value = "실행 URL")
    private String url;

    @ApiModelProperty(value = "결과 코드")
    private int status;

    @ApiModelProperty(value = "용량")
    private int totalPayLoad;

    @ApiModelProperty(value = "걸린 시간")
    private int totalLoadTime;

    @Builder
    public MonitoringResultVO(String url, int status, int totalPayLoad, int totalLoadTime) {
        this.url = url;
        this.status = status;
        this.totalPayLoad = totalPayLoad;
        this.totalLoadTime = totalLoadTime;
    }
}
