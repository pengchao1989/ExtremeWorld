package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Collection;

public interface CollectionDao extends PagingAndSortingRepository<Collection, Long>{
	
	public Page<Collection> findByUserId(Long userId, Pageable pageable);
}
