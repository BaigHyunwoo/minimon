package com.minimon.service;

import com.minimon.enums.SwitchEnum;
import com.minimon.exception.MonitoringExecutionException;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Service
public class MonitoringService {
    private static Queue<Runnable> MONITORING_QUEUE = new LinkedList<>();
    private static SwitchEnum QUEUE_SWITCH = SwitchEnum.ON;


    @Scheduled(fixedDelay = 3000)
    private void run() {
        if (QUEUE_SWITCH == SwitchEnum.OFF) return;

        Runnable task = MONITORING_QUEUE.poll();
        if (task != null) {
            try {
                task.run();
            } catch (Exception e) {
                throw new MonitoringExecutionException(e);
            }
        }
    }

    public void addTask(Runnable task) {
        if (QUEUE_SWITCH == SwitchEnum.OFF) return;

        MONITORING_QUEUE.add(task);
    }
}
