package com.minimon.config;

import com.minimon.common.CommonMessage;
import com.minimon.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdviceConfig {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse defaultException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("unKnown.code"), CommonMessage.getMessage("unKnown.msg"));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResponse missingServletRequestPartException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("badRequest.code"), CommonMessage.getMessage("badRequest.msg"));
    }
}

