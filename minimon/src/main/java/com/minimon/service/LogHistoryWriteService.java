package com.minimon.service;

import com.google.common.base.Joiner;
import com.minimon.entity.LogHistory;
import com.minimon.enums.ResponseEnum;
import com.minimon.repository.LogHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogHistoryWriteService {
    private final LogHistoryRepository logHistoryRepository;


    @Transactional
    public LogHistory save(LogHistory logHistory) {
        logHistoryRepository.save(logHistory);
        return logHistory;
    }

    public LogHistory save(HttpServletRequest request, Method method, long totalTimeMillis, ResponseEnum status, String errorName, String errorMsg) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String params = paramMap.isEmpty() ? "" : paramMapToString(paramMap);

        return save(LogHistory.builder()
                .httpMethod(request.getMethod())
                .uri(request.getRequestURI())
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .progressTime(totalTimeMillis)
                .params(params)
                .status(status)
                .errorName(errorName)
                .errorMsg(errorMsg)
                .build());
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> %s",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
