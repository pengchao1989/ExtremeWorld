package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Hobby;

public interface HobbyDao extends PagingAndSortingRepository<Hobby, Long>{
	
	

}
