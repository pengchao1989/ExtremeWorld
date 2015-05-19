package com.yumfee.extremeworld.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Discussion;

public interface DiscussionDao extends PagingAndSortingRepository<Discussion, Long>{

	public Page<Discussion> findByCourseId(Long courseId,Pageable pageable);
	public Page<Discussion> findByUserIdIn(Collection<Long> ids, Pageable pageable);
	public Page<Discussion> findByTaxonomyId(Long taxonomyId, Pageable pageable);
	
	@Query("SELECT d FROM Discussion d LEFT JOIN d.hobbys h WHERE h.id=?")
	public Page<Discussion> findByHobbyId(Long hobbyId, Pageable pageable);
	@Query("SELECT d FROM Discussion d LEFT JOIN d.hobbys h WHERE h.id=?  AND d.taxonomy.id=?")
	public Page<Discussion> findByHobbyIdAndTaxonomyId(Long hobbyId, Long taxonomyId, Pageable pageable);
}
