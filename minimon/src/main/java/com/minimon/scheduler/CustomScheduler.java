package com.minimon.scheduler;

import com.minimon.enums.SchedulerActiveTypeEnum;
import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import com.minimon.vo.SchedulerTaskVO;
import com.minimon.vo.SchedulerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomScheduler implements InitializingBean {
    private final TaskScheduler scheduler;
    private final MonitoringScheduler monitoringScheduler;
    private Map<String, SchedulerTaskVO> scheduledTasks = new ConcurrentHashMap<>();


    /**
     * 서버 시작시 스케줄러 자동 실행
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        runAllTasks();
    }

    public Map getRunningScheduler() {
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("CURRENT THREAD SIZE", Thread.activeCount());
        tasks.put("TOTAL TASK SIZE", scheduledTasks.size());

        for (SchedulerTypeEnum schedulerTypeEnum : SchedulerTypeEnum.values()) {
            String status = SchedulerStatusEnum.STOP.getCode();
            SchedulerTaskVO task = new SchedulerTaskVO();
            if (scheduledTasks.containsKey(schedulerTypeEnum.getCode())) {
                task = scheduledTasks.get(schedulerTypeEnum.getCode());
                ScheduledFuture scheduledFuture = task.getScheduler();
                status = (scheduledFuture.isDone() == false ? SchedulerStatusEnum.RUNNING.getCode() : SchedulerStatusEnum.STOP.getCode());
            }

            task.setSchedulerType(schedulerTypeEnum);
            task.setStatus(status);
            tasks.put(schedulerTypeEnum.getCode(), task);
        }

        log.info(tasks.toString());
        return tasks;
    }

    public boolean run(String schedulerType) {
        if (scheduledTasks.containsKey(schedulerType) == false) {
            SchedulerTypeEnum schedulerTypeEnum = SchedulerTypeEnum.valueOf(schedulerType);
            Optional.ofNullable(getTaskBySchedulerType(schedulerTypeEnum)).ifPresent(task -> {
                run(task, schedulerTypeEnum.getCode(), schedulerTypeEnum.getActiveType(), schedulerTypeEnum.getTime());
            });
            return true;
        }
        return false;
    }

    public boolean run(SchedulerVO customExecutorVO) {
        if (scheduledTasks.containsKey(customExecutorVO.getSchedulerType()) == false) {
            Optional.ofNullable(getTaskBySchedulerType(SchedulerTypeEnum.valueOf(customExecutorVO.getSchedulerType()))).ifPresent(task -> {
                run(task, customExecutorVO.getSchedulerType(), customExecutorVO.getActiveType(), customExecutorVO.getTime());
            });
            return true;
        }
        return false;
    }

    private void run(Runnable task, String schedulerType, String activeType, String time) {
        SchedulerTaskVO schedulerTaskVO = new SchedulerTaskVO();
        schedulerTaskVO.setSchedulerType(SchedulerTypeEnum.valueOf(schedulerType));
        switch (SchedulerActiveTypeEnum.valueOf(activeType)) {
            case CRON:
                schedulerTaskVO.setScheduler(scheduler.schedule(task, new CronTrigger(time)));
                break;
            case DELAY:
                schedulerTaskVO.setScheduler(scheduler.scheduleAtFixedRate(task, Integer.parseInt(time)));
                break;
        }
        scheduledTasks.put(schedulerType, schedulerTaskVO);
        log.info("RUN TASK " + schedulerType + " " + activeType + " " + time);
    }

    public boolean stop(String schedulerType) {
        if (Optional.ofNullable(scheduledTasks.get(schedulerType)).isPresent()) {
            SchedulerTaskVO schedulerTaskVO = scheduledTasks.get(schedulerType);
            ScheduledFuture scheduledFuture = schedulerTaskVO.getScheduler();
            log.info(schedulerType + " isCancel ? " + scheduledFuture.cancel(true));
            scheduledTasks.remove(schedulerType);
            log.info("STOP TASK " + schedulerType);
            return true;
        }
        return false;
    }

    public boolean runAllTasks() {
        Arrays.stream(SchedulerTypeEnum.values()).forEach(s ->
                run(SchedulerVO.builder()
                        .schedulerType(s.getCode())
                        .activeType(s.getActiveType())
                        .time(s.getTime())
                        .build()));
        log.info("RUN ALL TASK");
        return true;
    }

    public boolean stopAllTasks() {
        Arrays.stream(SchedulerTypeEnum.values()).forEach(s -> stop(s.getCode()));
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
                return () -> monitoringScheduler.transactionMonitoring();
            default:
                return null;
        }
    }

    public boolean execute(String schedulerType) {
        Optional.ofNullable(getTaskBySchedulerType(SchedulerTypeEnum.valueOf(schedulerType))).ifPresent(task -> task.run());
        return true;
    }
}
