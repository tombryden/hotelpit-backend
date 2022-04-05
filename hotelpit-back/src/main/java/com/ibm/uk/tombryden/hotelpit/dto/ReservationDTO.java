package com.ibm.uk.tombryden.hotelpit.dto;

import javax.validation.constraints.NotNull;

public class ReservationDTO {
	
	@NotNull
	private long roomID;
	
	@NotNull
	private String checkInDate;
	
	@NotNull
	private String checkOutDate;
	
	@NotNull
	private int totalGuests;

	public long getRoomID() {
		return roomID;
	}

	public void setRoomID(long roomID) {
		this.roomID = roomID;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public int getTotalGuests() {
		return totalGuests;
	}

	public void setTotalGuests(int totalGuests) {
		this.totalGuests = totalGuests;
	}

}
