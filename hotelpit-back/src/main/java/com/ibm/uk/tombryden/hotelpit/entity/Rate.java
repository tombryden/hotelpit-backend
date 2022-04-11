package com.ibm.uk.tombryden.hotelpit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Rate {
	
	public Rate(@NotNull @Size(max = 30) String name, @NotNull float multiplier, @NotNull String description) {
		super();
		this.name = name;
		this.description = description;
		this.multiplier = multiplier;
	}

	protected Rate () {
		
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
	private float multiplier;
	
	@ManyToMany(mappedBy = "rates")
	private Set<Room> rooms;
	
	@OneToMany(mappedBy = "rate")
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

	public float getMultiplier() {
		BigDecimal bd = new BigDecimal(multiplier);
		
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		return bd.floatValue();
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}
	
	

}
