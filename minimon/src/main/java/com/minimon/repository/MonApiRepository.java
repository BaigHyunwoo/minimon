package com.minimon.repository;

import com.minimon.entity.MonApi;
import com.minimon.enums.UseStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonApiRepository extends JpaRepository<MonApi, Integer> {
	List<MonApi> findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum monitoringUseYn);

}
