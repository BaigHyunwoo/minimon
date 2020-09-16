package com.minimon.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseMeta {
    private int code;
    private String message;
    private LocalDateTime dateTime;

    public CommonResponseMeta(int code, String message) {
        setMeta(code, message);
    }

    private void setMeta(int code, String message) {
        this.code = code;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
