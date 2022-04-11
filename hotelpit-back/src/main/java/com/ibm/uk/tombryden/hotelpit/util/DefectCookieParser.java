package com.ibm.uk.tombryden.hotelpit.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefectCookieParser {
	
	public DefectCookieParser(String defectsJson) throws JsonMappingException, JsonProcessingException {
		// if defects not passed, then just return empty list
		if(defectsJson == null) {
			defectCookies = new ArrayList<String>();
			return;
		}
		
		ObjectMapper om = new ObjectMapper();
		this.defectCookies = om.readValue(defectsJson, new TypeReference<List<String>>(){});
	}
	
	private List<String> defectCookies;
	
	public List<String> getDefectCookies() {
		return defectCookies;
	}

	public void setDefectCookies(List<String> defectCookies) {
		this.defectCookies = defectCookies;
	}

}
