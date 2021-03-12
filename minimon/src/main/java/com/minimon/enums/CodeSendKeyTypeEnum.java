package com.minimon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeSendKeyTypeEnum {
    ENTER("Keys.ENTER", "ENTER"),
    BACK_SPACE("Keys.BACK_SPACE", "BACK_SPACE"),
    DELETE("Keys.DELETE", "DELETE"),
    SPACE("Keys.SPACE", "SPACE"),
    ESCAPE("Keys.ESCAPE", "ESCAPE");

    private String code;
    private String value;

    public static Object codeOf(String code){
        for (CodeSendKeyTypeEnum codeSendKeyTypeEnum : CodeSendKeyTypeEnum.values()) {
            if(codeSendKeyTypeEnum.getCode().equals(code)){
                return codeSendKeyTypeEnum;
            }
        }
        return null;
    }
}
