package com.yumfee.extremeworld.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Sponsorship;

public interface SponsorshipDao extends PagingAndSortingRepository<Sponsorship,Long>{

	@Query("SELECT s FROM Sponsorship s WHERE s.hobby.id=? AND s.trade.tradeStatus='TRADE_SUCCESS'")
	Page<Sponsorship> findByHobbyId(Long hobbyId, Pageable pageable);
}
