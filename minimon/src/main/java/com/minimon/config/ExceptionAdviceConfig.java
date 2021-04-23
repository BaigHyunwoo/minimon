package com.minimon.config;

import com.minimon.common.CommonMessage;
import com.minimon.common.CommonResponse;
import com.minimon.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
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

    @ExceptionHandler(DriverVersionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse driverVersionException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("driverVersionNotMatch.code"), CommonMessage.getMessage("driverVersionNotMatch.msg"));
    }

    @ExceptionHandler(KillDriverProcessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse driverUploadException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("killDriverProcess.code"), CommonMessage.getMessage("killDriverProcess.msg"));
    }

    @ExceptionHandler(WebDriverException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse webDriverException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("webDriverError.code"), CommonMessage.getMessage("webDriverError.msg"));
    }

    @ExceptionHandler(ResponseParsingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse responseParseException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("responseParsingError.code"), CommonMessage.getMessage("responseParsingError.msg"));
    }

    @ExceptionHandler(SearchQueryParsingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResponse searchQueryParsingException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return CommonResponse.fail(CommonMessage.getMessage("searchQueryParsingError.code"), CommonMessage.getMessage("searchQueryParsingError.msg"));
    }

    @ExceptionHandler(MonitoringExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void monitoringExecutionException(HttpServletRequest request, Exception e) {
        log.error(CommonMessage.getMessage("monitoringExecutionError.code"), CommonMessage.getMessage("monitoringExecutionError.msg"));
        log.error(e.getMessage());
    }

    @ExceptionHandler(ActFileConvertException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void actFileConvertException(HttpServletRequest request, Exception e) {
        log.error(CommonMessage.getMessage("actFileConvertError.code"), CommonMessage.getMessage("actFileConvertError.msg"));
        log.error(e.getMessage());
    }

    @ExceptionHandler(ActTestFileDownLoadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void actTestFileDownLoadException(HttpServletRequest request, Exception e) {
        log.error(CommonMessage.getMessage("actTestFileDownLoadError.code"), CommonMessage.getMessage("actTestFileDownLoadError.msg"));
        log.error(e.getMessage());
    }
}

