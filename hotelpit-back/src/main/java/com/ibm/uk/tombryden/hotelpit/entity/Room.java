package com.ibm.uk.tombryden.hotelpit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

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

@Entity
public class Room {
	
	protected Room() {
		
	}
	
	public Room(long id, @NotNull @Size(max = 30) String name, @NotNull String description, @NotNull float basePrice, int singleBeds, int doubleBeds) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
		this.singleBeds = singleBeds;
		this.doubleBeds = doubleBeds;
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
	
	// max guests calculated: 2 * double beds (as 2x people can sleep in a double bed) + single beds
	// if in production this can be modified based on user preference (maybe prefer to not share double bed)
	public int getMaxGuests() {
		return (this.doubleBeds * 2) + this.singleBeds;
	}

}
