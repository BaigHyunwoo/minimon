package com.minimon.config;

import com.minimon.common.CommonMessage;
import com.minimon.common.CommonResponse;
import com.minimon.exception.UndefinedDriverException;
import com.minimon.exception.UndefinedResultReceiveException;
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
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("unKnown.code"), CommonMessage.getMessage("unKnown.msg"));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResponse missingServletRequestPartException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("badRequest.code"), CommonMessage.getMessage("badRequest.msg"));
    }

    @ExceptionHandler(UndefinedDriverException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse undefinedDriverException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("undefinedDriver.code"), CommonMessage.getMessage("undefinedDriver.msg"));
    }

    @ExceptionHandler(UndefinedResultReceiveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse undefinedResultReceiveException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("undefinedResultReceive.code"), CommonMessage.getMessage("undefinedResultReceive.msg"));
    }
}

