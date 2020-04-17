package com.minimon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonResult;

@Repository
public interface TblMonResultRepository extends JpaRepository<TblMonResult, String> {

	List<TblMonResult> findByTypeOrderByRegDateAsc(String type);
	
}
