package com.ibm.uk.tombryden.hotelpit.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Rate;
import com.ibm.uk.tombryden.hotelpit.repository.RateRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/rate")
public class RateController {
	
	@Autowired
	private RateRepository rateRepository;
	
	@GetMapping("/room/{roomid}")
	public Set<Rate> getRoomRates(@PathVariable(value="roomid") long roomID) {
		return rateRepository.findByRoom(roomID);
	}

}
