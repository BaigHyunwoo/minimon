package com.minimon.repository;

import com.minimon.entity.MonApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonApiRepository extends JpaRepository<MonApi, String> {

	MonApi findBySeq(int seq);

	List<MonApi> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);

}
