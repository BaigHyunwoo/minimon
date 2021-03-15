package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(value = "API 검사 VO")
@NoArgsConstructor
public class MonApiCheckVO {

    @ApiModelProperty(value = "검사 Http method")
    private String method;

    @ApiModelProperty(value = "검사 경로")
    private String url;

    @ApiModelProperty(value = "검사 데이터")
    private String data;

    @Builder
    public MonApiCheckVO(String method, String url, String data) {
        this.method = method;
        this.url = url;
        this.data = data;
    }
}
