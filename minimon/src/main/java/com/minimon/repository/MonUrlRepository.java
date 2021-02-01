package com.minimon.repository;

import com.minimon.entity.MonUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonUrlRepository extends JpaRepository<MonUrl, Integer> {
	List<MonUrl> findByMonitoringUseYn(String monitoringUseYn);
}
