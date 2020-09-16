package com.minimon.service;

import com.minimon.enums.SchedulerActiveTypeEnum;
import com.minimon.enums.SchedulerStatusEnum;
import com.minimon.enums.SchedulerTypeEnum;
import com.minimon.scheduler.MonitoringScheduler;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomSchedulerService implements InitializingBean {
    private final TaskScheduler scheduler;
    private final MonitoringScheduler monitoringScheduler;
    private Map<String, Map<String, Object>> scheduledTasks = new ConcurrentHashMap<>();


    /**
     * 서버 시작시 스케줄러 자동 실행
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
//        runAllTasks();
    }

    public Map getRunningScheduler() {
        Map<String, Object> tasks = new HashMap<>();
        tasks.put("CURRENT THREAD SIZE", Thread.activeCount());
        tasks.put("TOTAL TASK SIZE", scheduledTasks.size());

        Arrays.stream(SchedulerTypeEnum.values()).forEach(s -> {
            Map<String, String> taskStatus = new HashMap<>();
            String activeType = s.getActiveType();
            String time = s.getTime();
            String status = SchedulerStatusEnum.STOP.getCode();

            if (scheduledTasks.containsKey(s.getCode())) {
                Map task = scheduledTasks.get(s.getCode());
                ScheduledFuture scheduledFuture = (ScheduledFuture) task.get("task");
                status = (scheduledFuture.isDone() == false ? SchedulerStatusEnum.RUNNING.getCode() : SchedulerStatusEnum.STOP.getCode());
                time = "" + task.get("Time");
                activeType = "" + task.get("ActiveType");
            }

            taskStatus.put("ActiveType", activeType);
            taskStatus.put("time", time);
            taskStatus.put("Status", status);
            tasks.put(s.getCode(), taskStatus);
        });

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
        Map<String, Object> taskStatus = new HashMap<>();
        taskStatus.put("ActiveType", activeType);
        taskStatus.put("Time", time);
        switch (SchedulerActiveTypeEnum.valueOf(activeType)) {
            case CRON:
                taskStatus.put("task", scheduler.schedule(task, new CronTrigger(time)));
                break;
            case DELAY:
                taskStatus.put("task", scheduler.scheduleAtFixedRate(task, Integer.parseInt(time)));
                break;
        }
        scheduledTasks.put(schedulerType, taskStatus);
        log.info("RUN TASK " + schedulerType + " " + activeType + " " + time);
    }

    public boolean stop(String schedulerType) {
        if (Optional.ofNullable(scheduledTasks.get(schedulerType)).isPresent()) {
            Map task = scheduledTasks.get(schedulerType);
            ScheduledFuture scheduledFuture = (ScheduledFuture) task.get("task");
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
            case MONITORING:
                return () -> monitoringScheduler.execute();
            default:
                return null;
        }
    }
}
