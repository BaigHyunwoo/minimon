package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.enums.SchedulerTypeEnum;
import com.minimon.scheduler.CustomScheduler;
import com.minimon.service.MonitoringService;
import com.minimon.vo.RunningSchedulerVO;
import com.minimon.vo.SchedulerTaskVO;
import com.minimon.vo.SchedulerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
@Api(tags = {"Scheduler Controller"})
public class SchedulerController {
    private final CustomScheduler customScheduler;
    private final MonitoringService monitoringService;


    @PostMapping(path = "/execute")
    @ApiOperation(value = "특정 스케줄 작업 수동 실행 1회")
    public CommonResponse execute(@RequestParam String schedulerType) {
        return new CommonResponse(customScheduler.execute(SchedulerTypeEnum.valueOf(schedulerType)));
    }

    @PostMapping(path = "/run")
    @ApiOperation(value = "특정 스케줄 작업 실행")
    public CommonResponse run(@RequestParam String schedulerType) {
        return new CommonResponse(customScheduler.run(SchedulerTypeEnum.valueOf(schedulerType)));
    }

    @PostMapping(path = "/stop")
    @ApiOperation(value = "특정 스케줄 작업 중지")
    public CommonResponse stop(@RequestParam String schedulerType) {
        return new CommonResponse(customScheduler.stop(SchedulerTypeEnum.valueOf(schedulerType)));
    }

    @GetMapping(path = "/running/scheduler/list")
    @ApiOperation(value = "활성화 스케줄 작업 목록 조회", response = RunningSchedulerVO.class)
    public CommonResponse getRunningScheduler() {
        return new CommonResponse(customScheduler.getRunningScheduler());
    }

    @PostMapping(path = "/run/all")
    @ApiOperation(value = "모든 스케줄 작업 실행")
    public CommonResponse runAllTasks() {
        return new CommonResponse(customScheduler.runAllTasks());
    }

    @PostMapping(path = "/stop/all")
    @ApiOperation(value = "모든 스케줄 작업 중지")
    public CommonResponse stopAllTasks() {
        return new CommonResponse(customScheduler.stopAllTasks());
    }

    @PostMapping(path = "/monitoring/on")
    @ApiOperation(value = "모니터링 가동")
    public CommonResponse runMonitoring() {
        monitoringService.on();
        return new CommonResponse();
    }

    @PostMapping(path = "/monitoring/off")
    @ApiOperation(value = "모니터링 중지")
    public CommonResponse stopMonitoring() {
        monitoringService.off();
        return new CommonResponse();
    }
}
