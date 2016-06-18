package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.TopicScore;

public interface TopicScoreDao extends PagingAndSortingRepository<TopicScore, Long> {

}
