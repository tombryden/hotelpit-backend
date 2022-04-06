package com.ibm.uk.tombryden.hotelpit;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.repository.BookingRepository;

@Component
public class ScheduledTasks {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	/**
	 * Removes expired booking reservations from the database every second
	 */
	@Scheduled(fixedRate = 1000)
	public void removeExpiredBookingReservations() {
		
		Set<Booking> allBookingReservations = bookingRepository.findByStatus(BookingStatus.RESERVATION);
		
		// loop through reservations and find the ones that have expired (10 mins after current time)
		Set<Booking> bookingsToRemove = new HashSet<Booking>();
		LocalDateTime current = LocalDateTime.now();
		
		for(Booking booking : allBookingReservations) {
			LocalDateTime reservationEndTime = booking.getCreationTimestamp().plusMinutes(10);
			
			// if the current time is equal or past the end time.. remove this reservation
			if(current.isEqual(reservationEndTime) || current.isAfter(reservationEndTime)) {
				bookingsToRemove.add(booking);
			}
		}
		
		bookingRepository.deleteAllInBatch(bookingsToRemove);
	}

}
