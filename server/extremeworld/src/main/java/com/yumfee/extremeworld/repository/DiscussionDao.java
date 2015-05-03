package com.yumfee.extremeworld.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Discussion;

public interface DiscussionDao extends PagingAndSortingRepository<Discussion, Long>{

	public Page<Discussion> findByCourseId(Long courseId,Pageable pageable);
	public Page<Discussion> findByUserIdIn(Collection<Long> ids, Pageable pageable);
}
