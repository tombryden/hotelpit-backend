package com.ibm.uk.tombryden.hotelpit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectCategory;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectPage;
import com.ibm.uk.tombryden.hotelpit.repository.DefectRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/defect")
public class DefectController {
	
	@Autowired
	private DefectRepository defectRepository;

	@GetMapping
	public ResponseEntity<Object> getDefectsByCategoryAndPage(@RequestParam DefectCategory category, @RequestParam DefectPage page) {
		return ResponseEntity.ok(defectRepository.findAllByCategoryAndName(category, page));
	}
	
}
