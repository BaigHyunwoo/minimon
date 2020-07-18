package com.minimon.repository;

import com.minimon.entity.MonUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonUrlRepository extends JpaRepository<MonUrl, String> {

	MonUrl findBySeq(int seq);

	List<MonUrl> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);
}