package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "결과 수신 경로 변경 VO")
public class ResultReceiveEditVO {

    @ApiModelProperty(value = "결과 수신 경로")
    private String resultReceivePath;
}
