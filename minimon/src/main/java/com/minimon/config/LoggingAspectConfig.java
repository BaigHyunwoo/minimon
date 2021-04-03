package com.minimon.config;

import com.minimon.entity.LogHistory;
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
import java.lang.reflect.Method;

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
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        LogHistory logHistory = logHistoryWriteService.save(request, method, stopWatch.getTotalTimeMillis());
        log.info(logHistory.toString());
        return proceed;
    }
}
