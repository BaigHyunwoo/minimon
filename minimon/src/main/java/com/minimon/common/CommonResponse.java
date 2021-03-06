package com.minimon.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.minimon.enums.ResponseEnum;
import lombok.Getter;
import org.springframework.cache.support.NullValue;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {
    private CommonResponseMeta meta;
    private Object data;

    public static CommonResponse fail(String errorMessage) {
        return new CommonResponse(ResponseEnum.FAIL.getCode(), errorMessage);
    }

    public static CommonResponse fail(int code, String errorMessage) {
        return new CommonResponse(code, errorMessage);
    }

    public static CommonResponse fail(String code, String errorMessage) {
        return new CommonResponse(Integer.valueOf(code), errorMessage);
    }

    public static CommonResponse notExistResponse() {
        return fail(CommonMessage.getMessage("notExist.code"), CommonMessage.getMessage("notExist.msg"));
    }

    public static CommonResponse preparingFunctionResponse() {
        return fail(CommonMessage.getMessage("preparingFunction.code"), CommonMessage.getMessage("preparingFunction.msg"));
    }

    public CommonResponse() {
        setSuccess();
        this.data = new String();
    }

    public CommonResponse(Object result) {
        setSuccess();
        this.data = result;
    }

    public CommonResponse(int code, String message) {
        this.meta = new CommonResponseMeta(code, message);
        this.data = new String();
    }

    private void setSuccess() {
        this.meta = new CommonResponseMeta(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getValue());
    }
}
