package com.yumfee.extremeworld.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseCatalogue;


public interface CourseCatalogueDao extends PagingAndSortingRepository<Course,Long>{

	@Query("select cc from CourseCatalogue cc JOIN cc.courses c WHERE c.type=?")
	List<CourseCatalogue> findByType(String t);
	
	@Query("SELECT cc FROM  CourseCatalogue cc WHERE cc.courseTaxonomy.id=?")
	List<CourseCatalogue> findByCourseCatalogueId(Long courseTaxonomyId);
	
}
