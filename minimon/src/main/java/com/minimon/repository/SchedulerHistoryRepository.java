package com.minimon.repository;

import com.minimon.entity.SchedulerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerHistoryRepository extends JpaRepository<SchedulerHistory, Integer> {
}
