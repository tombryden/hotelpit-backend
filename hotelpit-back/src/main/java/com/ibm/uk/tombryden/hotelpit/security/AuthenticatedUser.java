package com.ibm.uk.tombryden.hotelpit.security;


import org.springframework.security.core.context.SecurityContextHolder;

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
	
	

}
