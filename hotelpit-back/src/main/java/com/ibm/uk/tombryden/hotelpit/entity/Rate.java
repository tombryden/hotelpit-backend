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
public class Rate {
	
	public Rate(long id, @NotNull @Size(max = 30) String name, @NotNull float multiplier, @NotNull String description) {
		super();
		this.id = id;
		this.name = name;
		this.multiplier = multiplier;
		this.description = description;
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
	private float multiplier;
	
	@NotNull
	@Column(columnDefinition = "TEXT")
	private String description;

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

	public String getMultiplier() {
		BigDecimal bd = new BigDecimal(multiplier);
		
		bd.setScale(2, RoundingMode.HALF_UP);
		
		return bd.toString();
	}

	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
