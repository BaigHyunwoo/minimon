package com.minimon.scheduler;

import com.minimon.enums.SchedulerTypeEnum;
import com.minimon.repository.SchedulerHistoryRepository;
import com.minimon.vo.RunningSchedulerVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CustomSchedulerTest {

    @Autowired
    private CustomScheduler customScheduler;

    @Autowired
    private SchedulerHistoryRepository schedulerHistoryRepository;

    @Test
    void getRunningScheduler() {
        RunningSchedulerVO runningSchedulerVO = customScheduler.getRunningScheduler();
        assertNotEquals(0, runningSchedulerVO.getRunningTaskCount());
        assertNotEquals(0, runningSchedulerVO.getRunningTaskCount());
    }

    @Test
    void stopAllTasks() {
        int totalTaskSize = SchedulerTypeEnum.values().length;

        totalTaskSize -= totalTaskSize;
        customScheduler.stopAllTasks();
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());
    }

    @Test
    void runAllTasks() {
        int totalTaskSize = SchedulerTypeEnum.values().length;

        totalTaskSize -= totalTaskSize;
        customScheduler.stopAllTasks();
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());

        totalTaskSize += SchedulerTypeEnum.values().length;
        customScheduler.runAllTasks();
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());
    }

    @Test
    void run() {
        int totalTaskSize = SchedulerTypeEnum.values().length;

        totalTaskSize -= totalTaskSize;
        customScheduler.stopAllTasks();
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());

        totalTaskSize++;
        assertEquals(true, customScheduler.run(SchedulerTypeEnum.URL_MONITORING));
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());
    }

    @Test
    void stop() {
        int totalTaskSize = SchedulerTypeEnum.values().length;

        totalTaskSize--;
        assertEquals(true, customScheduler.stop(SchedulerTypeEnum.URL_MONITORING));
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());

        totalTaskSize--;
        assertEquals(true, customScheduler.stop(SchedulerTypeEnum.API_MONITORING));
        assertEquals(totalTaskSize, customScheduler.getRunningScheduler().getRunningTaskCount());
    }

    @Test
    void execute() {
        assertEquals(true, customScheduler.execute(SchedulerTypeEnum.API_MONITORING));
        assertNotEquals(0, schedulerHistoryRepository.count());
    }
}