package com.minimon.entity;

import com.minimon.enums.UseStatusEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "MON_URL")
public class MonUrl extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "고유 번호")
    private int seq;

    @NotNull
    @ApiModelProperty(name = "URL 경로")
    private String url;

    @NotNull
    @ApiModelProperty(name = "제목")
    private String title;

    @NotNull
    @ApiModelProperty(name = "타임 아웃 시간")
    private int timeout;

    @NotNull
    @ApiModelProperty(name = "지연 한계 시간")
    private int errorLoadTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "모니터링 사용 여부")
    private UseStatusEnum monitoringUseYn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "지연시간 검사 여부")
    private UseStatusEnum loadTimeCheckYn;

    @NotNull
    @ApiModelProperty(name = "응답 코드")
    private int status;

    @NotNull
    @ApiModelProperty(name = "걸린 시간")
    private int loadTime;
}
