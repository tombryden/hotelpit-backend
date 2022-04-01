package com.ibm.uk.tombryden.hotelpit.repository;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.uk.tombryden.hotelpit.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
	@Query(value = "SELECT r FROM Room r JOIN r.bookings b WHERE b.checkInDate BETWEEN :checkIn AND :checkOutMinusOne OR"
			+ " b.checkOutDate BETWEEN :checkInPlusOne AND :checkOut")
	public Set<Room> findBookedRoomsBetweenDates(@Param(value = "checkIn") LocalDate checkIn, @Param(value = "checkInPlusOne") LocalDate checkInPlusOneDay, @Param(value = "checkOut") LocalDate checkOut, @Param(value = "checkOutMinusOne") LocalDate checkOutMinusOneDay);
	
}
