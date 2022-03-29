package com.ibm.uk.tombryden.hotelpit.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		// find user by username
		Optional<com.ibm.uk.tombryden.hotelpit.entity.User> user = userRepository.findByUsername(username);
		
		// if user doesn't exist.. throw exception
		if(user.isEmpty()) throw new UsernameNotFoundException("User not found");
		
		// only have one role as no admin capability
		GrantedAuthority sga = new SimpleGrantedAuthority("USER");
	
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(sga);
		
		// return spring userdetails obj
		return new User(user.get().getUsername(), user.get().getPassword(), authorities);
	}

}
