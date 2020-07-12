package com.minimon.repository;

import com.minimon.entity.MonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonTransactionRepository extends JpaRepository<MonTransaction, String> {

	MonTransaction findBySeq(int seq);

	List<MonTransaction> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);
}
