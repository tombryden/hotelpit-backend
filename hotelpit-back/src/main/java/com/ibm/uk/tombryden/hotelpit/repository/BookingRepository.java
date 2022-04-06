package com.ibm.uk.tombryden.hotelpit.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query(value = "SELECT b FROM Booking b WHERE b.status=:status")
	public Set<Booking> findByStatus(BookingStatus status);
	
	@Query(value = "SELECT b FROM Booking b WHERE b.status=:status AND b.user=:user")
	public Set<Booking> findByStatusAndUser(BookingStatus status, User user);

}
