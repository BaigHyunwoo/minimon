package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@ApiModel(value = "모니터링 실행 결과 VO")
public class MonitoringResultVO {

    @ApiModelProperty(value = "실행 URL")
    private String url;

    @Setter
    @ApiModelProperty(value = "결과 코드")
    private HttpStatus status;

    @ApiModelProperty(value = "걸린 시간")
    private int totalLoadTime;

    @ApiModelProperty(value = "응답")
    private Object response;

    @Builder
    public MonitoringResultVO(String url, int totalLoadTime, HttpStatus status, Object response) {
        this.url = url;
        this.totalLoadTime = totalLoadTime;
        this.status = status;
        this.response = response;
    }
}
