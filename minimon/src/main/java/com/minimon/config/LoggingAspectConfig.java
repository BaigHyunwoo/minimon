package com.minimon.config;

import com.google.common.base.Joiner;
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
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Aspect
@Slf4j
public class LoggingAspectConfig {

    @Pointcut("within(com.minimon.controller..*)")
    public void onRequest() {
    }

    @Around("com.minimon.config.LoggingAspectConfig.onRequest()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String httpMethod = request.getMethod();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = paramMap.isEmpty() ? "" : paramMapToString(paramMap);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        log.info(httpMethod + " " + request.getRequestURI() + " " + method.getDeclaringClass() + " - " + method.getName() + " : " + stopWatch.getTotalTimeMillis() + "MS - " + params);
        return proceed;
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
