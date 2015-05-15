package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Video;

public interface VideoDao extends PagingAndSortingRepository<Video, Long>
{
	public Page<Video> findByUserId(Long user, Pageable pageable);
	
	//public Page<Video> findByHobbyId(Long hobbyId, Pageable pageable);
}
