package com.ibm.uk.tombryden.hotelpit.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Booking {
	
	public enum BookingStatus {
		RESERVATION,
		CONFIRMED,
		CANCELLED,
	}
	
	public Booking(User user, Room room, LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status, int totalGuests) {
		super();
		this.user = user;
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.status = status;
		this.totalGuests = totalGuests;
	}

	protected Booking() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="room_id", nullable = false)
	private Room room;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private int totalGuests;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	@CreationTimestamp
	private Timestamp creationTimestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public int getTotalGuests() {
		return totalGuests;
	}

	public void setTotalGuests(int totalGuests) {
		this.totalGuests = totalGuests;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}
}
