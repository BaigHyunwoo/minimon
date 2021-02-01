package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@ApiModel(value = "URL 검사 VO")
public class MonUrlCheckVO {

    @ApiModelProperty(value = "검사 경로")
    private String url;

    @ApiModelProperty(value = "반복 설정 시간")
    private int timeout;

    @Builder
    public MonUrlCheckVO(String url, int timeout) {
        this.url = url;
        this.timeout = timeout;
    }
}
