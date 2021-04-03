package com.minimon.entity;

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
@Table(name = "LOG_HISTORY")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LogHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "고유 번호")
    private int seq;

    @NotNull
    @ApiModelProperty(name = "메소드")
    private String httpMethod;

    @NotNull
    @ApiModelProperty(name = "URI")
    private String uri;

    @NotNull
    @ApiModelProperty(name = "접근 CLASS")
    private String className;

    @NotNull
    @ApiModelProperty(name = "접근 METHOD")
    private String methodName;

    @NotNull
    @ApiModelProperty(name = "걸린 시간(MS)")
    private long progressTime;

    @Lob
    @NotNull
    @ApiModelProperty(name = "요청 파라미터")
    private String params;

    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "등록일", hidden = true)
    private LocalDateTime regDate;

    @Builder
    public LogHistory(String httpMethod, String uri, String className, String methodName, long progressTime, String params) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.className = className;
        this.methodName = methodName;
        this.progressTime = progressTime;
        this.params = params;
    }
}
