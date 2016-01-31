package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.AccessLog;

public interface AccessLogDao extends PagingAndSortingRepository<AccessLog, Long>{

}
