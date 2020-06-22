package com.minimon.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimon.entity.TblMonUrl;

@Repository
public interface TblMonUrlRepository extends JpaRepository<TblMonUrl, String> {

	TblMonUrl findBySeq(int seq);

	List<TblMonUrl> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);
}
