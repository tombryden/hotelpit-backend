package com.ibm.uk.tombryden.hotelpit.security;


import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;

/**
 * Custom class to get current authenticated user from security context
 * @author Tom
 *
 */
public class AuthenticatedUser {
	
	public AuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(principal instanceof UserWithID) {
			user = (UserWithID) principal;
		}
	}
	
	private UserWithID user;

	public UserWithID getUser() {
		return user;
	}
	
	public Optional<User> getUserFromRepository(UserRepository userRepository) {
		return userRepository.findById(user.getId());
	}
	
	

}
