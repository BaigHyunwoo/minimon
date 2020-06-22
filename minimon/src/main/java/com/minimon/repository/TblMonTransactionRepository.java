package com.minimon.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonTransaction;

@Repository
public interface TblMonTransactionRepository extends JpaRepository<TblMonTransaction, String> {

	TblMonTransaction findBySeq(int seq);

	List<TblMonTransaction> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);
}
