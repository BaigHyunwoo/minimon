package com.minimon.scheduler;

import com.minimon.enums.SchedulerActiveTypeEnum;
import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import com.minimon.vo.RunningSchedulerVO;
import com.minimon.vo.SchedulerTaskVO;
import com.minimon.vo.SchedulerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomScheduler implements InitializingBean {
    private final TaskScheduler scheduler;
    private final MonitoringScheduler monitoringScheduler;
    private RunningSchedulerVO runningSchedulerVO = new RunningSchedulerVO();


    /**
     * 서버 시작시 스케줄러 자동 실행
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        initRunningSchedulerVO();
        runAllTasks();
    }

    public RunningSchedulerVO getRunningScheduler() {
        initRunningSchedulerVO();
        return runningSchedulerVO;
    }

    private void initRunningSchedulerVO() {
        runningSchedulerVO.setCurrentThreadSize(Thread.activeCount());
        runningSchedulerVO.setTotalTaskSize(runningSchedulerVO.getScheduledTasks().size());

        for (SchedulerTypeEnum schedulerTypeEnum : SchedulerTypeEnum.values()) {
            SchedulerStatusEnum status = SchedulerStatusEnum.STOP;
            SchedulerTaskVO task = new SchedulerTaskVO();

            if (runningSchedulerVO.getScheduledTasks().containsKey(schedulerTypeEnum)) {
                task = runningSchedulerVO.getScheduledTasks().get(schedulerTypeEnum);
                status = task.getStatus();
            }

            task.setSchedulerType(schedulerTypeEnum);
            task.setStatus(status);
            runningSchedulerVO.setScheduledTasks(schedulerTypeEnum, task);
        }
    }

    public boolean run(SchedulerTypeEnum schedulerTypeEnum) {
        if (runningSchedulerVO.getScheduledTasks().get(schedulerTypeEnum).getStatus().equals(SchedulerStatusEnum.STOP)) {
            Optional.ofNullable(getTaskBySchedulerType(schedulerTypeEnum)).ifPresent(task -> {
                run(task, schedulerTypeEnum, schedulerTypeEnum.getActiveType(), schedulerTypeEnum.getTime());
            });
            return true;
        }
        return false;
    }

    private boolean run(SchedulerVO customExecutorVO) {
        if (runningSchedulerVO.getScheduledTasks().get(customExecutorVO.getSchedulerType()).getStatus().equals(SchedulerStatusEnum.STOP)) {
            Optional.ofNullable(getTaskBySchedulerType(customExecutorVO.getSchedulerType())).ifPresent(task -> {
                run(task, customExecutorVO.getSchedulerType(), customExecutorVO.getActiveType(), customExecutorVO.getTime());
            });
            return true;
        }
        return false;
    }

    private void run(Runnable task, SchedulerTypeEnum schedulerTypeEnum, String activeType, String time) {
        SchedulerTaskVO schedulerTaskVO = new SchedulerTaskVO();
        schedulerTaskVO.setSchedulerType(schedulerTypeEnum);
        switch (SchedulerActiveTypeEnum.valueOf(activeType)) {
            case CRON:
                schedulerTaskVO.setScheduler(scheduler.schedule(task, new CronTrigger(time)));
                break;
            case DELAY:
                schedulerTaskVO.setScheduler(scheduler.scheduleAtFixedRate(task, Integer.parseInt(time)));
                break;
        }

        schedulerTaskVO.setStatus(SchedulerStatusEnum.RUNNING);
        runningSchedulerVO.setScheduledTasks(schedulerTypeEnum, schedulerTaskVO);
        log.info("RUN TASK " + schedulerTypeEnum.getCode() + " " + activeType + " " + time);
    }

    public boolean stop(SchedulerTypeEnum schedulerTypeEnum) {
        if (Optional.ofNullable(runningSchedulerVO.getScheduledTasks().get(schedulerTypeEnum)).isPresent()) {
            SchedulerTaskVO schedulerTaskVO = runningSchedulerVO.getScheduledTasks().get(schedulerTypeEnum);
            if (schedulerTaskVO.getStatus().equals(SchedulerStatusEnum.RUNNING)) {
                ScheduledFuture scheduledFuture = schedulerTaskVO.getScheduler();
                log.info(schedulerTypeEnum.getCode() + " isCancel ? " + scheduledFuture.cancel(true));
                runningSchedulerVO.getScheduledTasks().remove(schedulerTypeEnum);
                log.info("STOP TASK " + schedulerTypeEnum.getCode());
            }
            return true;
        }
        return false;
    }

    public boolean runAllTasks() {
        Arrays.stream(SchedulerTypeEnum.values()).forEach(s ->
                run(SchedulerVO.builder()
                        .schedulerType(s)
                        .activeType(s.getActiveType())
                        .time(s.getTime())
                        .build()));
        log.info("RUN ALL TASK");
        return true;
    }

    public boolean stopAllTasks() {
        Arrays.stream(SchedulerTypeEnum.values()).forEach(s -> stop(s));
        log.info("STOP ALL TASK");
        return true;
    }

    private Runnable getTaskBySchedulerType(SchedulerTypeEnum schedulerType) {
        switch (schedulerType) {
            case URL_MONITORING:
                return () -> monitoringScheduler.urlMonitoring();
            case API_MONITORING:
                return () -> monitoringScheduler.apiMonitoring();
            case ACT_MONITORING:
                return () -> monitoringScheduler.actMonitoring();
            default:
                return null;
        }
    }

    public boolean execute(SchedulerTypeEnum schedulerType) {
        Optional.ofNullable(getTaskBySchedulerType(schedulerType)).ifPresent(task -> task.run());
        return true;
    }
}
