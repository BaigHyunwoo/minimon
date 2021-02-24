package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.scheduler.CustomScheduler;
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

    @PostMapping(path = "/run")
    @ApiOperation(value = "특정 스케줄 작업 실행")
    public CommonResponse run(@RequestParam String schedulerType) {
        return new CommonResponse(customScheduler.run(schedulerType));
    }

    @PostMapping(path = "/stop")
    @ApiOperation(value = "특정 스케줄 작업 중지")
    public CommonResponse stop(@RequestParam String schedulerType) {
        return new CommonResponse(customScheduler.stop(schedulerType));
    }

    @GetMapping(path = "/running/scheduler/list")
    @ApiOperation(value = "활성화 스케줄 작업 목록 조회")
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
}
