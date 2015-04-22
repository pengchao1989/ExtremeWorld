package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Discussion;

public interface DiscussionDao extends PagingAndSortingRepository<Discussion, Long>{

}
