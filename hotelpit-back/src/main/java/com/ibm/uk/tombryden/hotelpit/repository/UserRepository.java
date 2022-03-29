package com.ibm.uk.tombryden.hotelpit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.uk.tombryden.hotelpit.entity.User;

public interface UserRepository extends JpaRepository<User, Long>  {
	
	public Optional<User> findByUsername(String username);

}
