package com.minimon.entity;

import com.minimon.enums.CodeActionEnum;
import com.minimon.enums.CodeSelectorTypeEnum;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MON_CODE_DATA")
@NoArgsConstructor
public class MonCodeData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @ApiModelProperty(name = "SEQ")
    private int seq;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "코드 행위")
    private CodeActionEnum codeAction;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(name = "코드 선택자 종류")
    private CodeSelectorTypeEnum codeSelectorType;

    @ApiModelProperty(name = "코드 선택자 값")
    private String codeSelectorValue;

    @ApiModelProperty(name = "코드 입력 값")
    private String codeValue;
}
