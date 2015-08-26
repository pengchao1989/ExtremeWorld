package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Activity;

public interface ActivityDao extends PagingAndSortingRepository<Activity,Long>{

}
