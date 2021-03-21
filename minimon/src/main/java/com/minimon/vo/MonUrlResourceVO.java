package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@ApiModel(value = "URL 검사 결과 리소스 정보 VO")
public class MonUrlResourceVO {

    @ApiModelProperty(value = "걸린 시간")
    private double loadTime;

    @ApiModelProperty(value = "용량")
    private double payLoad;

    @ApiModelProperty(value = "리소스 종류")
    private String type;

    @ApiModelProperty(value = "응답 결과 코드")
    private int status;

    @ApiModelProperty(value = "리소스 경로")
    private String url;

    @ApiModelProperty(value = "리소스 순서")
    private int sortOrder;

    @Builder
    public MonUrlResourceVO(String url, String type, int status, double payLoad, int sortOrder, double loadTime) {
        this.url = url;
        this.type = type;
        this.status = status;
        this.payLoad = payLoad;
        this.sortOrder = sortOrder;
        this.loadTime = loadTime;
    }
}
