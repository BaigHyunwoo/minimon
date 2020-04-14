package com.minimon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonTransaction;

@Repository
public interface TblMonTransactionRepository extends JpaRepository<TblMonTransaction, String> {

	TblMonTransaction findBySeq(int seq);

	List<TblMonTransaction> findByUseable(int useable);

}
