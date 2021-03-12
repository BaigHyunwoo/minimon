package com.minimon.vo;

import com.minimon.entity.MonCodeData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@ApiModel(value = "ACT 모니터링 코드 실행 결과 VO")
public class MonActCodeResultVO {

    @ApiModelProperty(value = "순서")
    private int sortOrder;

    @ApiModelProperty(value = "실행 코드")
    private MonCodeData monCodeData;

    @ApiModelProperty(value = "결과 코드")
    private HttpStatus status;

    @ApiModelProperty(value = "응답")
    private String response;

    @Builder
    public MonActCodeResultVO(int sortOrder, MonCodeData monCodeData, HttpStatus status, String response) {
        this.sortOrder = sortOrder;
        this.monCodeData = monCodeData;
        this.status = status;
        this.response = response;
    }
}
