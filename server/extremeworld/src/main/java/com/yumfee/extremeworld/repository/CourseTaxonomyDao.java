package com.yumfee.extremeworld.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Course;
import com.yumfee.extremeworld.entity.CourseTaxonomy;

public interface CourseTaxonomyDao extends PagingAndSortingRepository<CourseTaxonomy,Long>, JpaSpecificationExecutor<CourseTaxonomy>
{

	List<CourseTaxonomy> findAll();
	
	@Query("select ct from CourseTaxonomy ct JOIN ct.courses c WHERE c.t=?")
	List<CourseTaxonomy> find(String t);
}
