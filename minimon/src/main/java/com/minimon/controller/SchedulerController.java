package com.minimon.controller;

import com.minimon.common.CommonResponse;
import com.minimon.service.CustomSchedulerService;
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
    private final CustomSchedulerService customSchedulerService;

    @PostMapping(path = "/run")
    @ApiOperation(value = "특정 스케줄 작업 실행")
    public CommonResponse run(@RequestParam String schedulerType) {
        return new CommonResponse(customSchedulerService.run(schedulerType));
    }

    @PutMapping(path = "/run")
    @ApiOperation(value = "특정 스케줄 작업 실행 -> 설정 값 변경")
    public CommonResponse run(@RequestBody SchedulerVO customExecutorVO) {
        return new CommonResponse(customSchedulerService.run(customExecutorVO));
    }

    @PostMapping(path = "/stop")
    @ApiOperation(value = "특정 스케줄 작업 중지")
    public CommonResponse stop(@RequestParam String schedulerType) {
        return new CommonResponse(customSchedulerService.stop(schedulerType));
    }

    @GetMapping(path = "/running/scheduler/list")
    @ApiOperation(value = "활성화 스케줄 작업 목록 조회")
    public CommonResponse getRunningScheduler() {
        return new CommonResponse(customSchedulerService.getRunningScheduler());
    }

    @PostMapping(path = "/run/all")
    @ApiOperation(value = "모든 스케줄 작업 실행")
    public CommonResponse runAllTasks() {
        return new CommonResponse(customSchedulerService.runAllTasks());
    }

    @PostMapping(path = "/stop/all")
    @ApiOperation(value = "모든 스케줄 작업 중지")
    public CommonResponse stopAllTasks() {
        return new CommonResponse(customSchedulerService.stopAllTasks());
    }
}
