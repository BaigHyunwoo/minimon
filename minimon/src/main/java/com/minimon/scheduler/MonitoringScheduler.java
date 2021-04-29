package com.minimon.scheduler;

import com.minimon.entity.MonAct;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonUrl;
import com.minimon.enums.MonitoringTypeEnum;
import com.minimon.service.MonActService;
import com.minimon.service.MonApiService;
import com.minimon.service.MonUrlService;
import com.minimon.service.MonitoringService;
import com.minimon.vo.MonitoringTaskVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringScheduler {
    private final MonUrlService monUrlService;
    private final MonApiService monApiService;
    private final MonActService monActService;
    private final MonitoringService monitoringService;

    public int urlMonitoring() {
        List<MonUrl> monUrlList = monUrlService.findScheduledList();
        monUrlList.forEach(monUrl -> monitoringService.addTask(MonitoringTaskVO.builder()
                .monitoringType(MonitoringTypeEnum.URL)
                .seq(monUrl.getSeq())
                .task(monUrlService.executeTask(monUrl.getSeq()))
                .build()));
        return monUrlList.size();
    }

    public int apiMonitoring() {
        List<MonApi> monApiList = monApiService.findScheduledList();
        monApiList.forEach(monApi -> monitoringService.addTask(MonitoringTaskVO.builder()
                .monitoringType(MonitoringTypeEnum.API)
                .seq(monApi.getSeq())
                .task(monApiService.executeTask(monApi.getSeq()))
                .build()));
        return monApiList.size();
    }

    public int actMonitoring() {
        List<MonAct> monActList = monActService.findScheduledList();
        monActList.forEach(monAct -> monitoringService.addTask(MonitoringTaskVO.builder()
                .monitoringType(MonitoringTypeEnum.ACT)
                .seq(monAct.getSeq())
                .task(monActService.executeTask(monAct.getSeq()))
                .build()));
        return monActList.size();
    }
}
