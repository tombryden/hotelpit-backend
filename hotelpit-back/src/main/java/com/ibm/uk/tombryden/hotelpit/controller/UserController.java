package com.ibm.uk.tombryden.hotelpit.controller;


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

import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
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

}
