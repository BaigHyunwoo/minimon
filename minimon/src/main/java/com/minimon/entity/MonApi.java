package com.minimon.entity;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "MON_API")
public class MonApi extends CommonEntity {

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
    @ApiModelProperty(name = "반복 시간")
    private int timer;

    @NotNull
    @ApiModelProperty(name = "타임아웃 시간")
    private int timeout;

    @NotNull
    @ApiModelProperty(name = "지연 시간 한계")
    private int errLoadTime;

    @NotNull
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "사용 여부")
    private String monitoringUseYn;

    @NotNull
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "지연 시간 검사 여부")
    private String loadTimeCheckYn;

    @NotNull
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "응답 데이터 검사 여부")
    private String responseCheckYn;

    @NotNull
    @ApiModelProperty(name = "사용 http 메소드")
    private String method;

    @ApiModelProperty(name = "요청 Body")
    private String data;

    @NotNull
    @ApiModelProperty(name = "응답 코드")
    private int status;

    @NotNull
    @ApiModelProperty(name = "걸린 시간")
    private int loadTime;

    @Lob
    @ApiModelProperty(name = "응답 데이터")
    private String response;

}
