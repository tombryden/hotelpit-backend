package com.ibm.uk.tombryden.hotelpit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Room {
	
	protected Room() {
		
	}
	
	public Room(@NotNull @Size(max = 30) String name, @NotNull String description, @NotNull float basePrice, int singleBeds, int doubleBeds, Set<Rate> rates) {
		super();
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
		this.singleBeds = singleBeds;
		this.doubleBeds = doubleBeds;
		this.rates = rates;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(max = 30)
	private String name;
	
	@NotNull
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@NotNull
	private float basePrice;
	
	private int singleBeds;
	
	private int doubleBeds;
	
	@ManyToMany
	@JoinTable(
			name = "room_rate",
			joinColumns = { @JoinColumn(name="room_id") },
			inverseJoinColumns = { @JoinColumn(name="rate_id") }
			)
	private Set<Rate> rates;
	
	@JsonIgnore
	@OneToMany(mappedBy = "room")
	private Set<Booking> bookings;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBasePrice() {
		BigDecimal bd = new BigDecimal(basePrice);
		
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		return bd.toString();
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public int getSingleBeds() {
		return singleBeds;
	}

	public void setSingleBeds(int singleBeds) {
		this.singleBeds = singleBeds;
	}

	public int getDoubleBeds() {
		return doubleBeds;
	}

	public void setDoubleBeds(int doubleBeds) {
		this.doubleBeds = doubleBeds;
	}
	
	public Set<Rate> getRates() {
		Set<Rate> ratesOrderedByMultiplier = new TreeSet<Rate>(new Comparator<Rate>() {

			@Override
			public int compare(Rate o1, Rate o2) {
				Float m1 = o1.getMultiplier();
				Float m2 = o2.getMultiplier();
				return m2.compareTo(m1);
			}
			
		});
		
		ratesOrderedByMultiplier.addAll(rates);
		
		return ratesOrderedByMultiplier;
	}

	public void setRates(Set<Rate> rates) {
		this.rates = rates;
	}

	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}

	// max guests calculated: 2 * double beds (as 2x people can sleep in a double bed) + single beds
	// if in production this can be modified based on user preference (maybe prefer to not share double bed)
	public int getMaxGuests() {
		return (this.doubleBeds * 2) + this.singleBeds;
	}

}
