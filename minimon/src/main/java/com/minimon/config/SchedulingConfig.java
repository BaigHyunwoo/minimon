package com.minimon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@EnableScheduling
@Configuration
public class SchedulingConfig {

    @Bean
    public TaskScheduler configureTasks() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("MonitoringScheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}
