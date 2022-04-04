package com.ibm.uk.tombryden.hotelpit.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.util.DateUtil;
import com.ibm.uk.tombryden.hotelpit.util.TextResponse;

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
	public ResponseEntity<Object> roomSearch(@RequestParam String checkin, @RequestParam String checkout, @RequestParam int guests) {
		
		// parse string date sent in params - try catch to give 400 status instead of 500
		LocalDate checkIn;
		try {
			checkIn = DateUtil.convertURLToDate(checkin);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new TextResponse("Failed to parse check in date '" + checkin + "'. Format must be yyyyMMdd"));
		}
		
		LocalDate checkOut;
		try {
			checkOut = DateUtil.convertURLToDate(checkout);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new TextResponse("Failed to parse check out date '" + checkout + "'. Format must be yyyyMMdd"));
		}
		
		return ResponseEntity.ok(getAvailableRoomsBetweenDates(checkIn, checkOut, guests));
	}
	
	@GetMapping("/{roomid}")
	public ResponseEntity<Object> individualRoomSearch(@PathVariable(value = "roomid") long roomid) {
		Optional<Room> room = roomRepository.findById(roomid);
		
		// if room is not found return 404
		if(room.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Could not find room with ID '" + roomid + "'"));
		
		return ResponseEntity.ok(room.get());
	}

}
