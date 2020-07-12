package com.minimon.repository;

import com.minimon.entity.MonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonResultRepository extends JpaRepository<MonResult, String> {

	List<MonResult> findByTypeOrderByRegDateAsc(String type);
	
}
