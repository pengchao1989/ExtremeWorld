package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Video;

public interface VideoDao extends PagingAndSortingRepository<Video, Long>
{

}
