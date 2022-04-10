package com.ibm.uk.tombryden.hotelpit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ibm.uk.tombryden.hotelpit.entity.Defect;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectCategory;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectPage;
import com.ibm.uk.tombryden.hotelpit.repository.DefectRepository;

@Component
public class StartupDataLoader implements ApplicationRunner {
	
	@Autowired
	private DefectRepository defectRepository;
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// generate standard defects.. truncate table first
		defectRepository.truncate();
		
		// HOME PAGE - FUCNTIONAL
		defectRepository.save(new Defect("Guest selector not populating on change", 
				"The 'Guests' dropdown will not change after selecting a value", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.HOME,
				"guestSelector"));
		
		defectRepository.save(new Defect("Search button not working", 
				"The 'Search' button will not function on click", 
				DefectCategory.FUNCTIONAL, 
				DefectPage.HOME,
				"searchNotWorking"));
	}

}
