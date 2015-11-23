package com.yumfee.extremeworld.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.CourseTaxonomy;

public interface CourseTaxonomyDao extends PagingAndSortingRepository<CourseTaxonomy,Long>, JpaSpecificationExecutor<CourseTaxonomy>
{
	List<CourseTaxonomy> findAll();
	
	@Query("SELECT ct FROM CourseTaxonomy ct  WHERE ct.hobby.id=?")
	List<CourseTaxonomy> findByHobby(Long hobbyId);
}
