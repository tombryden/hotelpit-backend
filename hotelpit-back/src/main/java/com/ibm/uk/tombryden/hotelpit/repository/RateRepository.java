package com.ibm.uk.tombryden.hotelpit.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.uk.tombryden.hotelpit.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
	
	@Query(value = "SELECT ra FROM Rate ra JOIN ra.rooms ro WHERE ro.id=:roomid")
	public Set<Rate> findByRoom(@Param("roomid") long roomid);

}
