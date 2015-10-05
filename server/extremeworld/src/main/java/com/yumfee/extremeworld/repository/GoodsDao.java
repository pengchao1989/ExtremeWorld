package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Goods;

public interface GoodsDao extends  PagingAndSortingRepository<Goods,Long>{

	Page<Goods> findByShopId(Long id, Pageable pageRequest);
}
