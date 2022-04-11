package com.ibm.uk.tombryden.hotelpit.controller;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ibm.uk.tombryden.hotelpit.dto.PaymentDTO;
import com.ibm.uk.tombryden.hotelpit.dto.ReservationDTO;
import com.ibm.uk.tombryden.hotelpit.entity.Booking;
import com.ibm.uk.tombryden.hotelpit.entity.Booking.BookingStatus;
import com.ibm.uk.tombryden.hotelpit.entity.Rate;
import com.ibm.uk.tombryden.hotelpit.entity.Room;
import com.ibm.uk.tombryden.hotelpit.entity.User;
import com.ibm.uk.tombryden.hotelpit.repository.BookingRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RateRepository;
import com.ibm.uk.tombryden.hotelpit.repository.RoomRepository;
import com.ibm.uk.tombryden.hotelpit.repository.UserRepository;
import com.ibm.uk.tombryden.hotelpit.security.AuthenticatedUser;
import com.ibm.uk.tombryden.hotelpit.util.Utils;
import com.ibm.uk.tombryden.hotelpit.util.DefectCookieParser;
import com.ibm.uk.tombryden.hotelpit.util.TextResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RateRepository rateRepository;
	
	// FUNCTIONS
	private ResponseEntity<Object> verifyBookingFromRequest(Optional<Booking> booking) {
		// get authenticated user, get booking, check if booking is owned by current user, return booking
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		// check if user isnt found, if so return 404 (should never occur)
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get booking
		if(booking.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Booking not found"));
		
		// check if booking is owned by authorised user
		if(!user.get().equals(booking.get().getUser())) return ResponseEntity.status(403).body(new TextResponse("The current booking is not owned by the authorised user"));
		
		// if all is successful, returnn null
		return null;
	}
	
	// MAPPINGS
	@PostMapping("/pay")
	public ResponseEntity<Object> payAndConfirmBooking(@Valid @RequestBody PaymentDTO paymentDTO, @CookieValue(name = "defects", required = false) String defectsJsonArr) throws InterruptedException, JsonMappingException, JsonProcessingException {
		// defects
		DefectCookieParser dcp = new DefectCookieParser(defectsJsonArr);
		if(dcp.getDefectCookies().contains("Payment_PayTooLong")) Thread.sleep(Utils.getRandomNumber(10, 20) * 1000);
		
		// if card doesnt start with 4242 decline card number
		if(!paymentDTO.getCardNumber().startsWith("4242")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		// if card month doesnt equal 01 decline
		if(!paymentDTO.getExpMonth().equals("01")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		// if card year doesnt equal 22 decline
		if(!paymentDTO.getExpYear().equals("22")) return ResponseEntity.status(400).body(new TextResponse("Payment rejected"));
		
		
		// get user from authenciated user
		AuthenticatedUser authUser = new AuthenticatedUser();
		
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get current booking reservation
		Optional<Booking> reservation = bookingRepository.findById(paymentDTO.getBookingID());
		if(reservation.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Booking not found"));
		
		// check if reservation is owned by the user trying to pay
		if(!reservation.get().getUser().equals(user.get())) return ResponseEntity.status(403).body(new TextResponse("Booking is not owned by the logged in user"));
		
		// check if booking is in reservation status
		if(!reservation.get().getStatus().equals(BookingStatus.RESERVATION)) return ResponseEntity.status(400).body(new TextResponse("Booking ID provided is not in 'RESERVATION' status"));
		
		//booking paid.. change booking status to CONFIRMED
		reservation.get().setStatus(BookingStatus.CONFIRMED);
		
		return ResponseEntity.ok(bookingRepository.save(reservation.get()));
	}
	
	@PostMapping("/reserve")
	public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationDTO reservationDTO, @CookieValue(name = "defects", required = false) String defectsArrJson) throws InterruptedException, JsonMappingException, JsonProcessingException {
		// parse defects cookie
		DefectCookieParser dcp = new DefectCookieParser(defectsArrJson);
		
		if(dcp.getDefectCookies().contains("Rooms_ReservationTooLong")) Thread.sleep(Utils.getRandomNumber(10, 20) * 1000);
		
		if(dcp.getDefectCookies().contains("Rooms_ReservationInternalError")) return ResponseEntity.status(500).build();
		
		// get user from authenciated user
		AuthenticatedUser authUser = new AuthenticatedUser();
		
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get room from reqeust
		Optional<Room> room = roomRepository.findById(reservationDTO.getRoomID());
		if(room.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Room not found"));
		
		// check if user has any ongoing reservations
		Set<Booking> currentUserReservations = bookingRepository.findByStatusAndUser(BookingStatus.RESERVATION, user.get());
		if(currentUserReservations.size() > 0) {
			// reservation(s) exist, remove them
			bookingRepository.deleteAll(currentUserReservations);
		}
		
		// create new booking reservation
		Booking booking = new Booking(user.get(), room.get(), Utils.convertURLToDate(reservationDTO.getCheckInDate()), Utils.convertURLToDate(reservationDTO.getCheckOutDate()), BookingStatus.RESERVATION, reservationDTO.getTotalGuests());
		
		return ResponseEntity.ok(bookingRepository.save(booking));
	}
	
	@GetMapping("/{bookingid}")
	public ResponseEntity<Object> getBooking(@PathVariable long bookingid, @CookieValue(name = "defects", required = false) String defectsJsonArr) throws JsonMappingException, JsonProcessingException {
		// get authenticated user, get booking, check if booking is owned by current user, return booking
		AuthenticatedUser authUser = new AuthenticatedUser();
		Optional<User> user = authUser.getUserFromRepository(userRepository);
		
		// check if user isnt found, if so return 404 (should never occur)
		if(user.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("User not found"));
		
		// get booking
		Optional<Booking> booking = bookingRepository.findById(bookingid);
		if(booking.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Booking not found"));
		
		// check if booking is owned by authorised user
		if(!user.get().equals(booking.get().getUser())) return ResponseEntity.status(403).body(new TextResponse("The current booking is not owned by the authorised user"));
		
		// DEFECTS
		DefectCookieParser dcp = new DefectCookieParser(defectsJsonArr);
		// create new booking object with same info as found booking (want to update fields without it being saved automatically)
		Booking currBooking = booking.get();
		Booking fakeBooking = new Booking(currBooking.getUser(), currBooking.getRoom(), currBooking.getCheckInDate(), currBooking.getCheckOutDate(), currBooking.getStatus(), currBooking.getTotalGuests());
		
		if(dcp.getDefectCookies().contains("Rates_DatesIncorrect")) {
			fakeBooking.setCheckInDate(fakeBooking.getCheckInDate().plusDays(Utils.getRandomNumber(2, 10)));
			fakeBooking.setCheckOutDate(fakeBooking.getCheckInDate().plusDays(Utils.getRandomNumber(4, 12)));
			
			// if cookies doesnt contain payment rate defect then return.. if it does, mess with data for incorrect rate defect
			if(!dcp.getDefectCookies().contains("Payment_IncorrectRate")) return ResponseEntity.ok(fakeBooking);
		}
		
		if(dcp.getDefectCookies().contains("Payment_IncorrectRate")) {
			fakeBooking.setRate(new Rate("Business", 1.6F, "For business visitors only"));
			
			return ResponseEntity.ok(fakeBooking);
		}
		
		
		
		
		return ResponseEntity.ok(booking.get());
	}
	
	@PatchMapping("/{bookingid}/rate")
	public ResponseEntity<Object> addRateToBooking(@PathVariable long bookingid, @RequestParam long rateid, @CookieValue(name = "defects", required = false) String jsonDefectsArr) throws InterruptedException, JsonMappingException, JsonProcessingException {
		// get defects
		DefectCookieParser dcp = new DefectCookieParser(jsonDefectsArr);
		if(dcp.getDefectCookies().contains("Rates_SelectTooLong")) Thread.sleep(Utils.getRandomNumber(10, 20) * 1000);
		
		// get booking from id
		Optional<Booking> booking = bookingRepository.findById(bookingid);
		
		ResponseEntity<Object> verifyResp = verifyBookingFromRequest(booking);
		// if verified booking has an error return the error
		if(verifyResp != null) return verifyResp;
		
		// verify rate exists and is part of the booking room rate list
		Optional<Rate> rate = rateRepository.findById(rateid);
		if(rate.isEmpty()) return ResponseEntity.status(404).body(new TextResponse("Rate not found"));
		
		// is not part of booking room rate list
		if(!booking.get().getRoom().getRates().contains(rate.get())) return ResponseEntity.status(400).body(new TextResponse("Specified rate is not part of the booking room"));
		
		// checks done.. add rate to the booking
		booking.get().setRate(rate.get());
		
		return ResponseEntity.ok(bookingRepository.save(booking.get()));
	}
}
