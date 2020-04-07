package com.minimon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblModuleInfo;

@Repository
public interface TblModuleInfoRepository extends JpaRepository<TblModuleInfo,String> {
}
