package com.ibm.uk.tombryden.hotelpit.repository;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ibm.uk.tombryden.hotelpit.entity.Defect;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectCategory;
import com.ibm.uk.tombryden.hotelpit.entity.Defect.DefectPage;

public interface DefectRepository extends JpaRepository<Defect, Long> {
	
	@Query(value = "SELECT d FROM Defect d WHERE d.category=:category AND d.page=:page")
	public Set<Defect> findAllByCategoryAndName(DefectCategory category, DefectPage page);
	
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE defect", nativeQuery = true)
	public void truncate();

}
