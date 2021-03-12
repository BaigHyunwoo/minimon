package com.minimon.repository;

import com.minimon.entity.MonTransaction;
import com.minimon.enums.UseStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonTransactionRepository extends JpaRepository<MonTransaction, Integer>, JpaSpecificationExecutor {
    List<MonTransaction> findByMonitoringUseYnOrderByRegDateDesc(UseStatusEnum useStatusEnum);
}
