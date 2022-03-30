package com.ibm.uk.tombryden.hotelpit.util;

public class TextResponse {
	
	public TextResponse(String message) {
		super();
		this.message = message;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
