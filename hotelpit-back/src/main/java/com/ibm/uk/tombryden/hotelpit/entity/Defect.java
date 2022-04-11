package com.ibm.uk.tombryden.hotelpit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Defect {

	public enum DefectCategory {
		FUNCTIONAL, NONFUNCTIONAL,
	}
	
	public enum DefectPage {
		HOME, ROOMS, RATES, PAYMENT,
	}
	
	protected Defect() {
		
	}
	
	
	public Defect(String defectTitle, String defectHelper, DefectCategory defectCategory,
			DefectPage defectPage, String cookieName) {
		super();
		this.title = defectTitle;
		this.helper = defectHelper;
		this.category = defectCategory;
		this.page = defectPage;
		this.cookie = cookieName;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String helper;
	
	@Enumerated(EnumType.STRING)
	private DefectCategory category;
	
	@Enumerated(EnumType.STRING)
	private DefectPage page;
	
	private String cookie;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String defectTitle) {
		this.title = defectTitle;
	}

	public String getHelper() {
		return helper;
	}

	public void setHelper(String defectHelper) {
		this.helper = defectHelper;
	}

	public DefectCategory getCategory() {
		return category;
	}

	public void setCategory(DefectCategory defectCategory) {
		this.category = defectCategory;
	}

	public DefectPage getPage() {
		return page;
	}

	public void setPage(DefectPage defectPage) {
		this.page = defectPage;
	}


	public String getCookie() {
		return cookie;
	}


	public void setCookie(String cookieName) {
		this.cookie = cookieName;
	}
	

}
