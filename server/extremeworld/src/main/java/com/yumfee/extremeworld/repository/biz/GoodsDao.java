package com.yumfee.extremeworld.repository.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.biz.Goods;

public interface GoodsDao extends  PagingAndSortingRepository<Goods,Long>{

	Page<Goods> findByShopId(Long id, Pageable pageRequest);
	
	@Query("SELECT g FROM Goods g LEFT JOIN g.categorys c WHERE c.id=?")
	Page<Goods> findByCategoryId(Long id, Pageable pageRequest);
}