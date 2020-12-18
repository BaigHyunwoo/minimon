package com.minimon.repository;

import com.minimon.entity.MonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonResultRepository extends JpaRepository<MonResult, String> {
}
