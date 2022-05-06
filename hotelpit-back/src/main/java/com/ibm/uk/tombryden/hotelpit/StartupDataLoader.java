package com.ibm.uk.tombryden.hotelpit;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ibm.uk.tombryden.hotelpit.entity.Defect;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectCategory;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectPage;
import com.ibm.uk.tombryden.hotelpit.entity.Rate;
import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.repository.DefectRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RateRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;

@Component
public class StartupDataLoader implements ApplicationRunner {
	
	@Autowired
	private DefectRepository defectRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RateRepository rateRepository;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// generate standard defects.. truncate table first
		defectRepository.truncate();
		
		// HOME PAGE - FUCNTIONAL
		defectRepository.save(new Defect("Guest selector not populating on change", 
				"The 'Guests' dropdown will not change after selecting a value", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.HOME,
				"Home_GuestSelector"));
		
		defectRepository.save(new Defect("Search button not working", 
				"The 'Search' button will not function on click", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.HOME,
				"Home_Search"));
		
		defectRepository.save(new Defect("Check-out date selector not working", 
				"The 'Check-Out' date selector will not change after selecting a value", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.HOME,
				"Home_Checkout"));
		
		//ROOM PAGE - NON-FUNCTIONAL
		defectRepository.save(new Defect("Room search takes too long to load", 
				"Clicking the search button from the home page, or refreshing the room page takes too long to load room results (above 10 seconds)", 
				DefectCategory.NONFUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_TooLong"));
		
		defectRepository.save(new Defect("Room reservation takes too long to create", 
				"Clicking the book button generates a reservation, however this takes too long to load (above 10 seconds)", 
				DefectCategory.NONFUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_ReservationTooLong"));
		
		// ROOM PAGE - FUNCTIONAL
		defectRepository.save(new Defect("Book button not appearing on room cards", 
				"Once searched for rooms, room cards appear but the expected 'Book' buttons do not appear", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_NoBook"));
		
		defectRepository.save(new Defect("Room title and description in wrong order", 
				"The room title will appear where the description should appear... and vice versa", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_Mismatch"));
		
		defectRepository.save(new Defect("Typo in page title", 
				"'Choose a room' text shows random typos", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_TitleTypo"));
		
		defectRepository.save(new Defect("Reserving a room causes internal server error from API", 
				"When clicking 'Book' a reservation should be created. However, in this instance an internal server error (500) occurs from the API", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.ROOMS,
				"Rooms_ReservationInternalError"));
		
		// RATE PAGE - FUNCTIONAL
		defectRepository.save(new Defect("Original selected check in / check out date incorrect", 
				"The original dates selected when creating the booking do not match with the dates displayed on the rates page", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.RATES,
				"Rates_DatesIncorrect"));
		
		defectRepository.save(new Defect("Total price including rate calculated wrong", 
				"The calculation (price per night * number of nights) is incorrect", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.RATES,
				"Rates_CalculationIncorrect"));
		
		// RATE PAGE - NON FUNCTIONAL
		defectRepository.save(new Defect("Selecting a rate takes too long to load", 
				"When selecting a rate, it takes too long to load before redirecting to the payment page (above 10 seconds)", 
				DefectCategory.NONFUNCTIONAL, 
				DefectPage.RATES,
				"Rates_SelectTooLong"));
				
		// PAYMENT PAGE - FUNCTIONAL
		defectRepository.save(new Defect("Wrong rate is shown after being selected from rates page", 
				"After selecting a rate, this is wrongly represented on the payment page", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.PAYMENT,
				"Payment_IncorrectRate"));
		
		defectRepository.save(new Defect("Incorrect final price", 
				"After selecting a rate, the final price is incorrect. Tip: the API returns the correct final price, use this to compare", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.PAYMENT,
				"Payment_IncorrectPrice"));
		
		// PAYMENT PAGE - NON FUNCTIONAL
		defectRepository.save(new Defect("Clicking 'Pay Now' takes too long to validate payment", 
				"After entering card details and clicking 'Pay Now' the process takes too long finish (above 10 seconds)", 
				DefectCategory.NONFUNCTIONAL, 
				DefectPage.PAYMENT,
				"Payment_PayTooLong"));
		
		
		//generate standard rates and rooms if non exist
		if(rateRepository.findAll().size() == 0) {
			Set<Rate> rates = new HashSet<Rate>();
			
			Rate flexPlus = new Rate("Flexible Plus", 1.2F, "Cancel up to one day before check in");
			Rate saver = new Rate("Saver", 1.1F, "Free cancellation up to 7 days before check in");
			Rate saverEx = new Rate("Saver Extreme", 1.0F, "No cancellation available");
			
			rateRepository.save(flexPlus);
			
			rateRepository.save(saver);
			
			rateRepository.save(saverEx);
			
			rates.add(flexPlus);
			rates.add(saver);
			rates.add(saverEx);
			
			// rooms
			roomRepository.save(new Room("The Deluxe Suite", "The most expensive room", 100, 0, 1, rates));
			
			roomRepository.save(new Room("Standard Room", "A basic room", 80, 2, 0, rates));
		}
	}

}
