package com.yumfee.extremeworld.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Danmu;

public interface DanmuDao extends PagingAndSortingRepository<Danmu, Long>{

	List<Danmu> findByVideoId(Long id);
}
