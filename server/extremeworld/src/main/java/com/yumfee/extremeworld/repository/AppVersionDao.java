package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.AppVersion;

public interface AppVersionDao extends PagingAndSortingRepository<AppVersion, Long> {
	
}
