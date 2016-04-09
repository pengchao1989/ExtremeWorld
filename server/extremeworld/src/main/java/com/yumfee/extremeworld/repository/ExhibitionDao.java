package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Exhibition;

public interface ExhibitionDao extends PagingAndSortingRepository<Exhibition, Long>{

	@Query("SELECT e FROM Exhibition e LEFT JOIN e.hobbys h WHERE h.id=? AND status=?")
	Page<Exhibition> findByHobbyIdAndStatus(Long hobbyId, int status, Pageable pageable);
}
