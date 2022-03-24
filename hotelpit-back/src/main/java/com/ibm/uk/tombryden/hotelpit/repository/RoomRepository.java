package com.ibm.uk.tombryden.hotelpit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.uk.tombryden.hotelpit.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
}
