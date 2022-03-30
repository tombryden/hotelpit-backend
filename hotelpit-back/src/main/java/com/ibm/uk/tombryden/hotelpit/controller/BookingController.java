package com.ibm.uk.tombryden.hotelpit.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.dto.PaymentDTO;
import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.BookingRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
import com.ibm.uk.tombryden.hotelpit.security.AuthenticatedUser;
import com.ibm.uk.tombryden.hotelpit.util.DateUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
		if(!paymentDTO.getCardNumber().startsWith("4242")) return ResponseEntity.status(400).body("Payment rejected");
		
		// if card month doesnt equal 01 decline
		if(!paymentDTO.getExpMonth().equals("01")) return ResponseEntity.status(400).body("Payment rejected");
		
		// if card year doesnt equal 22 decline
		if(!paymentDTO.getExpYear().equals("22")) return ResponseEntity.status(400).body("Payment rejected");
		
		
		// get user from authenciated user (can never return nothing here since user has to be authenticated to access endpoint)
		AuthenticatedUser authUser = new AuthenticatedUser();
		
		Optional<User> user = userRepository.findById(authUser.getUser().getId());
		if(user.isEmpty()) return ResponseEntity.status(500).body("User not found");
		
		// get room from reqeust
		Optional<Room> room = roomRepository.findById(paymentDTO.getRoomID());
		if(room.isEmpty()) return ResponseEntity.status(400).body("Room could not be found");
		
		//create booking
		Booking booking = new Booking(user.get(), room.get(), DateUtil.convertURLToDate(paymentDTO.getCheckInDate()), DateUtil.convertURLToDate(paymentDTO.getCheckOutDate()));
		
		return ResponseEntity.ok(bookingRepository.save(booking));
		
	}

}