package com.yumfee.extremeworld.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yumfee.extremeworld.entity.Topic;

public interface TopicDao extends PagingAndSortingRepository<Topic, Long>
{
/*	@Query("SELECT tb_topic FROM tb_course_topic  LEFT JOIN tb_topic ON tb_course_topic.topic_id = tb_topic.id WHERE tb_course_topic.course_id = ?")
	public Page<Topic> findByCourseId(Long courseId,Pageable pageable);*/
	public Page<Topic> findByCourseIdAndMagicType(Long courseId,String magicType, Pageable pageable);
	public Page<Topic> findByUserIdIn(Collection<Long> ids, Pageable pageable);
	
	public Page<Topic> findByUserId(Long user, Pageable pageable);
	public Page<Topic> findByUserIdAndMediaWrapNotNull(Long user,  Pageable pageable);
	
	public Page<Topic> findByType(String type, Pageable pageable);
	
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=?")
	public Page<Topic> findByHobby(Long hobbyId,  Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=?")
	public Page<Topic> findByHobbyAndType(Long hobbyId, String type, Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND type=? AND t.taxonomy.id=?")
	public Page<Topic> findByHobbyAndTypeAndTaxonomy(Long hobbyId,  String type, Long taxonomyId,Pageable pageable);
	
	@Query("SELECT t FROM Topic t LEFT JOIN t.hobbys h WHERE h.id=? AND t.fine=1")
	public Page<Topic> findByHobbyAndFine(Long hobbyId, Pageable pageable);
	
	
}
