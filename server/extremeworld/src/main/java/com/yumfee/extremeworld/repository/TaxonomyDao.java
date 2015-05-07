package com.yumfee.extremeworld.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Taxonomy;

public interface TaxonomyDao extends PagingAndSortingRepository<Taxonomy, Long>{

	List<Taxonomy> findByHobbyId(Long id);
}
