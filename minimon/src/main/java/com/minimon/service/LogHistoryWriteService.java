package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.LogHistory;
import com.minimon.entity.MonResult;
import com.minimon.enums.UseStatusEnum;
import com.minimon.exception.UndefinedResultReceiveException;
import com.minimon.repository.LogHistoryRepository;
import com.minimon.repository.MonResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

}
