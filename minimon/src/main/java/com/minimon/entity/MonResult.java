package com.minimon.entity;

import com.minimon.enums.MonitoringTypeEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MON_RESULT")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class MonResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int seq;

    @NotNull
    private String relationType;

    @NotNull
    private int relationSeq;

    @NotNull
    private String title;

    @NotNull
    private String result;

    private String response;

    private int status;

    private double loadTime;

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

    @Builder
    public MonResult(MonitoringTypeEnum monitoringTypeEnum, int relationSeq, String title, String result, double loadTime) {
        this.relationType = monitoringTypeEnum.getCode();
        this.relationSeq = relationSeq;
        this.title = title;
        this.result = result;
        this.loadTime = loadTime;
    }
}
