package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Mood;

public interface MoodDao extends PagingAndSortingRepository<Mood,Long>{

}
