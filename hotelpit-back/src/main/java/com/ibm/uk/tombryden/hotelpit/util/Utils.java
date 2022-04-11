package com.ibm.uk.tombryden.hotelpit.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Utils {
	
	/**
	 * Convert URL date format to LocalDate
	 * @param dateStr YYYYMMDD EG: 20220330
	 * @return Date
	 */
	public static LocalDate convertURLToDate(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate date = LocalDate.parse(dateStr, formatter);
		
		return date;
	}
	
	
	public static int getRandomNumber(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + min);
	}

}
