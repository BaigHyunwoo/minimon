package com.minimon.entity;

import com.minimon.enums.UseStatusEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "MON_ACT")
@NoArgsConstructor
public class MonAct extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "고유 번호")
    private int seq;

    @NotNull
    @ApiModelProperty(name = "제목")
    private String title;

    @NotNull
    @ApiModelProperty(name = "타임아웃 시간")
    private int timeout;

    @NotNull
    @ApiModelProperty(name = "지연 시간 한계")
    private int errorLoadTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "사용 여부")
    private UseStatusEnum monitoringUseYn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(1)")
    @ApiModelProperty(name = "지연 시간 검사 여부")
    private UseStatusEnum loadTimeCheckYn;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "mon_act_seq")
    @ApiModelProperty(name = "검사 코드")
    private List<MonCodeData> codeDataList;

    @NotNull
    @ApiModelProperty(name = "응답 코드")
    private int status;

    @NotNull
    @ApiModelProperty(name = "걸린 시간")
    private int loadTime;

    @Builder
    public MonAct(String title, int timeout, int errorLoadTime, List<MonCodeData> codeDataList, int loadTime) {
        this.title = title;
        this.timeout = timeout;
        this.errorLoadTime = errorLoadTime;
        this.loadTime = loadTime;
        this.codeDataList = codeDataList;
        this.monitoringUseYn = UseStatusEnum.Y;
        this.loadTimeCheckYn = UseStatusEnum.Y;
        this.status = HttpStatus.OK.value();
    }
}
