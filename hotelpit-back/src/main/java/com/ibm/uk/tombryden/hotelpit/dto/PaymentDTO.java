package com.ibm.uk.tombryden.hotelpit.dto;

import javax.validation.constraints.NotNull;

public class PaymentDTO {
	
	@NotNull
	private String cardNumber;
	
	@NotNull
	private String expMonth;
	
	@NotNull
	private String expYear;
	
	@NotNull
	private long roomID;
	
	@NotNull
	private String checkInDate;
	
	@NotNull
	private String checkOutDate;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

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

}
