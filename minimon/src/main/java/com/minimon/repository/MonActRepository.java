package com.minimon.repository;

import com.minimon.entity.MonAct;
import com.minimon.enums.UseStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonActRepository extends JpaRepository<MonAct, Integer>, JpaSpecificationExecutor {
    List<MonAct> findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum useStatusEnum);
}
