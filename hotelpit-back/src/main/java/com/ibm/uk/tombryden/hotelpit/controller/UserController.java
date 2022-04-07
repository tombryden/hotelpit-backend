package com.ibm.uk.tombryden.hotelpit.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
import com.ibm.uk.tombryden.hotelpit.security.AuthenticatedUser;
import com.ibm.uk.tombryden.hotelpit.util.TextResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/signup")
	public User signUp(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user) throws InterruptedException {
		Thread.sleep(1000);
		
//		Authentication
		
		try
		{
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			return ResponseEntity.ok().body(new TextResponse("Logged in"));
		} catch(BadCredentialsException ex) {
			return ResponseEntity.ok().body(new TextResponse("Credentials incorrect"));
		} catch(Exception ex) {
			return ResponseEntity.status(500).build();
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Object> logout() {
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/auth")
	public ResponseEntity<Object> authenticated() {
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/reservation")
	public ResponseEntity<Object> getReservationIfExists() {
		// get authenticated user, get all bookings, check if any have reservation status, return first one as user can only have one reservation
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		// check if user isnt found, if so return 404 (should never occur)
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// loop through bookings.. check if any have reservation status.. if so, return this booking obj
		for(Booking booking : user.get().getBookings()) {
			if(booking.getStatus().equals(BookingStatus.RESERVATION)) {
				return ResponseEntity.ok(booking);
			}
		}
		
		// if no reservation found return 404
		return ResponseEntity.status(404).body(new TextResponse("User has no booking reservation"));
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<Object> getAllUserBookings() {
		// get authenticated user, get all bookings, check if any have reservation status, return first one as user can only have one reservation
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		// check if user isnt found, if so return 404 (should never occur)
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get all bookings in order of check in date
		// list because comparitor will often be duplicate (can be same check in date between rooms)
		List<Booking> bookings = new ArrayList<Booking>();
		bookings.addAll(user.get().getBookings());
		
		Collections.sort(bookings, new Comparator<Booking>() {

			@Override
			public int compare(Booking b1, Booking b2) {
				return b2.getCheckInDate().compareTo(b1.getCheckInDate());
			}
		});
		
		return ResponseEntity.ok(bookings);
	}

}
