package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Topic;

public interface TopicDao extends PagingAndSortingRepository<Topic, Long>
{

}
