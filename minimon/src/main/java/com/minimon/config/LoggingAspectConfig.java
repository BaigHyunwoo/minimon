package com.minimon.config;

import com.minimon.entity.LogHistory;
import com.minimon.enums.ResponseEnum;
import com.minimon.service.LogHistoryWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoggingAspectConfig {

    private final LogHistoryWriteService logHistoryWriteService;

    @Pointcut("within(com.minimon.controller..*)")
    public void onRequest() {
    }

    @Around("com.minimon.config.LoggingAspectConfig.onRequest()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ResponseEnum status = ResponseEnum.FAIL;
        Object proceed = null;
        String errorName = null;
        String errorMsg = null;
        Throwable throwable = null;

        StopWatch stopWatch = new StopWatch();
        try {

            stopWatch.start();
            proceed = joinPoint.proceed();
            status = ResponseEnum.SUCCESS;

        } catch (Throwable e) {
            errorName = e.toString();
            errorMsg = Arrays.stream(e.getStackTrace())
                    .map(stackTraceElement -> stackTraceElement.toString())
                    .collect(Collectors.joining("\n"));
        } finally {
            stopWatch.stop();
            LogHistory logHistory = logHistoryWriteService.save(request, method, stopWatch.getTotalTimeMillis(), status, errorName, errorMsg);
            log.info(logHistory.toString());

            if(status.equals(ResponseEnum.FAIL)){
                log.error(errorName);
                log.error(errorMsg);
            }
        }
        return proceed;
    }
}
