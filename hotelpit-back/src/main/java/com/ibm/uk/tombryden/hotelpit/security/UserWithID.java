package com.ibm.uk.tombryden.hotelpit.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class UserWithID extends User {
	
	public UserWithID(String username, String password, Collection<? extends GrantedAuthority> authorities, long id) {
		super(username, password, authorities);
		
		this.id = id;
	}

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

}
