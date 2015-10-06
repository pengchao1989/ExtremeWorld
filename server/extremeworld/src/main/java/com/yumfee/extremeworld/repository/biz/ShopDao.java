package com.yumfee.extremeworld.repository.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.biz.Shop;

public interface ShopDao extends  PagingAndSortingRepository<Shop,Long>{

	@Query("SELECT t FROM Shop t LEFT JOIN t.hobbys h WHERE h.id=?")
	public Page<Shop> findByHobby(Long hobbyId,  Pageable pageable);
}
