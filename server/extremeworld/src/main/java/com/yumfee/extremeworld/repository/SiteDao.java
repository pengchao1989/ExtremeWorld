package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Site;

public interface SiteDao extends PagingAndSortingRepository<Site, Long>
{
	@Query("SELECT s FROM Site s LEFT JOIN s.hobbys h WHERE h.id=? AND isDelete = 0")
	Page<Site> findByHobbyId(Long hobbyId, Pageable pageable);
}
