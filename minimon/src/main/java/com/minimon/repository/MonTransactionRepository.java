package com.minimon.repository;

import com.minimon.entity.MonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonTransactionRepository extends JpaRepository<MonTransaction, String> {
}
