package com.minimon.entity;

import com.minimon.enums.MonitoringResultCodeEnum;
import com.minimon.enums.MonitoringTypeEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MON_RESULT")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class MonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "고유 번호")
    private int seq;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "모니터링 타입")
    private MonitoringTypeEnum monitoringType;

    @NotNull
    @ApiModelProperty(name = "매핑 SEQ")
    private int relationSeq;

    @NotNull
    @ApiModelProperty(name = "제목")
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "응답 결과 코드")
    private MonitoringResultCodeEnum resultCode;

    @ApiModelProperty(name = "응답 결과 body")
    private String response;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "응답 상태 코드")
    private HttpStatus status;

    @ApiModelProperty(name = "응답 지연시간")
    private double loadTime;

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

    @Builder
    public MonResult(MonitoringTypeEnum monitoringTypeEnum, int relationSeq, String title, MonitoringResultCodeEnum resultCode, double loadTime) {
        this.monitoringType = monitoringTypeEnum;
        this.relationSeq = relationSeq;
        this.title = title;
        this.resultCode = resultCode;
        this.loadTime = loadTime;
    }
}
