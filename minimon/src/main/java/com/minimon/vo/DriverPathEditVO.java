package com.minimon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "크롬 드라이버 경로 변경 VO")
public class DriverPathEditVO {

    @ApiModelProperty(value = "크롬 드라이버 경로")
    private String driverPath;
}
