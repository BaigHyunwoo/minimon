package com.minimon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonUrl;

@Repository
public interface TblMonUrlRepository extends JpaRepository<TblMonUrl, String> {

	TblMonUrl findBySeq(int seq);

}
