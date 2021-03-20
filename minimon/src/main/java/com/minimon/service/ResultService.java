package com.minimon.service;

import com.minimon.common.CommonProperties;
import com.minimon.common.CommonRestTemplate;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonResult;
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
public class ResultService {
    private final CommonRestTemplate commonRestTemplate;
    private final MonResultRepository monResultRepository;
    private final CommonProperties commonProperties;

    public Page getList(CommonSearchSpec commonSearchSpec) {
        return monResultRepository.findAll(commonSearchSpec.searchSpecs(), commonSearchSpec.pageRequest());
    }

    public Optional<MonResult> get(int seq) {
        return monResultRepository.findById(seq);
    }

    @Transactional
    public MonResult save(MonResult monResult) {
        monResultRepository.save(monResult);
        return monResult;
    }

    public String sendResultByProperties(MonResult monResult) {
        String response = null;

        try {
            response = commonRestTemplate.callApi(HttpMethod.POST, commonProperties.getLocation(), monResult);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SEND RESULT ERROR");
        }

        return response;
    }
}
