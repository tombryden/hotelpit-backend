package com.ibm.uk.tombryden.hotelpit.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.BookingRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
import com.ibm.uk.tombryden.hotelpit.security.AuthenticatedUser;
import com.ibm.uk.tombryden.hotelpit.util.DefectCookieParser;
import com.ibm.uk.tombryden.hotelpit.util.TextResponse;
import com.ibm.uk.tombryden.hotelpit.util.Utils;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	private Set<Room> getAvailableRoomsBetweenDates(LocalDate checkIn, LocalDate checkOut, int guests, User user) {
		// get all available rooms with specified number of guests
		Set<Room> availableRooms = getAllRoomsWithGuests(guests);
		
		// loop through rooms.. check if date any bookings for the room within the specified dates
		Set<Booking> bookingsBookedBetweenDates = bookingRepository.findBookedRoomsBetweenDates(checkIn, checkIn.plusDays(1), checkOut, checkOut.minusDays(1));
		
		// remove any rooms that are reserved by current user - want users to be able to search for similar dates, and begin the reservation process again 
		if(user != null) {
			for(Booking booking : bookingsBookedBetweenDates) {
				// if booking is owned by current user and the booking is a reservation.. show this in the search
				if(booking.getUser().equals(user) && booking.getStatus().equals(BookingStatus.RESERVATION)) {
					bookingsBookedBetweenDates.remove(booking);
				}
			}
		}
		
		Set<Room> bookedRooms = new HashSet<Room>();
		bookingsBookedBetweenDates.forEach(b -> bookedRooms.add(b.getRoom()));
		
		// remove all booked rooms from available rooms
		availableRooms.removeAll(bookedRooms);
		
		return availableRooms;
	}
	
	
	
	// MAPPINGS
	@GetMapping
	public ResponseEntity<Object> roomSearch(@RequestParam String checkin, @RequestParam String checkout, @RequestParam int guests, @CookieValue(name = "defects", required = false) String jsonDefectsArr) throws JsonMappingException, JsonProcessingException, InterruptedException {
		// get defect cookies..
		DefectCookieParser dcp = new DefectCookieParser(jsonDefectsArr);
		
		// generate random number between 10-20 secs
		if(dcp.getDefectCookies().contains("Rooms_TooLong")) Thread.sleep(Utils.getRandomNumber(10, 20) * 1000);
		
		LocalDate checkIn;
		try {
			checkIn = Utils.convertURLToDate(checkin);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new TextResponse("Failed to parse check in date '" + checkin + "'. Format must be yyyyMMdd"));
		}
		
		LocalDate checkOut;
		try {
			checkOut = Utils.convertURLToDate(checkout);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new TextResponse("Failed to parse check out date '" + checkout + "'. Format must be yyyyMMdd"));
		}
		
		// get if there is currently a logged in user
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		return ResponseEntity.ok(getAvailableRoomsBetweenDates(checkIn, checkOut, guests, user.isEmpty() ? null : user.get()));
	}
	
	@GetMapping("/{roomid}")
	public ResponseEntity<Object> individualRoomSearch(@PathVariable(value = "roomid") long roomid) {
		Optional<Room> room = roomRepository.findById(roomid);
		
		// if room is not found return 404
		if(room.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Could not find room with ID '" + roomid + "'"));
		
		return ResponseEntity.ok(room.get());
	}

}
