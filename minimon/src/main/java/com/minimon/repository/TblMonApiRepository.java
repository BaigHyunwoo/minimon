package com.minimon.repository;

import com.minimon.entity.TblMonApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TblMonApiRepository extends JpaRepository<TblMonApi, String> {

	TblMonApi findBySeq(int seq);

	List<TblMonApi> findByUseableAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
			int useable, Date startDate, Date endDate, int startHour, int endHour);

}
