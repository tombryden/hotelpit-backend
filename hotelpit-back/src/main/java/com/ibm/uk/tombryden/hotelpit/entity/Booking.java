package com.ibm.uk.tombryden.hotelpit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
	
	@ManyToOne
	@JoinColumn(name="rate_id", nullable = true)
	// nullable because initially on reservation creation there will be no rate selected
	private Rate rate;
	
	private LocalDate checkInDate;
	
	private LocalDate checkOutDate;
	
	private int totalGuests;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	@CreationTimestamp
	private LocalDateTime creationTimestamp;

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

	public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
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
	
	public long getNights() {
		return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
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

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}
	
	public String getTotalPrice() {
		// if booking has confirmed status then calculate the price.. if not return null as no price can be calculated
		if(!this.getStatus().equals(BookingStatus.CONFIRMED)) return null;
		
		float totalPrice = Float.valueOf(room.getBasePrice()) * rate.getMultiplier();
		
		// convert to 2 dp
		BigDecimal bd = new BigDecimal(totalPrice);
		
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		return bd.toString();
	}
}
