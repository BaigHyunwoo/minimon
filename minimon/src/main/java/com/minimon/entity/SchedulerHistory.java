package com.minimon.entity;

import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.enums.ResponseEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SCHEDULER_HISTORY")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SchedulerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "고유 번호")
    private int seq;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "모니터링 종류")
    private MonitoringTypeEnum monitoringType;

    @NotNull
    @ApiModelProperty(name = "걸린 시간(MS)")
    private long progressTime;

    @NotNull
    @ApiModelProperty(name = "처리 갯수")
    private int progressCount;

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

    @Builder
    public SchedulerHistory(MonitoringTypeEnum monitoringType, long progressTime, int progressCount){
        this.monitoringType = monitoringType;
        this.progressTime = progressTime;
        this.progressCount = progressCount;
    }
}
