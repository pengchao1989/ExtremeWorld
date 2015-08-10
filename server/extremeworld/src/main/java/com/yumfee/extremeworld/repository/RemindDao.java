package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Remind;

public interface RemindDao extends PagingAndSortingRepository<Remind,Long>{

	Page<Remind> findByListenerId(Long id, Pageable pageRequest);
}
