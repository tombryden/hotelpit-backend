package com.ibm.uk.tombryden.hotelpit.repository;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query(value = "SELECT b FROM Booking b WHERE b.status=:status")
	public Set<Booking> findByStatus(BookingStatus status);
	
	@Query(value = "SELECT b FROM Booking b WHERE b.status=:status AND b.user=:user")
	public Set<Booking> findByStatusAndUser(BookingStatus status, User user);
	
	@Query(value = "SELECT b FROM Room r JOIN r.bookings b WHERE b.checkInDate BETWEEN :checkIn AND :checkOutMinusOne OR"
			+ " b.checkOutDate BETWEEN :checkInPlusOne AND :checkOut")
	public Set<Booking> findBookedRoomsBetweenDates(@Param(value = "checkIn") LocalDate checkIn, @Param(value = "checkInPlusOne") LocalDate checkInPlusOneDay, @Param(value = "checkOut") LocalDate checkOut, @Param(value = "checkOutMinusOne") LocalDate checkOutMinusOneDay);

}
