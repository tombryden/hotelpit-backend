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
	private long bookingID;

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

	public long getBookingID() {
		return bookingID;
	}

	public void setBookingID(long bookingID) {
		this.bookingID = bookingID;
	}

}
