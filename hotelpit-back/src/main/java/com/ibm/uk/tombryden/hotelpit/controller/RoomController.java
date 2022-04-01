package com.ibm.uk.tombryden.hotelpit.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.util.DateUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	// FUNCTIONS - NOT MAPPINGS
	private Set<Room> getAllRoomsWithGuests(int guests) {
		// cant make repo query since maxGuests is a transient field (calculated from beds)

		// get all rooms.. find which rooms have the number of guests specified or more
		List<Room> allRooms = roomRepository.findAll();
		
		Set<Room> roomsWithGuests = new HashSet<Room>();
		for(Room room : allRooms) {
			if(room.getMaxGuests() >= guests) {
				// room has required guests - add to list
				roomsWithGuests.add(room);
			}
		}
		
		return roomsWithGuests;
	}
	
	private Set<Room> getAvailableRoomsBetweenDates(LocalDate checkIn, LocalDate checkOut, int guests) {
		// get all available rooms with specified number of guests
		Set<Room> availableRooms = getAllRoomsWithGuests(guests);
		
		// loop through rooms.. check if date any bookings for the room within the specified dates
		Set<Room> roomsBookedBetweenDates = roomRepository.findBookedRoomsBetweenDates(checkIn, checkIn.plusDays(1), checkOut, checkOut.minusDays(1));
		
		// remove all booked rooms from available rooms
		availableRooms.removeAll(roomsBookedBetweenDates);
		
		return availableRooms;
	}
	
	
	
	// MAPPINGS
	@GetMapping
	public Set<Room> roomSearch(@RequestParam String checkin, @RequestParam String checkout, @RequestParam int guests) {
		LocalDate checkIn = DateUtil.convertURLToDate(checkin);
		LocalDate checkOut = DateUtil.convertURLToDate(checkout);
		
		return getAvailableRoomsBetweenDates(checkIn, checkOut, guests);
	}

}
