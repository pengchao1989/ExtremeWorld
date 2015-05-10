package com.yumfee.extremeworld.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.News;

public interface NewsDao extends PagingAndSortingRepository<News, Long>{

}
