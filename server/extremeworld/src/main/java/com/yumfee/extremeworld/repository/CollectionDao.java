package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Collection;

public interface CollectionDao extends PagingAndSortingRepository<Collection, Long>{
	
	public Page<Collection> findByUserIdAndStatus(Long userId, int status, Pageable pageable);
	public Collection findByUserIdAndTopicId(Long userId, Long topicId);
	public Collection findByUserIdAndTopicIdAndStatus(Long userId, Long topicId, int status);
}
