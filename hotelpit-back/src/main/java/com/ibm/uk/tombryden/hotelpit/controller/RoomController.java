package com.ibm.uk.tombryden.hotelpit.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@GetMapping
	public Set<Room> getAllRoomsWithGuests(@RequestParam int guests) {
		// cant make repo query since maxGuests is a transient field (calculated from beds)
		
		// get all rooms.. find which rooms have the number of guests specified or more
		List<Room> allRooms = roomRepository.findAll();
		
		Set<Room> roomsWithGuests = new HashSet<Room>();
		for(Room room : allRooms) {
			if(room.getMaxGuests() >= guests) {
				// room has required guests - add to list
				roomsWithGuests.add(room);
			}
		}
		
		return roomsWithGuests;
	}

}
