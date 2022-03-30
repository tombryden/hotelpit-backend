package com.ibm.uk.tombryden.hotelpit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.uk.tombryden.hotelpit.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
