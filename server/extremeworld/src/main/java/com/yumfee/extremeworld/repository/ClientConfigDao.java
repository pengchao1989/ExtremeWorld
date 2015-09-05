package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.AppKey;

public interface ClientConfigDao extends PagingAndSortingRepository<AppKey, Long>{

}
