package com.minimon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonApi;

@Repository
public interface TblMonApiRepository extends JpaRepository<TblMonApi, String> {

	TblMonApi findBySeq(int seq);

	List<TblMonApi> findByUseable(int useable);

}
