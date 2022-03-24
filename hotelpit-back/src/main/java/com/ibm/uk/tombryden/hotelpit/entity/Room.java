package com.ibm.uk.tombryden.hotelpit.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Room {
	
	protected Room() {
		
	}
	
	public Room(long id, @NotNull @Size(max = 30) String name, @NotNull String description, @NotNull float basePrice) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.basePrice = basePrice;
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

}
