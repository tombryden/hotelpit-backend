package com.ibm.uk.tombryden.hotelpit.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.dto.PaymentDTO;
import com.ibm.uk.tombryden.hotelpit.dto.ReservationDTO;
import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.BookingRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
import com.ibm.uk.tombryden.hotelpit.security.AuthenticatedUser;
import com.ibm.uk.tombryden.hotelpit.util.DateUtil;
import com.ibm.uk.tombryden.hotelpit.util.TextResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@PostMapping("/pay")
	public ResponseEntity<Object> payAndCreateBooking(@Valid @RequestBody PaymentDTO paymentDTO) {
		// if card doesnt start with 4242 decline card number
		if(!paymentDTO.getCardNumber().startsWith("4242")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		// if card month doesnt equal 01 decline
		if(!paymentDTO.getExpMonth().equals("01")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		// if card year doesnt equal 22 decline
		if(!paymentDTO.getExpYear().equals("22")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		
		// get user from authenciated user
		AuthenticatedUser authUser = new AuthenticatedUser();
		
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get room from reqeust
		Optional<Room> room = roomRepository.findById(paymentDTO.getRoomID());
		if(room.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Room not found"));
		
		//create booking
		Booking booking = new Booking(user.get(), room.get(), DateUtil.convertURLToDate(paymentDTO.getCheckInDate()), DateUtil.convertURLToDate(paymentDTO.getCheckOutDate()), BookingStatus.CONFIRMED, 1);
		
		return ResponseEntity.ok(bookingRepository.save(booking));
	}
	
	@PostMapping("/reserve")
	public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) throws InterruptedException {
		Thread.sleep(5000);
		
		// get user from authenciated user
		AuthenticatedUser authUser = new AuthenticatedUser();
		
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get room from reqeust
		Optional<Room> room = roomRepository.findById(reservationDTO.getRoomID());
		if(room.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Room not found"));
		
		// create booking
		Booking booking = new Booking(user.get(), room.get(), DateUtil.convertURLToDate(reservationDTO.getCheckInDate()), DateUtil.convertURLToDate(reservationDTO.getCheckOutDate()), BookingStatus.RESERVATION, reservationDTO.getTotalGuests());
		
		return ResponseEntity.ok(bookingRepository.save(booking));
	}
	
	@GetMapping("/{bookingid}")
	public ResponseEntity<Object> getBooking(@PathVariable long bookingid) {
		// get authenticated user, get booking, check if booking is owned by current user, return booking
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		// check if user isnt found, if so return 404 (should never occur)
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get booking
		Optional<Booking> booking = bookingRepository.findById(bookingid);
		if(booking.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Booking not found"));
		
		// check if booking is owned by authorised user
		if(!user.get().equals(booking.get().getUser())) return ResponseEntity.status(403).body(new TextResponse("The current booking is not owned by the authorised user"));
		
		return ResponseEntity.ok(booking.get());
		
	}
}
